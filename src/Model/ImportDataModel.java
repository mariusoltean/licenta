/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.AbbreviationsEditingController;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 * Created by osboxes on 31/03/16.
 */
public class ImportDataModel {
    
    private final String filename = "AnnotatedData.csv";
    
    public void saveToFile(DefaultTableModel tableModel) {
        Writer writer = null;
        int nRow = tableModel.getRowCount();
        int nCol = tableModel.getColumnCount();
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));

            //writer row information
            for (int i = 0; i < nRow; i++) {
                StringBuffer buffer = new StringBuffer();
                for (int j = 0; j < nCol; j++) {
                    buffer.append(tableModel.getValueAt(i, j));
                    if (j != nCol) {
                        buffer.append(",");
                    }
                }
                writer.write(buffer.toString() + "\r\n");
            }
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(AbbreviationsEditingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AbbreviationsEditingModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(AbbreviationsEditingModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
