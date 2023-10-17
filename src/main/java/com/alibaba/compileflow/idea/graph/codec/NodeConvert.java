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
package com.alibaba.compileflow.idea.graph.codec;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.compileflow.engine.definition.common.var.IVar;
import com.alibaba.compileflow.engine.definition.tbbpm.*;
import com.alibaba.compileflow.idea.graph.model.*;

/**
 * @author xuan
 * @since 2020/5/19
 */
public class NodeConvert {

    public static FlowNode toNode(BaseNodeModel model) {

        if (null == model) {
            return null;
        }

        if (model instanceof AutoTaskNodeModel) {

            AutoTaskNodeModel autoTaskNodeModel = (AutoTaskNodeModel)model;
            AutoTaskNode autoTaskNode = new AutoTaskNode();
            //
            fillToNode(autoTaskNode, autoTaskNodeModel);
            autoTaskNode.setAction(ActionConvert.toAction(autoTaskNodeModel.getAction()));
            return autoTaskNode;

        } else if (model instanceof DecisionNodeModel) {

            DecisionNodeModel decisionNodeModel = (DecisionNodeModel)model;
            DecisionNode decisionNode = new DecisionNode();
            //
            fillToNode(decisionNode, decisionNodeModel);
            decisionNode.setAction(ActionConvert.toAction(decisionNodeModel.getAction()));
            return decisionNode;

        } else if (model instanceof EndNodeModel) {

            EndNodeModel endNodeModel = (EndNodeModel)model;
            EndNode endNode = new EndNode();
            //
            fillToNode(endNode, endNodeModel);
            return endNode;
        } else if (model instanceof LoopProcessNodeModel) {

            LoopProcessNodeModel loopProcessNodeModel = (LoopProcessNodeModel)model;
            LoopProcessNode loopProcessNode = new LoopProcessNode();
            //
            fillToNode(loopProcessNode, loopProcessNodeModel);
            loopProcessNode.setCollectionVarName(loopProcessNodeModel.getCollectionVarName());
            loopProcessNode.setVariableName(loopProcessNodeModel.getVariableName());
            loopProcessNode.setIndexVarName(loopProcessNodeModel.getIndexVarName());
            loopProcessNode.setVariableClass(loopProcessNodeModel.getVariableClass());
            loopProcessNode.setStartNodeId(loopProcessNodeModel.getStartNodeId());
            loopProcessNode.setEndNodeId(loopProcessNodeModel.getEndNodeId());
            if (null != loopProcessNodeModel.getAllNodes()) {
                loopProcessNodeModel.getAllNodes().forEach(
                    subModel -> loopProcessNode.addNode(NodeConvert.toNode(subModel)));
            }
            return loopProcessNode;

        } else if (model instanceof NoteNodeModel) {

            NoteNodeModel noteNodeModel = (NoteNodeModel)model;
            NoteNode noteNode = new NoteNode();
            //
            fillToNode(noteNode, noteNodeModel);
            noteNode.setComment(noteNodeModel.getComment());
            noteNode.setVisible("true".equals(noteNodeModel.getVisible()));
            return noteNode;

        } else if (model instanceof ScriptTaskNodeModel) {

            ScriptTaskNodeModel scriptTaskNodeModel = (ScriptTaskNodeModel)model;
            ScriptTaskNode scriptTaskNode = new ScriptTaskNode();
            //
            fillToNode(scriptTaskNode, scriptTaskNodeModel);
            scriptTaskNode.setAction(ActionConvert.toAction(scriptTaskNodeModel.getAction()));
            return scriptTaskNode;

        } else if (model instanceof StartNodeModel) {
            StartNodeModel startNodeModel = (StartNodeModel)model;
            StartNode startNode = new StartNode();
            //
            fillToNode(startNode, startNodeModel);
            return startNode;

        } else if (model instanceof SubBpmNodeModel) {

            SubBpmNodeModel subBpmNodeModel = (SubBpmNodeModel)model;
            SubBpmNode subBpmNode = new SubBpmNode();
            //
            fillToNode(subBpmNode, subBpmNodeModel);
            subBpmNode.setType(subBpmNodeModel.getType());
            subBpmNode.setSubBpmCode(subBpmNodeModel.getSubBpmCode());
            subBpmNode.setWaitForCompletion(subBpmNodeModel.isWaitForCompletion());
            List<IVar> varList = VarConvert.toParameterDefineList(subBpmNodeModel.getVars());
            if (null != varList) {
                varList.forEach(subBpmNode::addVar);
            }
            return subBpmNode;

        } else if (model instanceof WaitTaskNodeModel) {

            WaitTaskNodeModel waitTaskNodeModel = (WaitTaskNodeModel)model;
            WaitTaskNode waitTaskNode = new WaitTaskNode();
            //
            fillToNode(waitTaskNode, waitTaskNodeModel);
            waitTaskNode.setInAction(ActionConvert.toAction(waitTaskNodeModel.getInAction()));
            waitTaskNode.setOutAction(ActionConvert.toAction(waitTaskNodeModel.getOutAction()));
            return waitTaskNode;
       } else if (model instanceof TxnTaskNodeModel) { //zhan

            TxnTaskNodeModel nodeModel = (TxnTaskNodeModel)model;
            TxnTaskNode node = new TxnTaskNode();
            //
            fillToNode(node, nodeModel);
            node.setTxnCode(nodeModel.getTxnCode());
            node.setEventName(nodeModel.getEventName());
            node.setTag(node.getTxnCode());//TODO

            node.setInAction(ActionConvert.toAction(nodeModel.getInAction()));
            node.setOutAction(ActionConvert.toAction(nodeModel.getOutAction()));
            return node;
        } else if (model instanceof ContinueNodeModel) {
            ContinueNodeModel continueNodeModel = (ContinueNodeModel)model;
            ContinueNode continueNode = new ContinueNode();
            fillToNode(continueNode, continueNodeModel);
            continueNode.setExpression(continueNodeModel.getExpression());
            return continueNode;
        } else if (model instanceof BreakNodeModel) {
            BreakNodeModel breakNodeModel = (BreakNodeModel)model;
            BreakNode breakNode = new BreakNode();
            fillToNode(breakNode, breakNodeModel);
            breakNode.setExpression(breakNodeModel.getExpression());
            return breakNode;
        } else if (model instanceof WaitEventModel) {
            WaitEventModel waitEventModel = (WaitEventModel)model;
            WaitEventTaskNode waitEventNode = new WaitEventTaskNode();
            fillToNode(waitEventNode, waitEventModel);
            waitEventNode.setEventName(waitEventModel.getEventName());
            waitEventNode.setInAction(ActionConvert.toAction(waitEventModel.getInAction()));
            return waitEventNode;
        }

        return null;
    }

