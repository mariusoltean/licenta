package Controller;

import Model.CasesForRulesModel;
import View.CasesForRulesFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by osboxes on 31/03/16.
 */
public class CasesForRulesController {

    private String possitioning = "";
    private final CasesForRulesFrame rulesFrame;
    private final CasesForRulesModel model;
    private int flag;
    private final String stopWordsFile=possitioning+"stopWords.txt";
    public CasesForRulesController(CasesForRulesFrame rulesFrame,
                                     CasesForRulesModel model) {
        possitioning=" "+getCurrentPossition();
        this.model = model;
        this.rulesFrame = rulesFrame;
        this.rulesFrame.addActionListenerButtonBack(new ButtonBackActionListener());
        this.showRules();
    }
    private static String possitioningCommand="pwd>enterPath.txt";
    private static String pathFile="enterPath.txt";


    public static String getCurrentPossition() {
        Process p1 = null;
        try {
            p1 = Runtime.getRuntime().exec(new String[]{"sh","-c",possitioningCommand});
            p1.waitFor();
            BufferedReader reader = new BufferedReader(new FileReader(pathFile));
            return reader.readLine()+"/";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    class ButtonBackActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                rulesFrame.setVisible(false);
            } catch (IllegalArgumentException e1) {
            }
        }
    }
    /**
     * Populate abbreviations table
     */
    private void showRules() {
        List<String> rulles = model.getCasesForRulesFromFile();
        for (String line : rulles) {
            String[] array = line.split(":");
            Object o = array[0];
            rulesFrame.getTableModel().addRow(new Object[]{array[0], array[1]});
        }
    }
}
