/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.CasesForRulesModel;
import Model.CompareRulesModel;
import Model.ValidationRulesModel;
import Util.*;
import View.CasesForRulesFrame;
import View.CompareRulesFrame;
import View.ValidationRulesFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Created by osboxes on 31/01/16.
 */
public class ValidationRulesController {

    //TODO Check for already saved rules "licenta/reguli" whenever the user strikes generate rules button
    //TODO when saving a rule the application will generate new rules for the set of examples which do not mach with the saved rule
    private String possitioning = "";
    private final ValidationRulesFrame rulesFrame;
    private final ValidationRulesModel model;
    private int flag;
    private int countIteration = 1;
    private final String stopWordsFile = possitioning + "stopWords.txt";

    public ValidationRulesController(ValidationRulesFrame rulesFrame,
                                     ValidationRulesModel model) {
        possitioning = FolderUtil.getCurrentPossition();
        this.model = model;
        this.rulesFrame = rulesFrame;
        this.rulesFrame.addActionListenerButtonGetColumnValues(new ButtonListenerGetColumnValues());
        this.rulesFrame.addActionListenerComboColumnValues(new ComboListenerGetColumnValues());
        this.rulesFrame.addActionListenerButtonBack(new ButtonBackActionListener());
        this.rulesFrame.addActionListenerButtonRemoveRules(new ButtonRemoveRulesListener());
        this.rulesFrame.addActionListnerButtonRunApriori(new ButtonRunAprioriListener());
        this.rulesFrame.addItemListenerComboBoxChanged(new ItemChangeListener());
        this.rulesFrame.addActionListenerButtonSaveRules(new ButtonSaveRulesListener());
        this.rulesFrame.addTableActionListener(new TableRowSelectedListener());
        this.rulesFrame.addActionListenerButtonSeeRules(new ButtonSeeRulesListener());
    }

    class ButtonListenerGetColumnValues implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String numeColoana = rulesFrame.getSelectedItemComboBoxName();

                MedicalTermProcessing medProcessing = new MedicalTermProcessing();
                medProcessing.extractValuesFromColumn(numeColoana);
                ArrayList<String> columnValues = medProcessing.getValuesColumn(numeColoana);
                rulesFrame.setComboBoxValues(columnValues);

            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ComboListenerGetColumnValues implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String numeColoana = rulesFrame.getSelectedItemComboBoxName();

