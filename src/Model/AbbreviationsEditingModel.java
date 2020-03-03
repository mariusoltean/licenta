/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.AbbreviationsEditingController;
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
public class AbbreviationsEditingModel {
    
    private final String filename = "Abrevieri.csv";
    
    public AbbreviationsEditingModel() {
        
    }
    
    public List<String> getAbbreviationsFromFile() {
        try {
            List<String> lines;
            File file = new File(filename);
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            Collections.sort(lines, String.CASE_INSENSITIVE_ORDER);
            return lines;
        } catch (IOException ex) {
            Logger.getLogger(AbbreviationsEditingModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

     /**
     * Metoda care salveaza in fisierul Abrevieri.csv modificarile efectuate in
     * tabelul din panelul AbbreviationsEditing
     *
     * @param tableModel
     */
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
    
    
class CustomRenderer implements TableCellRenderer
{
    JLabel label;
    int targetRow, targetCol;
  
    public CustomRenderer()
    {
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        targetRow = -1;
        targetCol = -1;
    }
  
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column)
    {
        if(isSelected)
        {
            label.setBackground(table.getSelectionBackground());
            label.setForeground(table.getSelectionForeground());
        }
        else
        {
            label.setBackground(table.getBackground());
            label.setForeground(table.getForeground());
        }
        if(row == targetRow && column == targetCol)
        {
            label.setBorder(BorderFactory.createLineBorder(Color.red));
            label.setFont(table.getFont().deriveFont(Font.BOLD));
        }
        else
        {
            label.setBorder(null);
            label.setFont(table.getFont());
        }
        label.setText((String)value);
        return label;
    }
  
    public void setTargetCell(int row, int col)
    {
        targetRow = row;
        targetCol = col;
    }
}
}
