package com.alibaba.compileflow.idea.graph.nodeview.dialog;

import com.alibaba.compileflow.idea.graph.model.*;
import com.alibaba.compileflow.idea.graph.mxgraph.Graph;
import com.alibaba.compileflow.idea.graph.nodeview.component.ActionPanel;
import com.alibaba.compileflow.idea.graph.nodeview.component.CmpTaskPanel;
import com.alibaba.compileflow.idea.graph.nodeview.component.CmpFrontTxnConfPanel;
import com.alibaba.compileflow.idea.graph.nodeview.component.CmpTaskPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTabbedPane;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUndoableEdit;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 *
 * @author zhan
 */
public  class CmpTaskDialog extends DialogWrapper {

    protected mxCell cell;
    protected Graph graph;
    protected Project project;

    protected JBTabbedPane rootTab = new JBTabbedPane();
//    protected NodeBasicPanel basicPanel = new NodeBasicPanel();
    protected CmpTaskPanel taskPanel;
    protected JPanel paramPanel;

    public CmpTaskDialog(@Nullable Project project, mxCell cell, Graph graph) {
        super(project);
        taskPanel = new CmpTaskPanel(project);
        this.cell = cell;
        this.graph = graph;
        this.paramPanel = getParamPanel(project, graph, cell);
        this.project = project;
        setTitle(getDialogTitle());
        setModal(true);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        initView();
        initData();
        this.taskPanel.refresh();
        return rootTab;
    }

    @Override
    public void doOKAction() {
        super.doOKAction();
        CmpTaskNodeModel baseModel = CmpTaskNodeModel.getFromCellValue(cell.getValue());
        String oldId = baseModel.getId();
        CmpTaskPanel.Data basicData = taskPanel.getData();
        baseModel.setName(basicData.name);
        baseModel.setId(basicData.id);
        baseModel.setTag(basicData.tag);
        baseModel.setG(basicData.g);
        baseModel.setTxnCode(basicData.txnCode);
        baseModel.setSkipFlag(basicData.skipFlag);
        baseModel.setBackFlag(basicData.backFlag);
        baseModel.setRequiredFields(basicData.requiredFields);
        baseModel.setAutofillFields(basicData.autofillFields);

        baseModel.setAutofillFields(mergeDataListToString(taskPanel.getMappingPanel().getDataList()));

        //fix transitionTo if modify
        BpmModel bpmModel = BpmModel.getFromGraphModel(graph.getModel());
        bpmModel.fixTransitionTo(oldId, basicData.id);

        // notify change
        graph.refresh();
        mxUndoableEdit edit = new mxUndoableEdit(graph.getModel());
        graph.getModel().fireEvent(new mxEventObject(mxEvent.CHANGE, "edit", edit, "changes", edit.getChanges()));
    }

    protected void initView() {
        rootTab.addTab("基本配置", new JLabel("基础配置信息"));
        if (null != paramPanel) {
            rootTab.addTab("交易处理配置", new JLabel("交易处理配置"));
        }

        rootTab.setComponentAt(0, taskPanel);
        if (null != paramPanel) {
            rootTab.setComponentAt(1, paramPanel);
        }

    }

    private void initData() {
        if (null == cell) {
            return;
        }

        CmpTaskNodeModel baseModel = CmpTaskNodeModel.getFromCellValue(cell.getValue());
        CmpTaskPanel.Data basicData = new CmpTaskPanel.Data();
        basicData.name = baseModel.getName();
        basicData.id = baseModel.getId();
        basicData.tag = baseModel.getTag();
        basicData.g = baseModel.getG();
        basicData.txnCode = baseModel.getTxnCode();
        basicData.skipFlag = baseModel.getSkipFlag();
        basicData.backFlag = baseModel.getBackFlag();
        basicData.requiredFields = baseModel.getRequiredFields();
        basicData.autofillFields = baseModel.getAutofillFields();
        taskPanel.setData(basicData);//设置panel控件数据

        if (baseModel.getAutofillFields() != null) {
            List<CmpFrontTxnConfPanel.Data> dataList = parseMergedString(baseModel.getAutofillFields());
            for (Vector<String> vector : toVectorList(dataList)) {
                taskPanel.getMappingPanel().getTableModel().addRow(vector);
            }
        }

    }

    /**
     * Get dialog title
     *
     * @return title
     */
    protected String getDialogTitle() {
        return "前端组件节点";
    }


    private String mergeDataListToString(List<CmpFrontTxnConfPanel.Data> dataList) {
        return dataList.stream()
                .map(data -> {
                    String line = "";
                    if (!data.sceneCode.isEmpty()) {
                        line += data.sceneCode + ":";
                    }
                    if (!data.txnCode.isEmpty()) {
                        line += data.txnCode + ":";
                    }
                    if (!data.oldFieldCode.isEmpty()) {
                        line += data.oldFieldCode + ":";
                    }
                    return line + data.newFieldCode;
                })
                .collect(Collectors.joining(","));
    }


    private List<CmpFrontTxnConfPanel.Data> parseMergedString(String mergedString) {
        List<CmpFrontTxnConfPanel.Data> dataList = new ArrayList<>();
        String[] lines = mergedString.split(",");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(":");
            if (fields.length < 4) {
                StringBuilder sb = new StringBuilder(line);
                for (int i = 0; i < 4 - fields.length; i++) {
                    sb.insert(0, ":");
                }
                line = sb.toString();
                fields = line.split(":");
            }

            CmpFrontTxnConfPanel.Data data = new CmpFrontTxnConfPanel.Data();
            data.sceneCode = fields[0];
            data.txnCode = fields[1];
            data.oldFieldCode = fields[2];
            data.newFieldCode = fields[3];
            dataList.add(data);
        }
        return dataList;
    }

    private List<Vector<String>> toVectorList(List<CmpFrontTxnConfPanel.Data> dataList) {
        List<Vector<String>> vectorList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (CmpFrontTxnConfPanel.Data var : dataList) {
                Vector<String> vector = new Vector<String>();
                vector.add(var.sceneCode);
                vector.add(var.txnCode);
                vector.add(var.oldFieldCode);
                vector.add(var.newFieldCode);
                vectorList.add(vector);
            }
        }
        return vectorList;

    }

    protected Set<String> getContextVarNameSet() {
        BpmModel bpmModel = BpmModel.getFromGraphModel(graph.getModel());
        return bpmModel.getContextVarNameSet();
    }

    //===
    protected JPanel getParamPanel(Project project, Graph graph, mxCell cell) {
        return new ActionPanel(project);
    }

    protected void initParamPanelView() {
        ActionPanel.initContextVarNameComboBox(paramPanel, graph);
        ((ActionPanel) paramPanel).setJumpToSourceActionCallback((s) -> {
            doCancelAction();
            return null;
        });
    }

    protected void initParamPanelData() {
        ActionPanel.data2View(paramPanel, ActionModel.getActionFromCellValue(cell.getValue()));
    }

    protected void doParamSave() {
        ActionModel actionModel = ActionModel.getActionFromCellValue(cell.getValue());
        if (null == actionModel) {
            actionModel = ActionModel.of();
        }

        ActionModel.setActionToCellValue(cell.getValue(), actionModel);
        ActionPanel.view2Data(paramPanel, actionModel);
    }

}