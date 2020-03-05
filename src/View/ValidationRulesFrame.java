/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.ValidationRulesModel;
import Util.FolderUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;


/**
 * Created by osboxes on 11/05/16.
 */
public class ValidationRulesFrame extends JFrame {
    
    /**
     * The Model
     */
    private final ValidationRulesModel model = new ValidationRulesModel();
    /**
     * JPanel *
     */
    private final JPanel panel_validationRules_1 = new JPanel();
    private final JPanel panel_validationRules_2 = new JPanel();
    /**
     * JLabel
     */
    private final JLabel label_selectColumnName = new JLabel(" Select the column name:");
    private final JLabel label_selectColumnValue = new JLabel(" Select the column value:");
    /**
     * JButton *
     */
    private final JButton button_back = new JButton("Back");
    private final JButton button_removeRules = new JButton("Save rules");
    private final JButton button_runApriori = new JButton("Generate rules");
    private final JButton button_getColumnValues = new JButton("Get column values");
    private final JButton button_save_rules = new JButton("Flush ruleset");
    private final JButton button_see_rules = new JButton("Compare 2 rules");


    /**
     * JSplitPane *
     */
    private JSplitPane splitPane;
    /**
     * JComboBox
     */
    String[] tip_coloana = {"S-Lum", "S-Cont", "S-MucANT", "S-MucJAC", "S-MucCORP",
        "SlezANT", "S-LezJAC", "S-LezCORP", "Pilor"};
    JComboBox jBox_columnName = new JComboBox(tip_coloana);
    JComboBox jbox_columnValue = new JComboBox();

    /**
     * JTable *
     */
    private JTable table;
    /**
     * DefaultTableModel *
     */
    private DefaultTableModel tableModel;
    Object[] columnNames = {"Rules", "Support", "Select"};

    /**
     * JScrollPane *
     */
    private JScrollPane scrollPanel;

    HashMap<ArrayList<String>, ArrayList<String>> map = new HashMap<ArrayList<String>, ArrayList<String>>();