    public static List<FlowNode> toNodeList(List<BaseNodeModel> modelList) {

        if (null == modelList) {
            return null;
        }

        return modelList.stream().map(NodeConvert::toNode).filter(Objects::nonNull).collect(
            Collectors.toList());
    }

    public static BaseNodeModel toModel(FlowNode node) {

        if (null == node) {
            return null;
        }

        if (node instanceof AutoTaskNode) {
            AutoTaskNode autoTaskNode = (AutoTaskNode)node;
            AutoTaskNodeModel model = AutoTaskNodeModel.of();
            //
            fillToModel(model, autoTaskNode);
            model.setAction(ActionConvert.toModel(autoTaskNode.getAction()));
            return model;
        } else if (node instanceof DecisionNode) {
            DecisionNode decisionNode = (DecisionNode)node;
            DecisionNodeModel model = DecisionNodeModel.of();
            //
            fillToModel(model, decisionNode);
            model.setAction(ActionConvert.toModel(decisionNode.getAction()));
            return model;
        } else if (node instanceof EndNode) {
            EndNode endNode = (EndNode)node;
            EndNodeModel model = EndNodeModel.of();
            //
            fillToModel(model, endNode);
            return model;
        } else if (node instanceof LoopProcessNode) {
            LoopProcessNode loopProcessNode = (LoopProcessNode)node;
            LoopProcessNodeModel model = LoopProcessNodeModel.of();
            //
            fillToModel(model, loopProcessNode);
            model.setCollectionVarName(loopProcessNode.getCollectionVarName());
            model.setVariableName(loopProcessNode.getVariableName());
            model.setIndexVarName(loopProcessNode.getIndexVarName());
            model.setVariableClass(loopProcessNode.getVariableClass());
            model.setStartNodeId(loopProcessNode.getStartNodeId());
            model.setEndNodeId(loopProcessNode.getEndNodeId());
            model.setAllNodes(NodeConvert.toModelList(loopProcessNode.getAllNodes()));
            return model;
        } else if (node instanceof NoteNode) {
            NoteNode noteNode = (NoteNode)node;
            NoteNodeModel model = NoteNodeModel.of();
            //
            fillToModel(model, noteNode);
            model.setComment(noteNode.getComment());
            model.setVisible(noteNode.isVisible() ? "true" : "false");
            return model;
        } else if (node instanceof ScriptTaskNode) {
            ScriptTaskNode scriptTaskNode = (ScriptTaskNode)node;
            ScriptTaskNodeModel model = ScriptTaskNodeModel.of();
            //
            fillToModel(model, scriptTaskNode);
            model.setAction(ActionConvert.toModel(scriptTaskNode.getAction()));
            return model;
        } else if (node instanceof StartNode) {
            StartNode startNode = (StartNode)node;
            StartNodeModel model = StartNodeModel.of();
            //
            fillToModel(model, startNode);
            return model;
        } else if (node instanceof SubBpmNode) {
            SubBpmNode subBpmNode = (SubBpmNode)node;
            SubBpmNodeModel model = SubBpmNodeModel.of();
            //
            fillToModel(model, subBpmNode);
            model.setType(subBpmNode.getType());
            model.setSubBpmCode(subBpmNode.getSubBpmCode());
            model.setWaitForCompletion(subBpmNode.isWaitForCompletion());
            model.setVars(VarConvert.toModelList(subBpmNode.getVars()));
            return model;
        } else if (node instanceof WaitTaskNode) {
            WaitTaskNode waitTaskNode = (WaitTaskNode)node;
            WaitTaskNodeModel model = WaitTaskNodeModel.of();
            //
            fillToModel(model, waitTaskNode);
            model.setInAction(ActionConvert.toModel(waitTaskNode.getInAction()));
            model.setOutAction(ActionConvert.toModel(waitTaskNode.getOutAction()));
            return model;
        } else if (node instanceof ContinueNode) {
            ContinueNode continueNode = (ContinueNode)node;
            ContinueNodeModel model = ContinueNodeModel.of();
            fillToModel(model, continueNode);
            model.setExpression(continueNode.getExpression());
            return model;
        } else if (node instanceof BreakNode) {
            BreakNode breakNode = (BreakNode)node;
            BreakNodeModel model = BreakNodeModel.of();
            fillToModel(model, breakNode);
            model.setExpression(breakNode.getExpression());
            return model;
        } else if (node instanceof WaitEventTaskNode) {
            WaitEventTaskNode waitEventNode = (WaitEventTaskNode)node;
            WaitEventModel model = WaitEventModel.of();
            fillToModel(model, waitEventNode);
            model.setEventName(waitEventNode.getEventName());
            model.setInAction(ActionConvert.toModel(waitEventNode.getInAction()));
            return model;
        } else if (node instanceof TxnTaskNode) {
            TxnTaskNode taskNode = (TxnTaskNode)node;
            TxnTaskNodeModel model = TxnTaskNodeModel.of();
            fillToModel(model, taskNode);
            model.setEventName(taskNode.getEventName());
            model.setTxnCode(taskNode.getTxnCode());
            model.setInAction(ActionConvert.toModel(taskNode.getInAction()));
            return model;
        }

        return null;
    }

