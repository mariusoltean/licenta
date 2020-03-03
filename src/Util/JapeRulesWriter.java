package Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by osboxes on 11/05/16.
 */
public class JapeRulesWriter {
    private static final int NUMBEROFNONRULEWORDS=1;
    private static final String  folderName="reguli/";

    public static void generateJapeRules() throws IOException {
        //private static String fileName="S-Lum_rulesOut.out";
        List<String> fileNames=FolderUtilitary.getFileNamesFromRulesFolder(folderName);

        for (String fileName:fileNames) {

            final String japeRulesFileName=fileName.split("_")[0]+"_japeRulesOut.jape";
            final String rulesFile = FolderUtilitary.getCurrentPossition() + folderName + fileName;
            BufferedReader reader = new BufferedReader(new FileReader(rulesFile));
            ArrayList<String> rules = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                rules.add(line);
                System.out.println(line);
            }
            reader.close();
            PrintWriter writer = new PrintWriter("japeRules/" + japeRulesFileName, "UTF-8");
            //writer.close();
            int i = 0;
            for (String rule : rules) {
                String keyWord = rule.split("_")[1].split("<")[0].trim();
                writer.println("Phase: firstpass");
                writer.println("Input: Token");
                writer.println("Options: control=brill");
                writer.println("");
                writer.println("Rule: " + keyWord.toUpperCase());
                writer.println("Priority: 40");
                writer.println("(");

//                if(!rule.split("_")[1].split("<")[0].trim().equals(keyWord)){
//                    keyWord=rule.split("_")[1].split("<")[0].trim();
//                }
                String[] ruleWords = rule.split("<-")[1].trim().split(" ");
                i=0;
                for (String single : ruleWords) {
                    i++;
                    writer.println("{Token.string==\"" + single + "\"}");
                    if (i < ruleWords.length) {
                        //writer.println("|");
                        for (int j = 0; j < NUMBEROFNONRULEWORDS; j++) {
                            writer.println("({Token.kind==word})?");
                        }
                    }

                }

                writer.println("):" + keyWord.toLowerCase());
                writer.println("-->");
                writer.println(":" + keyWord.toLowerCase() + "." + keyWord.toUpperCase() + "={rule=\"" + keyWord.toUpperCase() + "\"}");
            }
            writer.close();
        }


    }
}
