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
package com.alibaba.compileflow.idea.graph.nodeview;

import com.alibaba.compileflow.idea.graph.model.*;
import com.alibaba.compileflow.idea.graph.mxgraph.Graph;
import com.alibaba.compileflow.idea.graph.nodeview.dialog.*;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.mxgraph.model.mxCell;

/**
 * Create properties dialog
 *
 * @author xuan
 * @since 2019/3/10
 */
public class NodeEditDialogFactory {

    public static DialogWrapper getDialog(Project project, mxCell cell, Graph graph) {

        if (null == cell || cell.getValue() == null) {
            return null;
        }

        Object node = cell.getValue();

        if (node instanceof ScriptTaskNodeModel) {
            return new ScriptTaskDialog(project, cell, graph);
        }

        if (node instanceof AutoTaskNodeModel || node instanceof DecisionNodeModel) {
            return new AutoTaskDialog(project, cell, graph);
        }

        if (node instanceof LoopProcessNodeModel) {
            return new LoopTaskDialog(project, cell, graph);
        }

        if (node instanceof SubBpmNodeModel) {
            return new SubBpmDialog(project, cell, graph);
        }

        if (node instanceof UserTaskNodeModel) {
            return new UserTaskDialog(project, cell, graph);
        }

        if (node instanceof WaitTaskNodeModel) {
            return new WaitTaskDialog(project, cell, graph);
        }

        //zhan
        if (node instanceof TxnTaskNodeModel) {
            return new TxnTaskDialog(project, cell, graph);
        }

        if (node instanceof NoteNodeModel) {
            return new NoteDialog(project, cell, graph);
        }

        if (node instanceof StartNodeModel || node instanceof EndNodeModel) {
            return new NoActionDialog(project, cell, graph);
        }

        if (node instanceof ContinueNodeModel) {
            return new ContinueDialog(project, cell, graph);
        }

        if (node instanceof BreakNodeModel) {
            return new BreakDialog(project, cell, graph);
        }

        if (node instanceof WaitEventModel) {
            return new WaitEventDialog(project, cell, graph);
        }

        throw new IllegalStateException("node can find model. node class:" + node.getClass());
    }

}
