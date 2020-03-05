/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.AbbreviationsEditingModel;
import Util.FolderUtil;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;


/**
 * Created by osboxes on 11/05/16.
 */
public class CompareRulesFrame extends JFrame {

    /**
     * The Model
     */
    private final AbbreviationsEditingModel model = new AbbreviationsEditingModel();

    /**
     * JPanel *
     */
    //private final JPanel panel_abrrevEditing_1 = new JPanel();
    private final JPanel panel_abrrevEditing_2 = new JPanel();

    /**
     * JButtons *
     */
    private final JButton button_back = new JButton("Back");
    /**
     * JSplitPane *
     */
    private JSplitPane splitPane;
    /**
     * JTable *
     */
    private JTable tableAmB;
    private JTable tableAiB;
    private JTable tableBmA;

    /**
     * JScrollPane *
     */
    private JScrollPane scrollPanel;
    private JScrollPane scrollPanelAiB;
    private JScrollPane scrollPanelBmA;

    private final JTextField text_abbrev = new JTextField();
    private final JTextField text_sign = new JTextField();

    private Object[] fields = {
            "Index", text_abbrev,
            "Context", text_sign
    };

    private DefaultTableModel tableModelAmB;
    private DefaultTableModel tableModelAiB;
    private DefaultTableModel tableModelBmA;
    Object[] columnNamesAmB = {"First - Second"};
    Object[] columnNamesAiB = {"Intersection"};
    Object[] columnNamesBmA = {"Second - First"};

    /**
     * constructor without parameters
     */
    public CompareRulesFrame() {
    }

    /**
     * constructor with parameter
     *
     * @param prefferedSize
     */
    public CompareRulesFrame(Dimension prefferedSize) {
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
        init();
    }

    private void init() {







        panel_abrrevEditing_2.setLayout(new BorderLayout());
        JPanel panel_abrevEdit2_1 = new JPanel(new GridLayout(1,2));
        JPanel panel_abrevEdit2_2 = new JPanel(new GridLayout(3, 4, 50, 0));
        for (int i = 0; i < 4; i++) {
            panel_abrevEdit2_2.add(new JLabel());
        }
        panel_abrevEdit2_2.add(button_back);
        //panel_abrevEdit2_2.add(button_save);
        //panel_abrevEdit2_2.add(button_delete);
        //panel_abrevEdit2_2.add(button_addRow);
        for (int i = 0; i < 4; i++) {
            panel_abrevEdit2_2.add(new JLabel());
        }
        tableModelAmB = new DefaultTableModel();
        tableModelAmB.setDataVector(new Object[][]{}, columnNamesAmB);
        tableAmB = new JTable(tableModelAmB){

            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    //comment row, exclude heading
                    if(colIndex == 0){
                        tip = getValueAt(rowIndex, colIndex).toString();
                    }
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }

                return tip;
            }
        };
        tableAmB.setFillsViewportHeight(true);
        scrollPanel = new JScrollPane(tableAmB);
        panel_abrevEdit2_1.add(scrollPanel);

        tableAmB.setFont(new Font("Serif", Font.ITALIC, 20));
        tableAmB.setRowHeight(20);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableAmB.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tableModelAmB);
        sorter.setComparator(0, new SorterComparator<String>());
        tableAmB.setRowSorter(sorter);

        JTableHeader anHeader = tableAmB.getTableHeader();
        anHeader.setForeground(Color.BLUE);
        anHeader.setFont(new Font("Serif", Font.ITALIC, 20));




        //third table
        tableModelAiB = new DefaultTableModel();
        tableModelAiB.setDataVector(new Object[][]{}, columnNamesAiB);
        tableAiB = new JTable(tableModelAiB){

            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    //comment row, exclude heading
                    if(colIndex == 0){
                        tip = getValueAt(rowIndex, colIndex).toString();
                    }
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }

                return tip;
            }
        };
        tableAiB.setFillsViewportHeight(true);
        scrollPanelAiB = new JScrollPane(tableAiB);
        panel_abrevEdit2_1.add(scrollPanelAiB);

        tableAiB.setFont(new Font("Serif", Font.ITALIC, 20));
        tableAiB.setRowHeight(20);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableAiB.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorterAiB = new TableRowSorter<DefaultTableModel>(tableModelAiB);
        sorterAiB.setComparator(0, new SorterComparator<String>());
        tableAiB.setRowSorter(sorterAiB);

        JTableHeader anHeaderAiB = tableAiB.getTableHeader();
        anHeaderAiB.setForeground(Color.BLUE);
        anHeaderAiB.setFont(new Font("Serif", Font.ITALIC, 20));


        //second table
        tableModelBmA = new DefaultTableModel();
        tableModelBmA.setDataVector(new Object[][]{}, columnNamesBmA);
        tableBmA = new JTable(tableModelBmA){

            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    //comment row, exclude heading
                    if(colIndex == 0){
                        tip = getValueAt(rowIndex, colIndex).toString();
                    }
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }

                return tip;
            }
        };
        tableBmA.setFillsViewportHeight(true);
        scrollPanelBmA = new JScrollPane(tableBmA);
        panel_abrevEdit2_1.add(scrollPanelBmA);

        tableBmA.setFont(new Font("Serif", Font.ITALIC, 20));
        tableBmA.setRowHeight(20);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableBmA.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorterBmA = new TableRowSorter<DefaultTableModel>(tableModelBmA);
        sorterBmA.setComparator(0, new SorterComparator<String>());
        tableBmA.setRowSorter(sorterBmA);

        JTableHeader anHeaderBmA = tableBmA.getTableHeader();
        anHeaderBmA.setForeground(Color.BLUE);
        anHeaderBmA.setFont(new Font("Serif", Font.ITALIC, 20));
        //end of tables
        panel_abrrevEditing_2.add(panel_abrevEdit2_1, BorderLayout.CENTER);
        panel_abrrevEditing_2.add(panel_abrevEdit2_2, BorderLayout.SOUTH);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        //splitPane.setTopComponent(panel_abrrevEditing_1);
        splitPane.setTopComponent(panel_abrrevEditing_2);
        setContentPane(splitPane);

        pack();
        splitPane.setDividerLocation(0.2);
        setLocationRelativeTo(null);
    }


    /**
     * Add actionListener to button_back
     *
     * @param a
     */
    public void addActionListenerButtonBack(ActionListener a) {
        button_back.addActionListener(a);
    }


    private class SorterComparator<E> implements Comparator {

        @Override
        public int compare(Object s1, Object s2) {
            return ((String) s1).compareTo((String) s2);
        }
    }



    /**
     *
     * @return tableModel to controller - AbbreviationsEditingController
     */
    public DefaultTableModel getTableModelAiB() {
        return this.tableModelAiB;
    }
    public DefaultTableModel getTableModelBmA() {
        return this.tableModelBmA;
    }
    public DefaultTableModel getTableModelAmB() {
        return this.tableModelAmB;
    }

    /**
     *
     * @return table to the controller - AbbreviationsEditingController
     */
    public JTable getTableAmB() {
        return tableAmB;
    }
    public JTable getTableAiB() {
        return tableAiB;
    }
    public JTable getTableBma() {
        return tableBmA;
    }

}
