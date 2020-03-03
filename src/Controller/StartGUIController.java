/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.AbbreviationsEditingModel;
import Model.ImportDataModel;
import Model.ValidationRulesModel;
import View.AbbreviationsEditingFrame;
import View.ImportDataFrame;
import View.StartGUIFrame;
import View.ValidationRulesFrame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by osboxes on 31/01/16.
 */
public class StartGUIController {

    private final StartGUIFrame startGUIFrame;

    public StartGUIController(StartGUIFrame startGUI) {

        this.startGUIFrame = startGUI;
        this.startGUIFrame.addListenerButtonAbbrevEditing(new ButtonAbbreviationsEditingListener());
        this.startGUIFrame.addListenerButtonImportData(new ButtonImportDataListener());
        this.startGUIFrame.addListenerButtonValidationRules(new ButtonValidationRules());
        this.startGUIFrame.addListenerButtonExit(new ListenerButtonExit());
    }

    class ButtonAbbreviationsEditingListener implements ActionListener {

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

    class ButtonImportDataListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                final Dimension prefferedSize = new Dimension(1200, 1000);
                ImportDataFrame importData = new ImportDataFrame(prefferedSize);
                importData.setVisible(true);
                ImportDataModel impDataModel = new ImportDataModel();
                ImportDataController importDataController = new ImportDataController(importData, impDataModel);
            } catch (IllegalArgumentException e1) {

            }
        }
    }

    class ButtonValidationRules implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                final Dimension prefferedSize = new Dimension(1200, 1000);
                ValidationRulesFrame validationRules = new ValidationRulesFrame(prefferedSize);
                validationRules.setVisible(true);
                ValidationRulesModel model = new ValidationRulesModel();
                ValidationRulesController rulesController = new ValidationRulesController(
                        validationRules, model);
            } catch (IllegalArgumentException e1) {
            }
        }
    }

    class ListenerButtonExit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                System.exit(0);
            } catch (IllegalArgumentException e1) {
            }
        }
    }
}
