package Util;


import gate.*;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by osboxes on 09/05/16.
 */
public class GateConnector {
    private static String folderName="japeRules/";
    public static void runTranslations() throws GateException, IOException {
        {
            int i=0;
            JapeRulesWriter.generateJapeRules();
            /*
            **THIS WAS A TEST IN ORDER TO CHECK IF GATE ACTUALY PERFORMS ANNOTATIONS AND TO TEST OUR SOLUTION

            List<String> fileNames=FolderUtilitary.getFileNamesFromRulesFolder(folderName);
            Gate.init();
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());
            SerialAnalyserController pipeline = (SerialAnalyserController) gate.Factory.createResource("gate.creole.SerialAnalyserController");
            LanguageAnalyser tokeniser = (LanguageAnalyser) gate.Factory.createResource(
                    "gate.creole.tokeniser.DefaultTokeniser");
            pipeline.add(tokeniser);
            for (String japeRulesFileName:fileNames) {
                LanguageAnalyser jape = (LanguageAnalyser) gate.Factory.createResource(
                        "gate.creole.Transducer", gate.Utils.featureMap(
                                "grammarURL", new File("/home/osboxes/Downloads/Licenta/Licenta/japeRules/" + japeRulesFileName).toURI().toURL(),
                                "encoding", "UTF-8"));
                pipeline.add(jape);

            }
            Corpus corpus = gate.Factory.newCorpus(null);
            Document doc = gate.Factory.newDocument(new File("/home/osboxes/Downloads/Licenta/Licenta/resultWithoutRule.csv").toURI().toURL());
            DocumentContent dc = doc.getContent();
            corpus.add(doc);
            pipeline.setCorpus(corpus);
            pipeline.execute();
            System.out.println("Found annotations of the following types: " +
                    doc.getAnnotations().getAllTypes()+"\n");
            System.out.println(doc.getAnnotations().get("VGI"));
             */
        }


    }
}
