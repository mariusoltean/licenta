/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static Util.LinesFilterer.filterInputData;

/**
 * Created by osboxes on 31/01/16.
 */
public class GenerateRules {

    String possitioning = "";
    String aprioriExec = "aprioriLinux ";
    String findstrCmd = "grep ";
    String sortCmd = "sort ";

    public GenerateRules() {
        possitioning = FolderUtil.getCurrentPossition();
    }

    public void runApriori(String confid, String supp, String numeColoana, String valoareColoana) {
        try {
            String aprioriParameters = " -tr " + "-c" + confid + " -s" + supp + " -q0 -n5 ";
            String inputFile = "Data." + numeColoana + ".in";
            String outputFile = "apriori/aprioriRules.out";
            String colValue = numeColoana + "_" + valoareColoana;
            String intermediateFile = "apriori/Data." + numeColoana + "_" + valoareColoana + ".in";
            List<String> filteredLines = filterInputData(inputFile, colValue);
            Files.write(Paths.get(possitioning + intermediateFile), filteredLines);
            Process generateRulesProcess = Runtime.getRuntime().exec("cmd /c start cmd.exe /C" + possitioning + "apriori.exe " + aprioriParameters + " " + intermediateFile + " " + outputFile);
            generateRulesProcess.waitFor();
            Thread.sleep(1000);
            Files.write(Paths.get(possitioning + outputFile), filterInputData(outputFile, colValue+ " <-")
                                                                    .stream()
                                                                    .sorted()
                                                                    .collect(Collectors.toList()));
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

            Process p1 = Runtime.getRuntime().exec(new String[]{"sh", "-c", possitioning + aprioriExec + aprioriParameters + possitioning + inputFile + " - | " + findstrCmd + "\"" + colValue + " <-\"" + " | " + sortCmd + " > " + possitioning + outputFile});
            p1.waitFor();

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GenerateRules.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
