/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.AbbreviationsEditingController;
import Util.GroupRules;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Created by osboxes on 31/01/16.
 */
public class CasesForRulesModel {

    private final String filename;

    public CasesForRulesModel(String key) throws IOException, InterruptedException {
        GroupRules.findRulesForKeysForShow(key);
        String tempCol=key.split(" <-")[0];
        this.filename=GroupRules.getCurrentPossition()+GroupRules.buildColumnDocumentWithIndexForShow(tempCol,0);

    }

    public List<String> getCasesForRulesFromFile() {
        try {
            //S-Lum_VGI <- antrului
            List<String> lines;
            File file = new File(filename);
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            //Collections.sort(lines, String.CASE_INSENSITIVE_ORDER);
            return lines;
        } catch (IOException ex) {
            Logger.getLogger(AbbreviationsEditingModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
