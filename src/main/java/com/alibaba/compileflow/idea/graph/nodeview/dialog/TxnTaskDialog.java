/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.compileflow.idea.graph.nodeview.dialog;

import com.alibaba.compileflow.idea.graph.model.ActionModel;
import com.alibaba.compileflow.idea.graph.model.TxnTaskNodeModel;
import com.alibaba.compileflow.idea.graph.mxgraph.Graph;
import com.alibaba.compileflow.idea.graph.nodeview.component.ActionPanel;
import com.alibaba.compileflow.idea.graph.nodeview.component.TxnNodeBasicPanel;
import com.alibaba.compileflow.idea.graph.nodeview.component.TxnNodeEventPanel;
import com.alibaba.compileflow.idea.graph.util.StringUtil;
import com.intellij.openapi.project.Project;
import com.mxgraph.model.mxCell;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author zhan
 */
public class TxnTaskDialog extends TxnBaseMultiTabDialog {

    public TxnTaskDialog(@Nullable Project project, mxCell cell, Graph graph) {
        super(project, cell, graph);
    }

    @Override
    protected String getDialogTitle() {
        return "场景子交易配置";
    }

    @Override
    protected void initParamPanelView() {
        ActionPanel.initContextVarNameComboBox(panels[1], graph);
        ((ActionPanel)panels[1]).setJumpToSourceActionCallback((s) -> {
            doCancelAction();
            return null;
        });
    }

    @Override
    protected void initParamPanelData() {

        TxnTaskNodeModel model = TxnTaskNodeModel.getFromCellValue(cell.getValue());

        //EventName
        TxnNodeEventPanel eventPanel = (TxnNodeEventPanel)panels[0];
        eventPanel.getEventNameField().setText(StringUtil.trimToEmpty(model.getEventName()));
        eventPanel.getTxnCodeField().setText(StringUtil.trimToEmpty(model.getTxnCode()));

        //inAction
        ActionPanel.data2View(panels[1], model.getInAction());
    }

    @Override
    protected void doParamSave() {
        TxnTaskNodeModel nodeModel = TxnTaskNodeModel.getFromCellValue(cell.getValue());

        //EventName
        TxnNodeEventPanel eventPanel = (TxnNodeEventPanel)panels[0];
        nodeModel.setEventName(StringUtil.trimToEmpty(eventPanel.getEventNameField().getText()));
        nodeModel.setTxnCode(StringUtil.trimToEmpty(eventPanel.getTxnCodeField().getText()));

        //txnCode
//        TxnNodeBasicPanel basicPanel = (TxnNodeBasicPanel)panels[1];
//        nodeModel.setTxnCode(StringUtil.trimToEmpty(basicPanel.getData().txnCode));

        //inAction
        ActionModel inAction = nodeModel.getInAction();
        if (null == inAction) {
            inAction = ActionModel.of();
            nodeModel.setInAction(inAction);
        }
        ActionPanel.view2Data(panels[1], inAction);
        nodeModel.setInAction(ActionPanel.isActionSettingPanelEmpty(panels[1]) ? null : inAction);
    }

    @Override
    protected JPanel[] getPanels() {
        return new JPanel[] {
            new TxnNodeEventPanel(), new ActionPanel(project)};
    }

    @Override
    protected String[] getTabNames() {
        return new String[] {"交易事件配置", "交易前置处理配置"};
    }

}
