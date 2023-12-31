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
package com.alibaba.compileflow.idea.graph.codec.impl.tbbpm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.compileflow.engine.FlowModel;
import com.alibaba.compileflow.engine.ProcessEngineFactory;
import com.alibaba.compileflow.engine.common.CompileFlowException;
import com.alibaba.compileflow.engine.common.DirectedGraph;
import com.alibaba.compileflow.engine.definition.common.EndElement;
import com.alibaba.compileflow.engine.definition.common.TransitionNode;
import com.alibaba.compileflow.engine.definition.common.TransitionSupport;
import com.alibaba.compileflow.engine.definition.tbbpm.TbbpmModel;
import com.alibaba.compileflow.engine.process.preruntime.converter.impl.TbbpmModelConverter;
import com.alibaba.compileflow.engine.runtime.impl.AbstractProcessRuntime;
import com.alibaba.compileflow.engine.runtime.impl.TbbpmProcessRuntime;
import com.alibaba.compileflow.idea.graph.codec.ModelCodeConvertExt;
import com.alibaba.compileflow.idea.graph.model.BpmModel;

/**
 * @author xuan
 * @since 2020/5/21
 */
public class TbbpmModelCodeConvertExtImpl implements ModelCodeConvertExt {

    @Override
    public String getJavaTestCode(BpmModel bpmModel, String xml) {
        return buildProcessRuntime(bpmModel, xml).generateTestCode();
        //return ProcessEngineFactory.getProcessEngine().getTestCode(xml);
    }

    @Override
    public String getJavaCode(BpmModel bpmModel, String xml) {
        return buildProcessRuntime(bpmModel, xml).generateJavaCode();
        //return ProcessEngineFactory.getProcessEngine().getJavaCode(xml);
    }

    private AbstractProcessRuntime<TbbpmModel> buildProcessRuntime(BpmModel bpmModel, String xml) {
        //to tbbpmModel
        StringFlowStreamSource stringFlowStreamSource = new StringFlowStreamSource(xml);
        TbbpmModel tbbpmModel = TbbpmModelConverter.getInstance().convertToModel(stringFlowStreamSource);

        checkCycle(tbbpmModel);
        checkContinuous(tbbpmModel);
        sortTransition(tbbpmModel);

        AbstractProcessRuntime<TbbpmModel> processRuntime = TbbpmProcessRuntime.of(tbbpmModel);

        processRuntime.init();
        return processRuntime;
    }

    private void sortTransition(TbbpmModel tbbpmModel) {
        tbbpmModel.getAllNodes().forEach(node -> node.getTransitions()
            .sort(Comparator.comparing(TransitionSupport::getPriority).reversed()));
    }

    private void checkCycle(TbbpmModel tbbpmModel) {
        DirectedGraph directedGraph = new DirectedGraph();
        for (TransitionNode node : tbbpmModel.getAllNodes()) {
            List<TransitionNode> outgoingNodes = node.getOutgoingNodes();
            if (isNotEmpty(outgoingNodes)) {
                outgoingNodes.forEach(
                    outgoingNode -> directedGraph.add(DirectedGraph.Edge.of(node, outgoingNode)));
            }
        }
        List<TransitionNode> cyclicVertexList = directedGraph.findCyclicVertexList();
        if (isNotEmpty(cyclicVertexList)) {
            throw new CompileFlowException("Cyclic nodes found in flow " + tbbpmModel.getCode()
                + " check node [" + cyclicVertexList.stream().map(TransitionNode::getId)
                .collect(Collectors.joining(",")) + "]");
        }
    }

    private void checkContinuous(TbbpmModel tbbpmModel) {
        ArrayList<TransitionNode> visitedNodes = new ArrayList<>();
        checkContinuous(tbbpmModel.getStartNode(), visitedNodes, tbbpmModel);
    }

    private void checkContinuous(TransitionNode node, List<TransitionNode> visitedNodes, FlowModel flowModel) {
        visitedNodes.add(node);
        if (node instanceof EndElement) {
            return;
        }
        List<TransitionNode> outgoingNodes = node.getOutgoingNodes();
        if (isEmpty(outgoingNodes)) {
            throw new CompileFlowException("Flow should end with an end node " + flowModel);
        }

        for (TransitionNode outgoingNode : outgoingNodes) {
            if (!visitedNodes.contains(outgoingNode)) {
                checkContinuous(outgoingNode, visitedNodes, flowModel);
            }
        }
    }

    private <T> boolean isEmpty(List<T> list) {
        return null == list || list.isEmpty();
    }

    private <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

}
