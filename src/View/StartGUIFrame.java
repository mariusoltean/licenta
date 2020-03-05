/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Util.FolderUtil;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.BevelBorder;


/**
 * Created by osboxes on 11/05/16.
 */
public class StartGUIFrame extends JFrame {

    private final JPanel panel_start = new JPanel();
    private final JButton button_importData = new JButton("Annotate Data");
    private final JButton button_abbrevEditing = new JButton("Abbreviations Editing");
    private final JButton button_validationRules = new JButton("Generate Annotation Rules");
    private final JButton button_Exit = new JButton("Exit");

    /**
     * Constructorul clasei
     *
     * @param prefferedSize - dimensiunea frame-ului
     */
    public StartGUIFrame(Dimension prefferedSize) {
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
        setPreferredSize(prefferedSize);
        setVisible(false);
        setTitle("Medical analysis");
        init();
    }

    private void init() {

        button_importData.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        panel_start.setLayout(new GridLayout(9, 3, 0, 0));
        //panel_start.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        for (int i = 0; i < 4; i++) {
            panel_start.add(new JLabel());
        }
        panel_start.add(button_importData);

        for (int i = 0; i < 5; i++) {
            panel_start.add(new JLabel());
        }
        panel_start.add(button_validationRules);
        for (int i = 0; i < 5; i++) {
            panel_start.add(new JLabel());
        }
        panel_start.add(button_abbrevEditing);
        for (int i = 0; i < 5; i++) {
            panel_start.add(new JLabel());
        }
        panel_start.add(button_Exit);
        for (int i = 0; i < 4; i++) {
            panel_start.add(new JLabel());
        }
        setContentPane(panel_start);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Listener pentru butonul button_abbrevEditing
     *
     * @param a
     */
    public void addListenerButtonAbbrevEditing(ActionListener a) {
        button_abbrevEditing.addActionListener(a);
    }

    /**
     * Listener pentru butonul button_Exit
     *
     * @param a
     */
    public void addListenerButtonExit(ActionListener a) {
        button_Exit.addActionListener(a);
    }

    /**
     * Listener pentru butonul button_importData
     *
     * @param a
     */
    public void addListenerButtonImportData(ActionListener a) {
        button_importData.addActionListener(a);
    }

    /**
     * Listener pentru buttonul button_validationRules
     *
     * @param a
     */
    public void addListenerButtonValidationRules(ActionListener a) {
        button_validationRules.addActionListener(a);
    }
}
