package com.alibaba.compileflow.idea.graph.nodeview.component;

import com.alibaba.compileflow.idea.graph.util.DialogUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhan
 */
public class TxnFieldsMapPanel extends JPanel {

    private JScrollPane scrollPane = new JBScrollPane() {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 300);
        }
    };

    private JButton addRowBtn = new JButton("增加映射行");
    private JButton copyRowBtn = new JButton("复制映射行");
    private JButton deleteRowBtn = new JButton("删除映射行");

    //    private String[] columnNames = new String[] {"sceneCode1", "txnCode1", "fieldCode1, sceneCode2", "txnCode2", "fieldCode2"};
    private String[] columnNames = new String[]{"原场景码", "原交易码", "原字段码", "当前字段码"};
    private DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    private JTable table = new JBTable(tableModel);

    private Project project;

    public TxnFieldsMapPanel(Project project) {
        super(new MigLayout("inset 20"));
        this.project = project;
        initView();
        initListener();
    }

    public void insertData(int row, Data data) {
        tableModel.insertRow(row,
                new String[]{data.sceneCode, data.txnCode, data.oldFieldCode, data.newFieldCode});
    }

    public List<Data> getDataList() {
        List<Data> dataList = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            Data data = new Data();
            data.sceneCode = (String) table.getModel().getValueAt(i, 0);
            data.txnCode = (String) table.getModel().getValueAt(i, 1);
            data.oldFieldCode = (String) table.getModel().getValueAt(i, 2);
            data.newFieldCode = (String) table.getModel().getValueAt(i, 3);
            dataList.add(data);
        }
        return dataList;
    }

    private void initView() {
        // add table to scroll
        scrollPane.setViewportView(table);
        table.setRowHeight(30);

        //add scroll and button to panel
        this.add(scrollPane, "wrap");

//        this.add(addRowBtn, "gap para");
//        this.add(copyRowBtn, "gap para");
//        this.add(deleteRowBtn, "span, wrap");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // 使用FlowLayout

//        this.add(addRowBtn, "gap para");
//        this.add(copyRowBtn, "gap para");
//        this.add(deleteRowBtn, "span, wrap");

        buttonPanel.add(addRowBtn);
        buttonPanel.add(copyRowBtn);
        buttonPanel.add(deleteRowBtn);

        this.add(buttonPanel); // 将按钮面板添加到主容器

    }


    private void initListener() {
        // add button listener
        addRowBtn.addActionListener((e) -> {
            tableModel.addRow(new String[]{"", "", "", ""});
            tableModel.fireTableDataChanged();
        });
        //delete button listener
        deleteRowBtn.addActionListener((e) -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                tableModel.removeRow(row);
                tableModel.fireTableDataChanged();
            }
        });
        //table listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int colum = table.getSelectedColumn();
//                if (0 == colum) {
//                    DialogUtil.prompt("请选择场景。", null, null);
//                }
            }
        });
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public static class Data {
        public String sceneCode;
        public String txnCode;
        public String oldFieldCode;
        public String newFieldCode;
    }


}
