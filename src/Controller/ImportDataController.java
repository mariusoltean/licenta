/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.AbbreviationsEditingModel;
import Model.ImportDataModel;
import Util.AnnotationData;
import View.AbbreviationsEditingFrame;
import View.ImportDataFrame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Created by osboxes on 31/01/16.
 */
public class ImportDataController {

    private final ImportDataFrame impDataFrame;
    private final ImportDataModel impDataModel;

    public ImportDataController(ImportDataFrame impDataFrame, ImportDataModel impDataModel) {
        this.impDataFrame = impDataFrame;
        this.impDataModel = impDataModel;
        this.impDataFrame.addListenerButtonBack(new ButtonBackListener());
        this.impDataFrame.addListenerButtonImportTextRows(new ButtonImportRowTextListener());
        this.impDataFrame.addListenerButtonAnnotations(new ButtonAnnotationsListener());
        this.impDataFrame.addListenerButtonAbbrevEditing(new ButtonOpenAbbrevEditing());
        this.impDataFrame.addListenerButtonReplaceShortcuts(new ButtonReplaceShortcuts());
        this.impDataFrame.addListenerButtonSave(new ButtonSaveData());
    }

    class ButtonBackListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                impDataFrame.setVisible(false);
            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ButtonImportRowTextListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int returnVal = impDataFrame.getFileDialog();
                if (returnVal == 0) {
                    File f = impDataFrame.getSelectedFile();
                    Afisare(f, impDataFrame.getTableModel());
                }
            } catch (IllegalArgumentException e1) {
            }
        }

        private void Afisare(File f, DefaultTableModel table) {
            try {
                List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.ISO_8859_1);
                table.setRowCount(0);
                lines.remove(0);
                for (String line : lines) {
                    String[] array = line.split(",");
                    if (array[0].contains("\"")) {
                        array[0] = array[0].replace("\"", "");
                    }
                    Object o = array[0];
                    table.addRow(new Object[]{array[0]});
                }
            } catch (Exception ex) {
                Logger.getLogger(ImportDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class ButtonAnnotationsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String column_name = addColumn();// = impDataFrame.adaugaColoana();
                if(column_name.equals("no_column")) {           //daca utilizatorul a dat "cancel" in loc sa adauge coloana
                    return;                                     //atunci nu executam nimic, nu adaugam coloana
                }
                DefaultTableModel obs_table = impDataFrame.getTableModel();
                String obs;
                AnnotationData data = new AnnotationData();
                data.readRules(column_name);
                HashMap<String, String> rules_map = data.getRulesMap();
                Collection values_col = rules_map.values();
                List<String> values_list = new ArrayList<String>(values_col);
                Set<String> key_set = rules_map.keySet();
                List<String> key_list = new ArrayList<String>(key_set);
                boolean completed = false;
                int last_column = obs_table.getColumnCount()-1;
                for (int i = 0; i < obs_table.getRowCount(); i++) {
                    completed = false;
                    obs = (String) obs_table.getValueAt(i, 1);
                    for (int j = 0; j < key_list.size(); j++) {
                        if (containsCheck(obs, key_list.get(j))) {
                            obs_table.setValueAt(values_list.get(j), i, last_column);
                            completed = true;
                        }
                    }
                    if (!completed && obs_table.getValueAt(i, last_column) == null) {
                        obs_table.setValueAt("N", i, last_column);
                    }
                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ButtonOpenAbbrevEditing implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                final Dimension prefferedSize = new Dimension(1200, 1000);
                AbbreviationsEditingFrame editAbrevieri = new AbbreviationsEditingFrame(prefferedSize);
                editAbrevieri.setVisible(true);
                AbbreviationsEditingModel abrevModel = new AbbreviationsEditingModel();
                AbbreviationsEditingController abbrevEditController = new AbbreviationsEditingController(editAbrevieri, abrevModel);
            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ButtonSaveData implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                /*FileWriter writer = new FileWriter("ReplaceShortcuts.csv");
                DefaultTableModel table = impDataFrame.getTableModel();
                List<String> numdata = new ArrayList<String>();
                List<String> lista = new ArrayList<String>();
                for (int count = 0; count < table.getRowCount(); count++) {
                    numdata.add(table.getValueAt(count, 1).toString());
                }
                for (String s : numdata) {
                    if (s.contains(";")) {
                        s = s.replace(";", "");
                    }
                    lista.add(s);
                }
                for (String s : lista) {
                    writer.append(s);
                    writer.append('\n');
                }
                writer.flush();
                writer.close();*/

                DefaultTableModel tableModel = (DefaultTableModel) impDataFrame.getTableModel();
                impDataModel.saveToFile(tableModel);
                //impDataFrame.getTable().getRowSorter().allRowsChanged();
                JOptionPane.showMessageDialog(null, "Saved successfully !");
            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ButtonReplaceShortcuts implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DefaultTableModel table = impDataFrame.getTableModel();
                List<String> lista_observatii = new ArrayList<String>();
                for (int count = 0; count < table.getRowCount(); count++) {
                    lista_observatii.add(table.getValueAt(count, 0).toString()); //lista cu observatiile din tabel - coloana cu prescurtarile
                }
                List<String> date_tabel_initiale = new ArrayList<String>(lista_observatii); //creez o noua lista pentru a salva o copie a datelor-prima coloana
                File f = new File("Abrevieri.csv");
                List<String> lista_abrevieri = Files.readAllLines(f.toPath(), StandardCharsets.ISO_8859_1);//lista cu abrevierile
                Object[] date_finale = new Object[2];   //randul care o sa se insereaza la fiecare pas

                for (String line : lista_abrevieri) {
                    if (line.contains("-")) {
                        replaceShortcuts(line, lista_observatii, date_tabel_initiale);
                    }
                }
                for (String line : lista_abrevieri) {
                    replaceShortcuts(line, lista_observatii, date_tabel_initiale);
                }
            } catch (IllegalArgumentException | IOException e1) {
            }
        }
    }

    public void replaceShortcuts(String line, List<String> lista_observatii, List<String> date_init) {
        int i = 0;
        String[] date_finale = new String[2];
        List<String> date_tabel_initiale = new ArrayList<String>(date_init); //creez o noua lista pentru a salva o copie a datelor-prima coloana
        DefaultTableModel table = impDataFrame.getTableModel();

        String[] parts = line.split(","); //impart lista cu abrevieri dupa virgula-split
        ListIterator it = lista_observatii.listIterator();
        while (it.hasNext()) {
            String s = (String) it.next();
            date_finale[0] = date_tabel_initiale.get(i); //pastrez in stringul din stanga coloana nemodificata

            if (s.contains("staza gastr ")) {
                s = s.replace("staza gastr", "staza gastrica");
            }
            if (s.contains(parts[0])) {
                if (s.contains("+")) {
                    s = s.replace("+", " si ");
                }
                String regex = "\\b" + parts[0] + "\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    date_finale[1] = matcher.replaceAll(parts[1]);

                    if (date_finale[1].contains("-")) {
                        date_finale[1] = date_finale[1].replace("-", " ");
                    }
                    table.removeRow(i); // sterg randul vechi
                    table.insertRow(i, date_finale); //adaug noul rand procesat
                }
            } else {
                if (table.getValueAt(i, 1) == null) {
                    date_finale[1] = s;
                    table.removeRow(i);
                    table.insertRow(i, date_finale);
                }
            }
            i++;
        }
        lista_observatii.removeAll(lista_observatii);
        for (int count = 0; count < table.getRowCount(); count++) {
            try {
                Object o = table.getValueAt(count, 1);
                if (o != null) {
                    lista_observatii.add(o.toString());
                } else {
                    lista_observatii.add(table.getValueAt(count, 0).toString());
                }
            } catch (Exception e) {
            }
        }
    }
    
    public String addColumn() {
        String column_name = adaugaColoana();
        for (int i = 2; i < impDataFrame.getTable().getColumnCount(); i++) {
            impDataFrame.getTable().getColumnModel().getColumn(i).setMaxWidth(150);
        }
        return column_name;
    }
    
    
    protected String adaugaColoana() {
        int valueColoana = impDataFrame.selectColumn();
        System.out.println("ValueColoana" + valueColoana);
        DefaultTableModel tm = impDataFrame.getTableModel();
        if (valueColoana == -1) {
            return "no_column";         //daca utilizatorul da "cancel"
        } else if (valueColoana == 0) {
            tm.addColumn("S-Lum");
            return "S-Lum";
        } else if (valueColoana == 1) {
            tm.addColumn("S-Cont");
            return "S-Cont";
        } else if (valueColoana == 2) {
            tm.addColumn("S-MucANT");
            return "S-MucANT";
        } else if (valueColoana == 3) {
            tm.addColumn("S-MucCORP");
            return "S-MucCORP";
        } else if (valueColoana == 4) {
            tm.addColumn("S-MucANT");
            return "S-MucANT";
        } else if (valueColoana == 5) {
            tm.addColumn("SlezANT");
            return "SlezANT";
        } else if (valueColoana == 6) {
            tm.addColumn("S-LezJAC");
            return "S-LezJAC";
        } else if (valueColoana == 7) {
            tm.addColumn("S-LezCORP");
            return "S-LezCORP";
        } else if (valueColoana == 8) {
            tm.addColumn("Pilor");
            return "Pilor";
        } else {
            return "no_column";
        }

    }
    
    
    public boolean containsCheck(String obs, String rule) {
        boolean ok = true;
        
        String[] parts = rule.split(" ");
        for(String s: parts) {
            if(!obs.contains(s))
                ok = false;
        }
        return ok;
    }
}
