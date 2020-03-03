/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.AbbreviationsEditingModel;
import Util.DictionaryUtil;
import View.AbbreviationsEditingFrame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Created by osboxes on 31/03/16.
 */
public class AbbreviationsEditingController {

    private final AbbreviationsEditingFrame abbrevEditFrame;
    private final AbbreviationsEditingModel model;

    public AbbreviationsEditingController(AbbreviationsEditingFrame abbrevEditFrame,
            AbbreviationsEditingModel model) {

        this.model = model;
        this.abbrevEditFrame = abbrevEditFrame;
        this.abbrevEditFrame.addActionListenerButtonBack(new ButtonBackActionListener());
        this.abbrevEditFrame.addActionListenerButtonAddRow(new ButtonAddRowListener());
        this.abbrevEditFrame.addActionListenerButtonDelete(new ButtonDeleteRowListener());
        this.abbrevEditFrame.addActionListenerButtonSave(new ButtonSaveListener());
        this.abbrevEditFrame.addActionListenerButtonSearch(new ButtonSearchWord());
        this.abbrevEditFrame.addActionListenerButtonFindInDictionary(new ButtonFindInDictionary());
        this.showAbbreviations();
    }

    class ButtonSearchWord implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                TableModel tm = abbrevEditFrame.getTableModel();
                JTable table = abbrevEditFrame.getTable();
                String searchedAbbrev = abbrevEditFrame.getSearchedWord();
                for(int i=0; i<tm.getRowCount(); i++) {
                    if (tm.getValueAt(i, 0).equals(searchedAbbrev)) {
                        table.getSelectionModel().setSelectionInterval(i, i);
                        Rectangle r = table.getCellRect(i, 0, true);
                        table.scrollRectToVisible(r);
                        break;
                    } 
                }
            } catch (IllegalArgumentException e1) {
            }
        }
    }
    //ButtonFindInDictionary

    class ButtonFindInDictionary implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                String textToBeSearched=abbrevEditFrame.getSearchedWord().toUpperCase();
                DictionaryUtil dictionaryService=new DictionaryUtil();
                boolean result=dictionaryService.existsInDictionary(textToBeSearched);
                if (result==true){
                    JOptionPane.showMessageDialog(null, "Word "+abbrevEditFrame.getSearchedWord()+" exists in dictionary");
                    //JOptionPane.showMessageDialog(null, "Word "+abbrevEditFrame.getSearchedWord()+" exists in dictionary", "Success !", JOptionPane.OK_OPTION);
                }else{
                    JOptionPane.showMessageDialog(null, "Word "+abbrevEditFrame.getSearchedWord()+" does not exist in dictionary", "Error !", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e1) {
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    class ButtonBackActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                abbrevEditFrame.setVisible(false);
            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ButtonAddRowListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                addRow();
            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ButtonDeleteRowListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                removeRow();
            } catch (IllegalArgumentException e1) {
            }
        }
    }

    /**
     * Listener pentru butonul save
     */
    class ButtonSaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DefaultTableModel tableModel = (DefaultTableModel) abbrevEditFrame.getTableModel();
                model.saveToFile(tableModel);
                abbrevEditFrame.getTable().getRowSorter().allRowsChanged();
                JOptionPane.showMessageDialog(null, "Saved successfully !");

            } catch (IllegalArgumentException e1) {
            }
        }

    }

    /**
     * Populate abbreviations table
     */
    private void showAbbreviations() {
        List<String> abbrev = model.getAbbreviationsFromFile();
        for (String line : abbrev) {
            String[] array = line.split(",");
            Object o = array[0];
            abbrevEditFrame.getTableModel().addRow(new Object[]{array[0], array[1]});
        }
    }

    public void addRow() {
        int selection = abbrevEditFrame.adaugaRow();
        if (selection == 1) {
            String signifficanceText=abbrevEditFrame.getText_sign();
            DictionaryUtil dictionaryService=new DictionaryUtil();
            boolean result=false;
            try {
                result=dictionaryService.existsInDictionary(signifficanceText.toUpperCase());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (result==true){
                //The word exists in dictionary
                addAbreviation();
            }else{
                int op =JOptionPane.showConfirmDialog(null, "The significance for the abbreviation does not exist in dictionary. Would you like to proceed adding the abbreviation?", "Warning", JOptionPane.WARNING_MESSAGE);
                if (op == JOptionPane.OK_OPTION) {
                    addAbreviation();
                    dictionaryService.addNewWord(signifficanceText.toUpperCase());
                }
            }
        }
    }
    public void addAbreviation(){
        abbrevEditFrame.getTableModel().getDataVector().removeAllElements();
        List<String> abbrev = model.getAbbreviationsFromFile();
        abbrev.add(abbrevEditFrame.getText_abbrev() + "," + abbrevEditFrame.getText_sign());
        Collections.sort(abbrev, String.CASE_INSENSITIVE_ORDER);
        for (String line : abbrev) {
            String[] array = line.split(",");
            Object o = array[0];
            abbrevEditFrame.getTableModel().addRow(new Object[]{array[0], array[1]});
        }
    }
    public void removeRow() {
        int selectedRow = abbrevEditFrame.getTable().getSelectedRow();
        abbrevEditFrame.getTableModel().removeRow(
                abbrevEditFrame.getTable().convertRowIndexToModel(selectedRow));
    }
    
    private void showSearchResults(int row, int col)
    {

    }
}
