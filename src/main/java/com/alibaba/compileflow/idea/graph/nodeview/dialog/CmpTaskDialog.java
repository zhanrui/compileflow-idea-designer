package com.alibaba.compileflow.idea.graph.nodeview.dialog;

import com.alibaba.compileflow.idea.graph.model.ActionModel;
import com.alibaba.compileflow.idea.graph.model.BaseNodeModel;
import com.alibaba.compileflow.idea.graph.model.BpmModel;
import com.alibaba.compileflow.idea.graph.mxgraph.Graph;
import com.alibaba.compileflow.idea.graph.nodeview.component.ActionPanel;
import com.alibaba.compileflow.idea.graph.nodeview.component.CmpTaskPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTabbedPane;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUndoableEdit;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Set;

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
    protected CmpTaskPanel taskPanel = new CmpTaskPanel();
    protected JPanel paramPanel;

    public CmpTaskDialog(@Nullable Project project, mxCell cell, Graph graph) {
        super(project);
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
        return rootTab;
    }

    @Override
    public void doOKAction() {
        super.doOKAction();

        BaseNodeModel baseNodeModel = BaseNodeModel.getBaseNodeFromCellValue(cell.getValue());
        String oldId = baseNodeModel.getId();
        CmpTaskPanel.Data basicData = taskPanel.getData();
        baseNodeModel.setName(basicData.name);
        baseNodeModel.setId(basicData.id);
        baseNodeModel.setTag(basicData.tag);
        baseNodeModel.setG(basicData.g);
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
            rootTab.addTab("action setting", new JLabel("action setting"));
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

        BaseNodeModel baseModel = BaseNodeModel.getBaseNodeFromCellValue(cell.getValue());
        CmpTaskPanel.Data basicData = new CmpTaskPanel.Data();
        basicData.name = baseModel.getName();
        basicData.id = baseModel.getId();
        basicData.tag = baseModel.getTag();
        basicData.g = baseModel.getG();
        taskPanel.setData(basicData);

    }

    /**
     * Get dialog title
     *
     * @return title
     */
    protected  String getDialogTitle(){
        return "前端组件节点";
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
        ((ActionPanel)paramPanel).setJumpToSourceActionCallback((s) -> {
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
