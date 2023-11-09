package com.alibaba.compileflow.idea.graph.model;

import java.util.List;

/**
 * @author zhan
 */
public class CaseTaskNodeModel extends BaseNodeModel {

    private String caseCode;

    private String collectionVarName;
    private String variableName;
    private String indexVarName;
    private String variableClass;
    private String startNodeId;
    private String endNodeId;
    private List<BaseNodeModel> allNodes;

    private CaseTaskNodeModel() {
    }

    public static CaseTaskNodeModel of() {
        return new CaseTaskNodeModel();
    }

    public static CaseTaskNodeModel getFromCellValue(Object cellValue) {
        return (CaseTaskNodeModel)cellValue;
    }

    ////////////////////////////// get set //////////////////////////////

    public String getCollectionVarName() {
        return collectionVarName;
    }

    public void setCollectionVarName(String collectionVarName) {
        this.collectionVarName = collectionVarName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getIndexVarName() {
        return indexVarName;
    }

    public void setIndexVarName(String indexVarName) {
        this.indexVarName = indexVarName;
    }

    public String getVariableClass() {
        return variableClass;
    }

    public void setVariableClass(String variableClass) {
        this.variableClass = variableClass;
    }

    public String getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(String startNodeId) {
        this.startNodeId = startNodeId;
    }

    public String getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(String endNodeId) {
        this.endNodeId = endNodeId;
    }

    public List<BaseNodeModel> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(List<BaseNodeModel> allNodes) {
        this.allNodes = allNodes;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    ////////////////////////////// ability //////////////////////////////

    public String[] getSubNodeIds() {
        String[] nodeIds = null;
        if (null != allNodes && allNodes.size() > 0) {
            nodeIds = new String[allNodes.size()];
            for (int i = 0, n = allNodes.size(); i < n; i++) {
                nodeIds[i] = allNodes.get(i).getId();
            }
        }
        return nodeIds;
    }

    public BaseNodeModel getNode(String nodeId) {
        if (null == allNodes || null == nodeId) {
            return null;
        }
        for (BaseNodeModel subModel : allNodes) {
            if (nodeId.equals(subModel.getId())) {
                return subModel;
            }
        }
        return null;
    }

}
