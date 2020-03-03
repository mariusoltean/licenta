/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by osboxes on 31/03/16.
 */
public class ValidationRulesModel {
    
    private final String filename = "apriori/aprioriRules.out";
    
    public HashMap<ArrayList<String>, ArrayList<String>> readFile() {
        try { 
            List<String> lines;
            File file = new File(filename);
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            if(lines.size()!=0) {
                ArrayList<String> lista_rules = new ArrayList<>();
                ArrayList<String> lista_support = new ArrayList<>();

                for (int i = 0; i < lines.size(); i++) {
                    String[] s = lines.get(i).split(" \\(");
                    lista_rules.add(s[0]); // add reguli
                    lista_support.add(s[1]); // add support
                }

                ListIterator<String> it = lista_support.listIterator();
                while (it.hasNext()) {
                    String elem = it.next();
                    String[] s = elem.split(",");
                    it.set(s[0]);
                }

                lista_rules.remove(0);
                lista_support.remove(0);

                HashMap<ArrayList<String>, ArrayList<String>> map =
                        new HashMap<ArrayList<String>, ArrayList<String>>();
                map.put(lista_rules, lista_support);
                return map;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void saveRulesToFile(String stomach_part, List<String> ruleList) {
        try {
                
                String nameFile = "../Licenta/reguli/"+stomach_part + "_rulesOut.out";
                FileWriter fileWriter; 
                fileWriter = new FileWriter(nameFile, true);
                
                BufferedWriter bufferWritter = new BufferedWriter(fileWriter);

                for (String s : ruleList) {
                    bufferWritter.write(s + "\n");
                }
                bufferWritter.close();

                System.out.println("Done");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
    }
    
}
