package Model;

import Util.FolderUtil;
import Util.GroupRules;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by osboxes on 06/04/16.
 */
public class CompareRulesModel {
    private final String filenameFirst;
    private final String filenameSecond;


    public CompareRulesModel(List<String> keys) throws IOException, InterruptedException {
        if (keys.size()==2) {
            String first=keys.get(0).trim().replace("*","");
            String second=keys.get(1).trim().replace("*","");
            GroupRules.findRulesForKeysForShowComparison(first,"First");
            GroupRules.findRulesForKeysForShowComparison(second,"Second");
            String tempColFirst = first.split(" <-")[0];
            String tempColSecond = second.split(" <-")[0];
            this.filenameFirst = FolderUtil.getCurrentPossition() + "apriori\\" + GroupRules.buildColumnDocumentWithIndexForShowComp(tempColFirst, "First");
            this.filenameSecond = FolderUtil.getCurrentPossition() + "apriori\\" + GroupRules.buildColumnDocumentWithIndexForShowComp(tempColSecond, "Second");
        }else{
            //BAD INPUT
            filenameFirst="";
            filenameSecond="";
        }
    }

    private List<String> getCasesForRulesFromFile(String filename) {
        try {
            List<String> result;
            File file = new File(filename);
            result = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            return result;
        } catch (IOException ex) {
            Logger.getLogger(CompareRulesModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public List<String> getAmBDifference(){
        List<String> casesForA=getCasesForRulesFromFile(filenameFirst);
        //List<String> tempA=casesForA;
        List<String> casesForB=getCasesForRulesFromFile(filenameSecond);
        //List<String> tempB=casesForB;
        casesForA.removeAll(casesForB);
        return casesForA;

    }
    public List<String> getBmADifference(){
        List<String> casesForA=getCasesForRulesFromFile(filenameFirst);
        //List<String> tempA=casesForA;
        List<String> casesForB=getCasesForRulesFromFile(filenameSecond);
        //List<String> tempB=casesForB;
        casesForB.removeAll(casesForA);
        return casesForB;

    }
    public List<String> intersection(List<String> a,List<String> b){
        List<String> result=new ArrayList<>();
        for (String dummy:a){
            if(b.contains(dummy)){
                result.add(dummy);
            }
        }
        return result;
    }
    public List<String> getAiB(){
        List<String> casesForA=getCasesForRulesFromFile(filenameFirst);
        //List<String> tempA=casesForA;
        List<String> casesForB=getCasesForRulesFromFile(filenameSecond);
        return intersection(casesForA,casesForB);
    }
}
