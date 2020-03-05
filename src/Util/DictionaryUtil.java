package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by osboxes on 21/04/16.
 */
public class DictionaryUtil {

    private String dictionaryName = "Dictionar/final-dictionarAprioriTest.txt";

    public void addNewWord(String word) {
        try {
            String possitioning = FolderUtil.getCurrentPossition() + dictionaryName;
            Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "echo '" + word + "' >>" + possitioning});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatForSearch(String textToSearch) {
        return "^" + textToSearch + "$";
    }

    public boolean existsInDictionary(String textToSearch) throws IOException, InterruptedException {
        String formatedText = this.formatForSearch(textToSearch);
        String possitioning = FolderUtil.getCurrentPossition() + dictionaryName;
        Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c", "grep " + formatedText + " " + possitioning});
        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
        if (in.readLine() != null) {
            in.close();
            return true;
        }
        in.close();
        return false;

    }
}
