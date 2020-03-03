package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by osboxes on 21/04/16.
 */
public class DictionaryUtil {

    private static String possitioningCommand="pwd>enterPath.txt";
    private static String pathFile="enterPath.txt";
    private String dictionaryName="Dictionar/final-dictionarAprioriTest.txt";
    private  String getCurrentPossition() throws IOException, InterruptedException {
        Process p1 = Runtime.getRuntime().exec(new String[]{"sh","-c",possitioningCommand});
        p1.waitFor();
        BufferedReader reader = new BufferedReader(new FileReader(pathFile));
        return reader.readLine()+"/";
    }
    public void addNewWord(String word){
        try {
            String possitioning=this.getCurrentPossition()+dictionaryName;
            Process p =Runtime.getRuntime().exec(new String[]{"sh","-c","echo '"+word+"' >>"+possitioning});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private String formatForSearch(String textToSearch){
        return "^"+textToSearch+"$";
    }
    public boolean existsInDictionary(String textToSearch) throws IOException, InterruptedException {
        String formatedText=this.formatForSearch(textToSearch);
        String possitioning=this.getCurrentPossition()+dictionaryName;
        Process p =Runtime.getRuntime().exec(new String[]{"sh","-c","grep "+formatedText+" "+possitioning});
        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) );
        if(in.readLine() != null) {
            in.close();
            return true;
        }
        in.close();
        return false;

    }
}
