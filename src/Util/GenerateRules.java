/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by osboxes on 31/01/16.
 */
public class GenerateRules {

    String possitioning = "";
    String aprioriExec = "aprioriLinux ";
    String findstrCmd = "grep ";
    String sortCmd = "sort ";
    private static String possitioningCommand="pwd>enterPath.txt";
    private static String pathFile="enterPath.txt";

    public GenerateRules(){
        possitioning=" "+getCurrentPossition();
    }
    public static String getCurrentPossition() {
        Process p1 = null;
        try {
            p1 = Runtime.getRuntime().exec(new String[]{"sh","-c",possitioningCommand});
            p1.waitFor();
            BufferedReader reader = new BufferedReader(new FileReader(pathFile));
            return reader.readLine()+"/";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void runApriori(String confid, String supp, String numeColoana, String valoareColoana) {
        try {
            String aprioriParameters = " -tr " + "-c" + confid + " -s" + supp + " -q0 -n5 - -";
            String inputFile = "Data." + numeColoana + ".in";
            String outputFile = "apriori/aprioriRules.out";
            String colValue = numeColoana + "_" + valoareColoana;
            String intermediateFile = "apriori/Data." + numeColoana + "_" + valoareColoana + ".in";
            Process p1 = Runtime.getRuntime().exec(new String[]{"sh","-c",findstrCmd + colValue + possitioning+inputFile + " | " +
                    possitioning+aprioriExec + aprioriParameters + " | " +
                    findstrCmd +"\"" +colValue+" <-\"" + " | " +
                    sortCmd + " > " +possitioning+outputFile});
            p1.waitFor();
            Runtime.getRuntime().exec(new String[]{"sh","-c",findstrCmd + colValue + possitioning+inputFile + " > " + possitioning+intermediateFile});

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GenerateRules.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void runAprioriScript2(String confid, String supp, String numeColoana, String valoareColoana) {
        try {
            String aprioriParameters = " -tr " + "-c" + confid + " -s" + supp + " -q0 -n5";
            String inputFile = "apriori/Data." + numeColoana + "_" + valoareColoana + ".in";
            String colValue = numeColoana + "_" + valoareColoana;
            String outputFile = "apriori/aprioriRules.out";

            Process p1 = Runtime.getRuntime().exec(new String[]{"sh","-c",possitioning+aprioriExec + aprioriParameters + possitioning+inputFile + " - | " + findstrCmd +"\"" +colValue+" <-\"" +  " | " + sortCmd + " > " + possitioning+outputFile});
            p1.waitFor();

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GenerateRules.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
