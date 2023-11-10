package com.alibaba.compileflow.idea.graph.nodeview.component;

import com.intellij.openapi.project.Project;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * @author zhan
 */
public class TxnTaskPanel extends JPanel {

    private JLabel nameLabel = new JLabel("节点名称");
    private JTextField nameText = new JTextField(20);
    private JLabel idLabel = new JLabel("节点ID");
    private JTextField idText = new JTextField(20);
    private JLabel tagLabel = new JLabel("节点标签");
    private JTextField tagText = new JTextField(20);
    private JLabel gLabel = new JLabel("g");
    private JTextField gText = new JTextField(20);

    private JLabel txnCodeLabel = new JLabel("交易码:");
    private JTextField txnCodeText = new JTextField(20);
    private JLabel requiredLabel = new JLabel("必填栏位:");
    private JTextField requiredFields = new JTextField(80);
    private JLabel autofillFieldsLabel = new JLabel("自动反填栏位:");
    private JTextField autofillFields = new JTextField(80);

    private JLabel skipFlagLabel = new JLabel("是否允许跳过:");

    private JRadioButton skipFlagYBtn = new JRadioButton("允许");
    private JRadioButton skipFlagNBtn = new JRadioButton("不允许", true);
    private ButtonGroup skipFlagGroup = new ButtonGroup();
    private JLabel backFlagLabel = new JLabel("是否允许回退:");
    private JRadioButton backFlagYBtn = new JRadioButton("允许");
    private JRadioButton backFlagNBtn = new JRadioButton("不允许", true);
    private ButtonGroup backFlagGroup = new ButtonGroup();

    private String skipFlag;
    private String backFlag;
    private TxnFieldsMappingPanel mappingPanel;

    public TxnTaskPanel(Project project) {
        super(new MigLayout("inset 20"));
        mappingPanel = new TxnFieldsMappingPanel(project);
        initView();
        initListener();
    }

    private void initView() {
        this.add(txnCodeLabel, "gap para");
        this.add(txnCodeText, "span, wrap");
        this.add(nameLabel, "gap para");
        this.add(nameText, "span, wrap");
        //this.add(tagLabel, "gap para");
        //this.add(tagText, "span, wrap");
        this.add(idLabel, "gap para");
        this.add(idText, "span, wrap");

        this.add(new JSeparator(), "span, growx, wrap, gaptop 10, gapbottom 10");

        this.add(autofillFieldsLabel, "gap para");
        this.add(autofillFields, "wrap");
        this.add(requiredLabel, "gap para");
        this.add(requiredFields, "wrap");

        if (skipFlag == null) {
            skipFlag = "0";
        }
        this.add(skipFlagLabel, "gap para");
        JPanel group1 = new JPanel();
        group1.add(skipFlagYBtn);
        group1.add(skipFlagNBtn);
        skipFlagGroup.add(skipFlagYBtn);
        skipFlagGroup.add(skipFlagNBtn);
        this.add(group1, "wrap");

        if (backFlag == null) {
            backFlag = "0";
        }
        this.add(backFlagLabel, "gap para");
        JPanel group2 = new JPanel();
        group2.add(backFlagYBtn);
        group2.add(backFlagNBtn);
        backFlagGroup.add(backFlagYBtn);
        backFlagGroup.add(backFlagNBtn);
        this.add(group2, "wrap");

        this.add(new JSeparator(), "span, growx, wrap, gaptop 10, gapbottom 10");
        //fields mapping
        this.add(mappingPanel, "span, growx, wrap 10");
        refresh();
    }

    private void initListener() {
        skipFlagYBtn.addActionListener((e) -> {
            if (skipFlagYBtn.isSelected()) {
                skipFlag = "1";
                refresh();
            }
        });
        skipFlagNBtn.addActionListener((e) -> {
            if (skipFlagNBtn.isSelected()) {
                skipFlag = "0";
                refresh();
            }
        });
        backFlagYBtn.addActionListener((e) -> {
            if (backFlagYBtn.isSelected()) {
                backFlag = "1";
                refresh();
            }
        });
        backFlagNBtn.addActionListener((e) -> {
            if (backFlagNBtn.isSelected()) {
                backFlag = "0";
                refresh();
            }
        });
    }

    public void refresh() {
        skipFlagYBtn.setSelected(skipFlag != null && !skipFlag.equals("0"));
        backFlagYBtn.setSelected(backFlag != null && !backFlag.equals("0"));
    }

    public TxnTaskPanel.Data getData() {
        TxnTaskPanel.Data data = new TxnTaskPanel.Data();
        data.name = nameText.getText();
        data.id = idText.getText();
        data.tag = tagText.getText();
        data.g = gText.getText();
        data.txnCode = txnCodeText.getText();
        data.skipFlag = skipFlag;
        data.backFlag = backFlag;
        data.requiredFields = requiredFields.getText();
        data.autofillFields = autofillFields.getText();
        return data;
    }

    public void setData(TxnTaskPanel.Data data) {
        nameText.setText(data.name);
        idText.setText(data.id);
        tagText.setText(data.tag);
        gText.setText(data.g);
        txnCodeText.setText(data.txnCode);
        autofillFields.setText(data.autofillFields);
        requiredFields.setText(data.requiredFields);
        skipFlag = data.skipFlag;
        backFlag = data.backFlag;
    }

    public static class Data {
        public String name;
        public String id;
        public String tag;
        public String g;
        public String txnCode;
        public String skipFlag;
        public String backFlag;
        public String autofillFields;
        public String requiredFields;
        public String allAutofillFlag;
    }

    public TxnFieldsMappingPanel getMappingPanel() {
        return mappingPanel;
    }
}
