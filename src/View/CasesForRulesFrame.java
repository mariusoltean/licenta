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
public class CasesForRulesFrame extends JFrame {

    /**
     * The Model
     */
    private final AbbreviationsEditingModel model = new AbbreviationsEditingModel();

    /**
     * JPanel *
     */
    private final JPanel panel_abrrevEditing_1 = new JPanel();
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
    private JTable table;

    /**
     * JScrollPane *
     */
    private JScrollPane scrollPanel;

    private final JTextField text_abbrev = new JTextField();
    private final JTextField text_sign = new JTextField();

    private Object[] fields = {
            "Index", text_abbrev,
            "Context", text_sign
    };

    private DefaultTableModel tableModel;
    Object[] columnNames = {"Index", "Context"};

    /**
     * constructor without parameters
     */
    public CasesForRulesFrame() {
    }

    /**
     * constructor with parameter
     *
     * @param prefferedSize
     */
    public CasesForRulesFrame(Dimension prefferedSize) {
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

        JPanel panel_abrevEdit1_1 = new JPanel(new GridLayout(3, 6, 5, 5));

        for (int i = 0; i < 9; i++) {
            panel_abrevEdit1_1.add(new JLabel());
        }
        for (int i = 0; i < 5; i++) {
            panel_abrevEdit1_1.add(new JLabel());
        }
        //panel_abrevEdit1_1.add(button_search);

        JPanel panel_abrevEdit1_2 = new JPanel(new GridLayout(1, 5, 10, 10));

        panel_abrevEdit1_2.add(new JLabel());
        panel_abrevEdit1_2.add(new JLabel());
        panel_abrevEdit1_2.add(new JLabel());
        panel_abrevEdit1_2.add(new JLabel());
        panel_abrevEdit1_2.add(new JLabel());

        panel_abrrevEditing_1.setLayout(new BorderLayout());
        panel_abrrevEditing_1.add(panel_abrevEdit1_1, BorderLayout.NORTH);
        panel_abrrevEditing_1.add(panel_abrevEdit1_2, BorderLayout.SOUTH);

        panel_abrrevEditing_2.setLayout(new BorderLayout());
        JPanel panel_abrevEdit2_1 = new JPanel(new BorderLayout());
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
        tableModel = new DefaultTableModel();
        tableModel.setDataVector(new Object[][]{}, columnNames);
        table = new JTable(tableModel){

            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    //comment row, exclude heading
                    if(colIndex != 0){
                        tip = getValueAt(rowIndex, colIndex).toString();
                    }
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }

                return tip;
            }
        };
        table.setFillsViewportHeight(true);
        scrollPanel = new JScrollPane(table);
        panel_abrevEdit2_1.add(scrollPanel);

        table.setFont(new Font("Serif", Font.ITALIC, 20));
        table.setRowHeight(20);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tableModel);
        sorter.setComparator(1, new SorterComparator<String>());
        table.setRowSorter(sorter);

        JTableHeader anHeader = table.getTableHeader();
        anHeader.setForeground(Color.BLUE);
        anHeader.setFont(new Font("Serif", Font.ITALIC, 20));

        panel_abrrevEditing_2.add(panel_abrevEdit2_1, BorderLayout.CENTER);
        panel_abrrevEditing_2.add(panel_abrevEdit2_2, BorderLayout.SOUTH);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(panel_abrrevEditing_1);
        splitPane.setBottomComponent(panel_abrrevEditing_2);
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
    public DefaultTableModel getTableModel() {
        return this.tableModel;
    }

    /**
     *
     * @return table to the controller - AbbreviationsEditingController
     */
    public JTable getTable() {
        return table;
    }

}