                MedicalTermProcessing medProcessing = new MedicalTermProcessing();
                medProcessing.extractValuesFromColumn(numeColoana);
                ArrayList<String> columnValues = medProcessing.getValuesColumn(numeColoana);
                rulesFrame.setComboBoxValues(columnValues);

            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ButtonBackActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
                System.out.println("ValidationRUlesController 119");
        }
    }

    class ButtonShowRulesListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //rulesFrame.populareTable();
                populareTabel();
            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ButtonRunAprioriListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                GenerateRules generateRules = new GenerateRules();
                String numeColoana = rulesFrame.getSelectedItemComboBoxName();
                String valoareColoana = rulesFrame.getSelectedItemComboBoxValue();
                if (valoareColoana != null) {
                    ArrayList<String> flagsApriori = new ArrayList<String>();
                    flagsApriori = rulesFrame.setAprioriParameters("");

                    if (!flagsApriori.isEmpty()) {
                        if (flag == 0) {
                            generateRules.runApriori(flagsApriori.get(0), flagsApriori.get(1), numeColoana, valoareColoana);
                        } else {
                            generateRules.runAprioriScript2(flagsApriori.get(0), flagsApriori.get(1), numeColoana, valoareColoana);
                        }
                        populareTabel();
                    }
                }
            } catch (IllegalArgumentException e1) {
            }

        }
    }

    class ButtonRemoveRulesListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //TODO WRITE CODE HERE
                countIteration++;
                GenerateRules generateRules = new GenerateRules();
                String numeColoana = rulesFrame.getSelectedItemComboBoxName();
                String valoareColoana = rulesFrame.getSelectedItemComboBoxValue();
                removeRulesFromFile(numeColoana, valoareColoana);
                removeRulesFromTable();
                model.saveRulesToFile(numeColoana, getRulesToSave());
                ArrayList<String> flagsApriori = new ArrayList<String>();
                flagsApriori = rulesFrame.setAprioriParameters("Apriori parameters rule-set #" + countIteration);

                if (!flagsApriori.isEmpty()) {
                    generateRules.runAprioriScript2(flagsApriori.get(0), flagsApriori.get(1), numeColoana, valoareColoana);
                }

                //rulesFrame.populareTable();

                populareTabel();
                flag = 1;
            } catch (Exception e1) {
            }
        }
    }

    class TableRowSelectedListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting() == false) {
                if (rulesFrame.getTable().getSelectedColumn() == 0) {
                    try {

                        String key = rulesFrame.getTable().getValueAt(rulesFrame.getTable().getSelectedRow(), 0).toString().trim();
                        final Dimension prefferedSize = new Dimension(1200, 1000);
                        CasesForRulesFrame casesForRules = new CasesForRulesFrame(prefferedSize);
                        casesForRules.setVisible(true);
                        CasesForRulesModel model = new CasesForRulesModel(key);
                        CasesForRulesController rulesController = new CasesForRulesController(
                                casesForRules, model);

                    } catch (IllegalArgumentException e1) {
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    class ButtonSaveRulesListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int clear = JOptionPane.showConfirmDialog(null, "Flush existing ruleset ?", "Flush ruleset", JOptionPane.YES_NO_OPTION);
                String stomach_part = rulesFrame.getSelectedItemComboBoxName();
                String nameFile = "../Licenta/reguli/" + stomach_part + "_rulesOut.out";
                FileWriter fileWriter; // new FileWriter(nameFile, false);
                if (clear == JOptionPane.YES_OPTION) {
                    fileWriter = new FileWriter(nameFile, false);
                    BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
                    bufferWritter.write("");
                    bufferWritter.close();
                }

                System.out.println("Flushing completed");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    class ButtonSeeRulesListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTableModel tableModel = rulesFrame.getTableModel();
            ArrayList<String> list_rulesTrue = new ArrayList<>();
            int rows = tableModel.getRowCount();

            String randCurent, checked;
            String[] aux;
            for (int i = 0; i < rows; i++) {
                randCurent = tableModel.getValueAt(i, 0).toString();
                checked = tableModel.getValueAt(i, 2).toString();
                if (checked.equals("true")) {
                    list_rulesTrue.add(randCurent);
                }
            }
            if (list_rulesTrue.size() != 2) {
                //Should produce error message
                JOptionPane.showMessageDialog(null, " Please select 2 rows for comparison ! ", "Error !", JOptionPane.ERROR_MESSAGE);
            } else {
                //Should display new frame
                final Dimension prefferedSize = new Dimension(1700, 1000);
                CompareRulesFrame compRF = new CompareRulesFrame(prefferedSize);
                compRF.setVisible(true);
                CompareRulesModel compM = null;
                try {
                    compM = new CompareRulesModel(list_rulesTrue);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                CompareRulesController compR = new CompareRulesController(compRF, compM);
//                CasesForRulesModel model = new CasesForRulesModel(key);
//                CasesForRulesController rulesController = new CasesForRulesController(
//                        casesForRules, model);


            }
        }
    }

    public void removeRulesFromFile(String numeColoana, String valoareColoana) {
        try {
            String nameFile = "../Licenta/apriori/Data." + numeColoana + "_" + valoareColoana + ".in";
            BufferedReader reader = new BufferedReader(new FileReader(nameFile));
            ArrayList<String> list_dataIn = new ArrayList<>();
            ArrayList<String> lista_rules = new ArrayList<>();

            String line = null;
            while ((line = reader.readLine()) != null) {
                list_dataIn.add(line);
            }
            lista_rules = this.getRulesTrue();

            Iterator<String> itr = list_dataIn.iterator();
            while (itr.hasNext()) {
                String rand_urm = itr.next();
                for (String rule : lista_rules) {
                    if (containsCheck(rand_urm, rule)) {
                        itr.remove();
                        break;
                    }
                }
            }
            PrintWriter writer = new PrintWriter(nameFile, "UTF-8");
            ArrayList<String> listaFinala = new ArrayList<>();
            for (int i = 0; i < list_dataIn.size(); i++) {
                writer.println(list_dataIn.get(i));
            }

            writer.close();
            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(ValidationRulesController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void removeRulesFromTable() {
        try {
            String nameFile = "../Licenta/apriori/aprioriRules.out";
            BufferedReader reader = new BufferedReader(new FileReader(nameFile));
            ArrayList<String> list_dataIn = new ArrayList<>();
            ArrayList<String> lista_rules = new ArrayList<>();

            String line = null;
            while ((line = reader.readLine()) != null) {
                list_dataIn.add(line);
            }
            lista_rules = this.getRulesForDelete();

            Iterator<String> itr = list_dataIn.iterator();
            while (itr.hasNext()) {
                String rand_urm = itr.next();
                for (int j = 0; j < lista_rules.size(); j++) {
                    if ((rand_urm).contains(lista_rules.get(j))) {
                        itr.remove();
                        break;
                    }
                }
            }
            PrintWriter writer = new PrintWriter(nameFile, "UTF-8");
            ArrayList<String> listaFinala = new ArrayList<>();
            for (int i = 0; i < list_dataIn.size(); i++) {
                writer.println(list_dataIn.get(i));
            }

            writer.close();
            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(ValidationRulesController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return o lista cu regulile pe care le selecteaza user-ul si vrea sa le
     * elimine
     */
    public ArrayList<String> getRulesTrue() {

        DefaultTableModel tableModel = rulesFrame.getTableModel();
        JTable table = rulesFrame.getTable();
        ArrayList<String> list_rulesTrue = new ArrayList<>();
        int rows = tableModel.getRowCount();

        String randCurent, checked;
        String[] aux;
        for (int i = 0; i < rows; i++) {
            randCurent = "";
            randCurent = tableModel.getValueAt(i, 0).toString();
            checked = tableModel.getValueAt(i, 2).toString();
            if (checked.equals("true")) {
                aux = randCurent.split("<- ");
                list_rulesTrue.add(aux[1]);
            }
        }
        return list_rulesTrue;
    }

    public ArrayList<String> getRulesToSave() {

        DefaultTableModel tableModel = rulesFrame.getTableModel();
        JTable table = rulesFrame.getTable();
        ArrayList<String> list_rulesTrue = new ArrayList<>();
        int rows = tableModel.getRowCount();

        String randCurent, checked;
        String[] aux;
        for (int i = 0; i < rows; i++) {
            randCurent = "";
            randCurent = tableModel.getValueAt(i, 0).toString();
            checked = tableModel.getValueAt(i, 2).toString();
            if (checked.equals("true")) {
                list_rulesTrue.add(randCurent.trim().replace("*", ""));
            }
        }
        return list_rulesTrue;
    }

    public ArrayList<String> getRulesForDelete() {

        DefaultTableModel tableModel = rulesFrame.getTableModel();
        JTable table = rulesFrame.getTable();
        ArrayList<String> lista_rulesTrue = new ArrayList<>();

        int rows = table.getRowCount();
        String randCurent, checked;
        for (int i = 0; i < rows; i++) {
            randCurent = "";
            randCurent = tableModel.getValueAt(i, 0).toString();
            checked = tableModel.getValueAt(i, 2).toString();
            if (checked.equals("true")) {
                lista_rulesTrue.add(randCurent + " (");
            }

        }
        return lista_rulesTrue;

    }

    class ItemChangeListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                flag = 0;
            }
        }
    }


    public void populareTabel() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(stopWordsFile));
            ArrayList<String> stopWordsList = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stopWordsList.add(line);
            }
            HashMap<ArrayList<String>, ArrayList<String>> map;
            //HashMap<ArrayList<String>, ArrayList<String>> result= new HashMap();
            List<String> sortKeys = new ArrayList<>();
            List<String> sortValues = new ArrayList<>();
            map = model.readFile();
            DefaultTableModel tableModel = rulesFrame.getTableModel();
            tableModel.setRowCount(0);

            for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                ArrayList<String> lista_key = (ArrayList<String>) entry.getKey();
                ArrayList<String> lista_values = (ArrayList<String>) entry.getValue();
                boolean flagForStopWord;
                boolean flagForAddition;
                boolean firstNonStopWord;
                for (int i = 0; i < lista_key.size(); i++) {
                    firstNonStopWord = true;
                    flagForAddition = true;
                    List<String> splitWords = new ArrayList<>();
                    try{
                        splitWords = Arrays.asList(lista_key.get(i).split("<- ")[1].split(" "));
                    } catch (ArrayIndexOutOfBoundsException e){
                        System.out.println(lista_key + " was skipped!");
                    }

                    for (String word : splitWords) {
                        flagForStopWord = true;
                        for (String stopWord : stopWordsList) {
                            if (word.equalsIgnoreCase(stopWord)) {
                                flagForStopWord = false;
                                break;
                            }
                        }
                        if (flagForStopWord == true) {
                            if (firstNonStopWord == true) {
                                if (splitWords.size() == 1) {
                                    flagForAddition = false;
                                    break;
                                }
                                firstNonStopWord = false;
                            } else {
                                flagForAddition = false;
                                break;
                            }
                        }

                    }
                    if (flagForAddition == false) {
                        sortKeys.add(lista_key.get(i));
                        sortValues.add(lista_values.get(i));
                        //tableModel.addRow(new Object[]{lista_key.get(i), lista_values.get(i), false});

                    }

                }
            }
            int result = GroupRules.groupThemTogether(sortKeys, sortValues);
            if (result != -1) {
                List<String> keyFromGroupingStage = GroupRules.getKeys();
                List<String> valuesFromGroupingStage = GroupRules.getValues();
                int position = GroupRules.dominantPossition(keyFromGroupingStage);

                //add here

                tableModel.addRow(new Object[]{keyFromGroupingStage.get(position).replace("*", ""), valuesFromGroupingStage.get(position), true});
                for (int i = 0; i < keyFromGroupingStage.size(); i++) {
                    if (position != i) {
                        tableModel.addRow(new Object[]{"        " + keyFromGroupingStage.get(i), valuesFromGroupingStage.get(i), false});
                        if (keyFromGroupingStage.get(i).contains("*")) {
                            keyFromGroupingStage.set(i, keyFromGroupingStage.get(i).replace("*", ""));
                        }
                    }

                    sortKeys.removeAll(keyFromGroupingStage);
                    sortValues.removeAll(valuesFromGroupingStage);
                }
                for (int i = 0; i < sortKeys.size(); i++) {
                    tableModel.addRow(new Object[]{sortKeys.get(i), sortValues.get(i), false});
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean containsCheck(String obs, String rule) {
        boolean ok = true;

        String[] parts = rule.split(" ");
        for (String s : parts) {
            if (!obs.contains(s))
                ok = false;
        }

        return ok;
    }

}
