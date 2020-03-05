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
public class FolderUtil {

    public static String getCurrentPossition() {
        return System.getProperty("user.dir") + "\\";
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
