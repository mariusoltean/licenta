/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Util.FolderUtil;
import Util.GroupRules;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by osboxes on 31/01/16.
 */
public class CasesForRulesModel {

    private final String filename;

    public CasesForRulesModel(String key) throws IOException, InterruptedException {
        GroupRules.findRulesForKeysForShow(key);
        String tempCol=key.split(" <-")[0];
        this.filename= FolderUtil.getCurrentPossition() + "apriori\\" +GroupRules.buildColumnDocumentWithIndexForShow(tempCol,0);
    }

    public List<String> getCasesForRulesFromFile() {
        try {
            //S-Lum_VGI <- antrului
            List<String> lines;
            File file = new File(filename);
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            //Collections.sort(lines, String.CASE_INSENSITIVE_ORDER);
            return lines;
        } catch (IOException ex) {
            Logger.getLogger(AbbreviationsEditingModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