    /**
     * Constructorul clasei
     *
     * @param prefferedSize - dimensiunea frame-ului
     */
    public ValidationRulesFrame(Dimension prefferedSize) {
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "Do you want to close the application??",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    Arrays.stream(Objects.requireNonNull(new File(FolderUtil.getCurrentPossition() + "apriori\\").listFiles())).forEach(File::delete);
                    System.exit(0);
                }
            }
        };
        addWindowListener(exitListener);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(prefferedSize);
        setVisible(false);
        setTitle("Generate Annotation Rules");
        init();
    }

    private void init() {

        JPanel panel_validationRule1_1 = new JPanel();
        panel_validationRule1_1.setLayout(new GridLayout(6, 4, 50, 0));
        for (int i = 0; i < 8; i++) {
            panel_validationRule1_1.add(new JLabel());
        }

        panel_validationRule1_1.add(label_selectColumnName);
        label_selectColumnName.setToolTipText(label_selectColumnName.getText());
        label_selectColumnName.setFont(new Font("Serif", Font.ITALIC, 18));
        panel_validationRule1_1.add(jBox_columnName);
        panel_validationRule1_1.add(new JLabel());
        panel_validationRule1_1.add(button_runApriori);

        for (int i = 0; i < 4; i++) {
            panel_validationRule1_1.add(new JLabel());
        }

        panel_validationRule1_1.add(label_selectColumnValue);
        label_selectColumnValue.setToolTipText(label_selectColumnValue.getText());
        label_selectColumnValue.setFont(new Font("Serif", Font.ITALIC, 18));
        panel_validationRule1_1.add(jbox_columnValue);
        panel_validationRules_1.add(panel_validationRule1_1, BorderLayout.CENTER);
        panel_validationRule1_1.add(new JLabel());
        panel_validationRule1_1.add(button_save_rules);

        for (int i = 0; i < 4; i++) {
            panel_validationRule1_1.add(new JLabel());
        }
        //panel_validationRule1_1.add(new JLabel());


        JPanel panel_validationRule1_2 = new JPanel(new GridLayout(1, 4, 10, 10));

        panel_validationRules_1.setLayout(new BorderLayout());
        panel_validationRules_1.add(panel_validationRule1_1, BorderLayout.NORTH);
        panel_validationRules_1.add(panel_validationRule1_2, BorderLayout.SOUTH);

        panel_validationRules_2.setLayout(new BorderLayout());
        JPanel panel_validationRules_2_1 = new JPanel(new BorderLayout());
        JPanel panel_validationRules_2_2 = new JPanel(new GridLayout(3,3,70,20));

       for (int i = 0; i < 3; i++) {
            panel_validationRules_2_2.add(new JLabel());
        }
        panel_validationRules_2_2.add(button_back);
        panel_validationRules_2_2.add(button_see_rules);
        panel_validationRules_2_2.add(button_removeRules);
        for (int i = 0; i < 3; i++) {
            panel_validationRules_2_2.add(new JLabel());
        }

        tableModel = new DefaultTableModel();
        tableModel.setDataVector(new Object[][]{}, columnNames);
        table = new JTable(tableModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    default:
                        return Boolean.class;
                }
            }
        };

        table.setFont(new Font("Serif", Font.ITALIC, 22));
        table.setRowHeight(22);
        table.getColumnModel().getColumn(0).setMinWidth(420);

        JTableHeader anHeader = table.getTableHeader();
        anHeader.setForeground(Color.BLUE);
        anHeader.setFont(new Font("Serif", Font.ITALIC, 21));

        table.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tableModel);                
                
        sorter.setComparator(1, new ValidationRulesFrame.SorterComparator<String>());
        table.setRowSorter(sorter);

        table.setFillsViewportHeight(true);
        scrollPanel = new JScrollPane(table);
        panel_validationRules_2_1.add(scrollPanel);

        panel_validationRules_2.add(panel_validationRules_2_1, BorderLayout.CENTER);
        panel_validationRules_2.add(panel_validationRules_2_2, BorderLayout.SOUTH);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(panel_validationRules_1);
        splitPane.setBottomComponent(panel_validationRules_2);
        setContentPane(splitPane);
        jBox_columnName.actionPerformed(null);
        pack();
        splitPane.setDividerLocation(0.2);
        setLocationRelativeTo(null);
    }

    public void addActionListenerButtonBack(ActionListener a) {
        button_back.addActionListener(a);
    }

    public void addActionListenerButtonGetColumnValues(ActionListener a) {
        button_getColumnValues.addActionListener(a);
    }
    //jBox_columnName
    public void addActionListenerComboColumnValues(ActionListener a) {
        jBox_columnName.addActionListener(a);
    }

    public void addActionListenerButtonRemoveRules(ActionListener a) {
        button_removeRules.addActionListener(a);
    }

    public void addActionListnerButtonRunApriori(ActionListener a) {
        button_runApriori.addActionListener(a);
    }
    
    public void addActionListenerButtonSaveRules(ActionListener a){
        button_save_rules.addActionListener(a);
    }
    public void addActionListenerButtonSeeRules(ActionListener a){
        button_see_rules.addActionListener(a);
    }
    
    public void addItemListenerComboBoxChanged(ItemListener a) {
        jbox_columnValue.addItemListener(a);
    }

    public void addTableActionListener(ListSelectionListener e){
        table.getSelectionModel().addListSelectionListener(e);
    }
    public String getSelectedItemComboBoxName() {

        Object obj = jBox_columnName.getSelectedItem();
        return obj.toString();
    }

    public String getSelectedItemComboBoxValue() {

        Object obj = jbox_columnValue.getSelectedItem();
        if (jbox_columnValue.getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, " Please select the column value ! ", "Error !", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return obj.toString();
    }

    public void setComboBoxValues(ArrayList<String> columnValues) {
        jbox_columnValue.removeAllItems();
        for (String val : columnValues) {
            jbox_columnValue.addItem(val);
        }
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTable() {
        return table;
    }


    public ArrayList<String> setAprioriParameters(String title) {
        if (title.equals("")){
            title="Apriori parameters";
        }

        ArrayList<String> lista_flags = new ArrayList<String>();
        JTextField text_confidence = new JTextField("85");
        JTextField text_suport = new JTextField("30");
        Object[] fields = {
            "Confidence", text_confidence,
            "Support", text_suport
        };

        int op = JOptionPane.showConfirmDialog(null, fields, title, JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            lista_flags.add(text_confidence.getText());
            lista_flags.add(text_suport.getText());
        }
        return lista_flags;
    }

    private class SorterComparator<E> implements Comparator {

        @Override
        public int compare(Object s1, Object s2) {
            return ((String) s1).compareTo((String) s2);

        }
    }
}
