/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Util.FolderUtil;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


/**
 * Created by osboxes on 11/05/16.
 */
public class ImportDataFrame extends JFrame {

    /**
     * JButtons *
     */
    private final JPanel panel_importData_2 = new JPanel();
    private final JPanel panel_importData_1 = new JPanel();
    private final JButton button_importTextRows = new JButton("Import annotation data ");
    private final JButton button_replaceShortcuts = new JButton("Replace Shortcuts");
    private final JButton button_annotations = new JButton("Annotate");
    private final JButton button_back = new JButton("Back");
    private final JButton button_abbrevEditing = new JButton("Abbreviations Editing");
    private final JButton button_save = new JButton("Save");
    /**
     * JSplitPane *
     */
    private JSplitPane splitPane;
    /**
     * JTable *
     */
    private JTable table;
    /**
     * DefaultTableModel *
     */
    private DefaultTableModel tableModel;
    Object[] columnNames = {"Observations", "Complete observations"};

    /**
     * JScrollPane *
     */
    private JScrollPane scrollPanel;
    /**
     * JFileChooser *
     */
    private final JFileChooser fileChooser = new JFileChooser();

    public ImportDataFrame() {
    }

    public ImportDataFrame(Dimension prefferedSize) {
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

        JPanel panel_impData_1_1 = new JPanel(new GridLayout(4, 3, 60, 0));
        for (int i = 0; i < 6; i++) {
            panel_impData_1_1.add(new JLabel());
        }
        panel_impData_1_1.add(button_importTextRows);
        panel_impData_1_1.add(button_replaceShortcuts);
        panel_impData_1_1.add(button_annotations);
        for (int i = 0; i < 3; i++) {
            panel_impData_1_1.add(new JLabel());
        }
        panel_importData_1.add(panel_impData_1_1, BorderLayout.CENTER);

        panel_importData_2.setLayout(new BorderLayout());
        JPanel panel_impData_2_1 = new JPanel(new BorderLayout());
        JPanel panel_impData_2_2 = new JPanel(new GridLayout(3, 3, 50, 0));
        for (int i = 0; i < 3; i++) {
            panel_impData_2_2.add(new JLabel());
        }
        panel_impData_2_2.add(button_back);
        panel_impData_2_2.add(button_abbrevEditing);
        panel_impData_2_2.add(button_save);
        for (int i = 0; i < 3; i++) {
            panel_impData_2_2.add(new JLabel());
        }

        tableModel = new DefaultTableModel();
        tableModel.setDataVector(new Object[][]{}, columnNames);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        scrollPanel = new JScrollPane(table);

        JTableHeader anHeader = table.getTableHeader();
        anHeader.setForeground(Color.BLUE);
        anHeader.setFont(new Font("Serif", Font.PLAIN, 20));
        table.setFont(new Font("Serif", Font.PLAIN, 19));
        table.setRowHeight(20);

        panel_impData_2_1.add(scrollPanel);
        panel_importData_2.add(panel_impData_2_1, BorderLayout.CENTER);
        panel_importData_2.add(panel_impData_2_2, BorderLayout.SOUTH);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(panel_importData_1);
        splitPane.setBottomComponent(panel_importData_2);
        setContentPane(splitPane);

        pack();
        splitPane.setDividerLocation(0.2);
        setLocationRelativeTo(null);
        fileChooser.setCurrentDirectory(new File("."));
    }

    /**
     * Listener pentru butoane
     *
     * @param a
     */
    public void addListenerButtonImportTextRows(ActionListener a) {
        button_importTextRows.addActionListener(a);
    }

    public void addListenerButtonReplaceShortcuts(ActionListener a) {
        button_replaceShortcuts.addActionListener(a);
    }

    public void addListenerButtonAnnotations(ActionListener a) {
        button_annotations.addActionListener(a);
    }

    public void addListenerButtonBack(ActionListener a) {
        button_back.addActionListener(a);
    }

    public void addListenerButtonAbbrevEditing(ActionListener a) {
        button_abbrevEditing.addActionListener(a);
    }

    public void addListenerButtonSave(ActionListener a) {
        button_save.addActionListener(a);
    }

    public void adaugaListenerFileChooser(ActionListener a) {
        fileChooser.addActionListener(a);
    }

    /**
     * Show open dialog
     *
     * @return
     */
    public int getFileDialog() {
        return fileChooser.showOpenDialog(this);
    }

    public File getSelectedFile() {
        return fileChooser.getSelectedFile();
    }

    /**
     * Select the column that i want to add to table
     *
     * @return
     */
    public int selectColumn() {
        JFrame frame_selectColumn = new JFrame();
        String[] tip_coloana = {"S-Lum", "S-Cont", "S-MucANT", "S-MucJAC", "S-MucCORP",
        "SlezANT", "S-LezJAC", "S-LezCORP", "Pilor"};
        JComboBox jBox_tipColoana = new JComboBox(tip_coloana);
        frame_selectColumn.add(jBox_tipColoana);
        jBox_tipColoana.setEditable(true);

        int op = JOptionPane.showConfirmDialog(null, jBox_tipColoana, "select or type a value", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            return jBox_tipColoana.getSelectedIndex();
        } else {
            return -1;
        }
    }

    /**
     * Return table to controller
     *
     * @return
     */
    public DefaultTableModel getTableModel() {
        return this.tableModel;
    }
    
    public JTable getTable() {
        return this.table;
    }

}
