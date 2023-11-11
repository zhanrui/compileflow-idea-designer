package com.alibaba.compileflow.idea.graph.nodeview.component;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author zhan
 */
public class CmpTaskPanel1 extends JPanel {

    private JLabel nameLabel = new JLabel("节点名称");
    private JTextField nameText = new JTextField(20);
    private JLabel idLabel = new JLabel("节点ID");
    private JTextField idText = new JTextField(10);
    private JLabel tagLabel = new JLabel("节点标签");
    private JTextField tagText = new JTextField(20);
    private JLabel gLabel = new JLabel("g");
    private JTextField gText = new JTextField(20);

    private JLabel txnCodeLabel = new JLabel("前端组件码:");
    private JTextField txnCodeField = new JTextField(30);
    private JLabel autofillFieldsLabel = new JLabel("自动反填栏位:");
    private JTextField autofillFields = new JTextField(80);
    private JLabel requiredLabel = new JLabel("必填栏位:");
    private JTextField requiredFields = new JTextField(80);


    public CmpTaskPanel1() {
        super(new MigLayout("inset 20"));
        initView();
    }

    private void initView() {
//        this.setPreferredSize(new Dimension(1000, 400));

        this.add(txnCodeLabel, "gap para");
        this.add(txnCodeField, "wrap");
        this.add(nameLabel, "gap para");
        this.add(nameText, "span, wrap");
        this.add(tagLabel, "gap para");
        this.add(tagText, "span, wrap");
        this.add(idLabel, "gap para");
        this.add(idText, "span, wrap");
//        this.add(gLabel, "gap para");
//        this.add(gText, "span, growx, wrap");

        this.add(new JSeparator(), "span, growx, wrap, gaptop 10, gapbottom 10");
        this.add(autofillFieldsLabel, "gap para");
        this.add(autofillFields, "wrap");
        this.add(requiredLabel, "gap para");
        this.add(requiredFields, "wrap");


    }

    public CmpTaskPanel1.Data getData() {
        CmpTaskPanel1.Data data = new CmpTaskPanel1.Data();
        data.name = nameText.getText();
        data.id = idText.getText();
        data.tag = tagText.getText();
        data.g = gText.getText();
        return data;
    }

    public void setData(CmpTaskPanel1.Data data) {
        nameText.setText(data.name);
        idText.setText(data.id);
        tagText.setText(data.tag);
        gText.setText(data.g);
    }

    public static class Data {
        public String name;
        public String id;
        public String tag;
        public String g;
        public String txnCode;
    }

    public static void main(String[] args) {
        CmpTaskPanel1 panel = new CmpTaskPanel1();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        CmpTaskPanel1.Data data = new CmpTaskPanel1.Data();
        data.name = "name1";
        data.id = "123";
        data.tag = "Hi";
        data.g = "1,2,3,4";
        panel.setData(data);
    }
}