    public static List<BaseNodeModel> toModelList(List<FlowNode> nodeList) {

        if (null == nodeList) {
            return null;
        }

        return nodeList.stream().map(NodeConvert::toModel).filter(Objects::nonNull).collect(
            Collectors.toList());
    }

    private static void fillToNode(FlowNode node, BaseNodeModel model) {

        if (null == node || null == model) {
            return;
        }
        node.setId(model.getId());
        node.setG(model.getG());
        node.setName(model.getName());
        node.setTag(model.getTag());
        //
        List<Transition> inTransitionList = TransitionConvert.toTransitionList(model.getInTransitions());
        if (null != inTransitionList) {
            inTransitionList.forEach(node::addIncomingTransition);
        }
        List<Transition> outTransitionList = TransitionConvert.toTransitionList(model.getOutTransitions());
        if (null != outTransitionList) {
            outTransitionList.forEach(node::addOutgoingTransition);
        }
    }

    private static void fillToModel(BaseNodeModel model, FlowNode node) {

        if (null == node || null == model) {
            return;
        }
        model.setId(node.getId());
        model.setG(node.getG());
        model.setName(node.getName());
        model.setTag(node.getTag());
        //
        model.setOutTransitions(TransitionConvert.toModelList(node.getOutgoingTransitions()));
    }

}
