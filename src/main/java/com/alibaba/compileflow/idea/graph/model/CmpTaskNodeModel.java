package com.alibaba.compileflow.idea.graph.model;

/**
 * @author zhan
 */
public class CmpTaskNodeModel extends BaseNodeModel {

    private ActionModel inAction;
    private ActionModel outAction;

    private String eventName;

    //zhan
    private String txnCode;
    private String autofillFields;
    private String requiredFields;
    private String allAutofillFlag;


    private CmpTaskNodeModel() {
    }

    public static CmpTaskNodeModel of() {
        return new CmpTaskNodeModel();
    }

    public static CmpTaskNodeModel getFromCellValue(Object cellValue) {
        return (CmpTaskNodeModel)cellValue;
    }

    public ActionModel getInAction() {
        return inAction;
    }

    public void setInAction(ActionModel inAction) {
        this.inAction = inAction;
    }

    public ActionModel getOutAction() {
        return outAction;
    }

    public void setOutAction(ActionModel outAction) {
        this.outAction = outAction;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }


    public String getAutofillFields() {
        return autofillFields;
    }

    public void setAutofillFields(String autofillFields) {
        this.autofillFields = autofillFields;
    }

    public String getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(String requiredFields) {
        this.requiredFields = requiredFields;
    }

    public String getAllAutofillFlag() {
        return allAutofillFlag;
    }

    public void setAllAutofillFlag(String allAutofillFlag) {
        this.allAutofillFlag = allAutofillFlag;
    }
}
