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
package com.alibaba.compileflow.idea.graph.nodeview.component;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author zhan
 */
public class TxnNodeBasicPanel extends JPanel {

    /**
     * name
     */
    private JLabel nameLabel = new JLabel("柜面交易名称");
    private JTextField nameText = new JTextField(20);
    private JLabel txnCodeLabel = new JLabel("柜面交易码");
    private JTextField txnCodeText = new JTextField(20);
    /**
     * id
     */
    private JLabel idLabel = new JLabel("id");
    private JTextField idText = new JTextField(10);
    /**
     * tag
     */
    private JLabel tagLabel = new JLabel("tag");
    private JTextField tagText = new JTextField(20);
    /**
     * g
     */
    private JLabel gLabel = new JLabel("g");
    private JTextField gText = new JTextField(20);

    public TxnNodeBasicPanel() {
        super(new MigLayout("inset 20"));
        initView();
    }

    private void initView() {
        this.add(nameLabel, "gap para");
        this.add(nameText, "span, growx, wrap");
        this.add(txnCodeLabel, "gap para");
        this.add(txnCodeText, "span, growx, wrap");
        this.add(idLabel, "gap para");
        this.add(idText, "span, growx, wrap");
        this.add(tagLabel, "gap para");
        this.add(tagText, "span, growx, wrap");
        this.add(gLabel, "gap para");
        this.add(gText, "span, growx, wrap");
    }

    public TxnNodeBasicPanel.Data getData() {
        TxnNodeBasicPanel.Data data = new TxnNodeBasicPanel.Data();
        data.name = nameText.getText();
        data.txnCode = txnCodeText.getText();
        data.id = idText.getText();
        data.tag = tagText.getText();
//        data.tag = txnCodeText.getText(); //同txnCode
        data.g = gText.getText();
        return data;
    }

    public void setData(TxnNodeBasicPanel.Data data) {
        nameText.setText(data.name);
        txnCodeText.setText(data.txnCode);
        idText.setText(data.id);
        tagText.setText(data.tag);
        gText.setText(data.g);
    }

    public static class Data {
        public String name;
        public String txnCode;
        public String id;
        public String tag;
        public String g;
    }

    public static void main(String[] args) {
        TxnNodeBasicPanel panel = new TxnNodeBasicPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        TxnNodeBasicPanel.Data data = new TxnNodeBasicPanel.Data();
        data.name = "name1";
        data.txnCode = "100201";
        data.id = "123";
        data.tag = "Hi";
        data.g = "1,2,3,4";
        panel.setData(data);
    }
}
