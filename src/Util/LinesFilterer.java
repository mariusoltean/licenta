package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class LinesFilterer {

    public static List<String> filterInputData(String inputFile, String colValue){
        List<String> filteredLines = new ArrayList<>();
        colValue = ".*" + colValue + ".*";
        File file =new File(inputFile);
        Scanner in = null;
        try {
            in = new Scanner(file);
            while(in.hasNext())
            {
                String line=in.nextLine();
                if(line.matches(colValue))
                    filteredLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filteredLines;
    }
}
