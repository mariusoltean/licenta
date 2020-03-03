/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexandra
 */
public class MedicalTermProcessing {

    public ArrayList<String> extractValuesFromColumn(String numeColoana) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("DataForGenerateRules.csv"));
            List<String> listAllLines = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                listAllLines.add(line);
            }

            List<String> listColumn = new ArrayList<String>(); //lista cu rezultatele de pe coloana specificata
            List<String> listColoanaExapandata = new ArrayList<String>();

            String[] headers = listAllLines.get(0).split(",");
            int index_header = 0;
            for (int i = 0; i < headers.length; i++) {
                if (numeColoana.equals(headers[i])) {
                    index_header = i;
                }
            }

            String[] elementsofColumn = null;
            for (String s : listAllLines) {
                elementsofColumn = s.split(",");
                listColumn.add(elementsofColumn[index_header]);
                listColoanaExapandata.add(elementsofColumn[0]);

            }
            listColumn.remove(0);
            listColoanaExapandata.remove(0);

            PrintWriter writer = new PrintWriter("Data."+ numeColoana + ".in", "UTF-8");
            ArrayList<String> listaFinala = new ArrayList<>();
            for (int i = 0; i < listColumn.size(); i++) {
                String stringConcat = numeColoana + "_" + listColumn.get(i) + " " + listColoanaExapandata.get(i);
                writer.println(stringConcat);
                listaFinala.add(stringConcat);
            }
            writer.close();
            reader.close();

            return listaFinala;

        } catch (IOException ex) {
            Logger.getLogger(MedicalTermProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    /**
     * Metoda care returneaza o lista cu valorile unei coloane
     *
     * @param numeColoana
     * @return
     */
    public ArrayList<String> getValuesColumn(String numeColoana) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("DataForGenerateRules.csv"));
            List<String> listAllLines = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                listAllLines.add(line);
            }

            List<String> listColumn = new ArrayList<String>();       //lista cu rezultatele de pe coloana specificata

            String[] headers = listAllLines.get(0).split(",");
            int index_header = 0;
            for (int i = 0; i < headers.length; i++) {
                if (numeColoana.equals(headers[i])) {
                    index_header = i;
                }
            }
            String[] elementsofColumn = null;
            for (String s : listAllLines) {
                elementsofColumn = s.split(",");
                listColumn.add(elementsofColumn[index_header]);

            }
            listColumn.remove(0);

            ArrayList<String> lista_finala = new ArrayList<>();
            for (int i = 0; i < listColumn.size(); i++) {
                if (!(lista_finala.contains(listColumn.get(i)))) {
                    lista_finala.add(listColumn.get(i));
                }
            }
            reader.close();
            return lista_finala;
        } catch (IOException ex) {
            Logger.getLogger(MedicalTermProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
