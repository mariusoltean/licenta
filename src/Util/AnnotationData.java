/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * Created by osboxes on 31/01/16.
 */
public class AnnotationData {

    private HashMap<String, String> rules_map = new HashMap<>();

    public void readRules(String column_name) {
        try {

            String filename = "reguli/"+column_name+"_rulesOut.out";
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            ArrayList<String> listAllLines = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                listAllLines.add(line);
            }
            String key, value;
            String[] v1, v2, v3;
            for (String s : listAllLines) {
                v1 = s.split(column_name+"_");
                v2 = v1[1].split(" \\("); //v2[0] - RDG <- reflux duodeno gastric
                v3 = v2[0].split(" <- ");   //v3[0] - RDG  ,  v3[1] = reflux ...
                key = v3[1];
                value = v3[0];
                if(!rules_map.containsKey(key))
                    rules_map.put(key, value);
                else
                    System.out.println("cheie duplicataa !!!");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, "The rules are not validated yet!", "Alert!", JOptionPane.OK_OPTION);
        }
    }

    public HashMap<String, String> getRulesMap() {
        return rules_map;
    }

    
}
