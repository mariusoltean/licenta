/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.AbbreviationsEditingModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;


/**
 * Created by osboxes on 11/05/16.
 */
public class AbbreviationsEditingFrame extends JFrame {
    
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
    private final JButton button_save = new JButton("Save");
    private final JButton button_delete = new JButton("Delete");
    private final JButton button_addRow = new JButton("Add row");
    private final JButton button_search = new JButton("Find word");
    private final JButton button_findInDictionary = new JButton("Validate word");
    private static String possitioningCommand="pwd>enterPath.txt";
    private static String pathFile="enterPath.txt";

    private static String getCurrentPossition() throws IOException, InterruptedException {
        Process p1 = Runtime.getRuntime().exec(new String[]{"sh","-c",possitioningCommand});
        p1.waitFor();
        BufferedReader reader = new BufferedReader(new FileReader(pathFile));
        return reader.readLine()+"/apriori/";
    }
    /**
     * JLabel *
     */
    private final JLabel label_search = new JLabel("Search word: ");
    /**
     * JTextField *
     */
    private final JTextField field_search = new JTextField();
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
        "Abbreviation", text_abbrev,
        "Significance", text_sign
    };
    
    private DefaultTableModel tableModel;
    Object[] columnNames = {"Abbreviations", "Significance"};

    /**
     * constructor without parameters
     */
    public AbbreviationsEditingFrame() {
    }

    /**
     * constructor with parameter
     *
     * @param prefferedSize
     */
    public AbbreviationsEditingFrame(Dimension prefferedSize) {
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "Do you want to close the application??",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    try {
                        Runtime.getRuntime().exec(new String[]{"sh","-c","rm -R "+getCurrentPossition()+"*" });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }


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
        panel_abrevEdit1_1.add(label_search);
        panel_abrevEdit1_1.add(field_search);
        for (int i = 0; i < 4; i++) {
            panel_abrevEdit1_1.add(new JLabel());
        }
        panel_abrevEdit1_1.add(button_search);
        panel_abrevEdit1_1.add(button_findInDictionary);

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
        panel_abrevEdit2_2.add(button_save);
        panel_abrevEdit2_2.add(button_delete);
        panel_abrevEdit2_2.add(button_addRow);
        for (int i = 0; i < 4; i++) {
            panel_abrevEdit2_2.add(new JLabel());
        }
        tableModel = new DefaultTableModel();
        tableModel.setDataVector(new Object[][]{}, columnNames);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        scrollPanel = new JScrollPane(table);
        panel_abrevEdit2_1.add(scrollPanel);

        table.setFont(new Font("Serif", Font.ITALIC, 20));
        table.setRowHeight(20);

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
    
    public String getText_abbrev() {
        return text_abbrev.getText();
    }

    public String getText_sign() {
        return text_sign.getText();
    }

    /**
     * Add actionListener to button_back
     *
     * @param a
     */
    public void addActionListenerButtonBack(ActionListener a) {
        button_back.addActionListener(a);
    }

    /**
     * Add actionListener to button_save
     *
     * @param a
     */
    public void addActionListenerButtonSave(ActionListener a) {
        button_save.addActionListener(a);
    }

    /**
     * Add actionListener to button_delete
     *
     * @param a
     */
    public void addActionListenerButtonDelete(ActionListener a) {
        button_delete.addActionListener(a);
    }

    /**
     * Add actionListener to button_addRow
     *
     * @param a
     */
    public void addActionListenerButtonAddRow(ActionListener a) {
        button_addRow.addActionListener(a);
    }

    /**
     * Add actionListener to button_search
     *
     * @param a
     */
    public void addActionListenerButtonSearch(ActionListener a) {
        button_search.addActionListener(a);
    }
    public void addActionListenerButtonFindInDictionary(ActionListener a) {
        button_findInDictionary.addActionListener(a);
    }

    private class SorterComparator<E> implements Comparator {

        @Override
        public int compare(Object s1, Object s2) {
            return ((String) s1).compareTo((String) s2);
        }
    }

    /**
     * Insert new abbreviation in the table Add new row
     */
    public int adaugaRow() {
        
        int op = JOptionPane.showConfirmDialog(null, fields, "Add new abbreviation", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            return 1;
        }
        return 0;
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
    
    public String getSearchedWord() {
        return field_search.getText();
    }
}
