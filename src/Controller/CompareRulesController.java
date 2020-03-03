package Controller;

import Model.CompareRulesModel;
import View.CompareRulesFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by osboxes on 12/04/16.
 */
public class CompareRulesController {
    private String possitioning = "";
    private final CompareRulesFrame rulesFrame;
    private final CompareRulesModel model;
    private int flag;
    private final String stopWordsFile=possitioning+"stopWords.txt";
    public CompareRulesController(CompareRulesFrame rulesFrame,
                                  CompareRulesModel model) {
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
        List<String> rullesAmB = model.getAmBDifference();
        if (rullesAmB.size() == 0) {
            rulesFrame.getTableModelAmB().addRow(new Object[]{"No differences"});
        }else{
            for (final String line : rullesAmB) {
                rulesFrame.getTableModelAmB().addRow(new Object[]{line});
            }

        }

        List<String> rullesBmA = model.getBmADifference();
        if (rullesBmA.size() == 0) {
            rulesFrame.getTableModelBmA().addRow(new Object[]{"No differences"});
        }else{
            for (final String line : rullesBmA) {
                rulesFrame.getTableModelBmA().addRow(new Object[]{line});
            }

        }
        List<String> rullesAiB = model.getAiB();
        if (rullesAiB.size() == 0) {
            rulesFrame.getTableModelAiB().addRow(new Object[]{"No intersection"});
        }else{
            for (final String line : rullesAiB) {
                rulesFrame.getTableModelAiB().addRow(new Object[]{line});
            }

        }
    }
}
