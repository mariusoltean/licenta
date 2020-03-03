package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by osboxes on 11/05/16.
 */
public class FolderUtilitary {

    private static String possitioningCommand="pwd>enterPath.txt";
    private static String pathFile="enterPath.txt";
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


    public static List<String> getFileNamesFromRulesFolder(String folderName){
        File folder = new File(getCurrentPossition()+folderName);
        File[] listOfFiles = folder.listFiles();
        List<String> fileNames=new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                //System.out.println(file.getName());
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }
}
