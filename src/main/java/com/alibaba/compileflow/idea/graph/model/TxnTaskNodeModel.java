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
package com.alibaba.compileflow.idea.graph.model;

/**
 * @author zhan
 */
public class TxnTaskNodeModel extends BaseNodeModel {

    private ActionModel inAction;
    private ActionModel outAction;

    private String eventName;

    //zhan
    private String txnCode;
    private String autofillFields;
    private String requiredFields;
    private String allAutofillFlag;


    private TxnTaskNodeModel() {
    }

    public static TxnTaskNodeModel of() {
        return new TxnTaskNodeModel();
    }

    public static TxnTaskNodeModel getFromCellValue(Object cellValue) {
        return (TxnTaskNodeModel)cellValue;
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
