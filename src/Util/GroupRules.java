package Util;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by osboxes on 11/02/16.
 */
public class GroupRules {

    private static class objToHandle {
        private String keyObj;
        private Float valueObj;

        public objToHandle(String key, Float val) {
            keyObj = key;
            valueObj = val;
        }

        public String getKeyObj() {
            return keyObj;
        }

        public void setKeyObj(String keyObj) {
            this.keyObj = keyObj;
        }

        public Float getValueObj() {
            return valueObj;
        }

        public void setValueObj(Float valueObj) {
            this.valueObj = valueObj;
        }
    }

    private static List<objToHandle> candidateRules;
    private static int support = 40;
    private final static int stepValue = 5;//5% error margin for the difference between 2 rules
    private static String currPath = FolderUtil.getCurrentPossition() + "apriori\\";

    public static String buildColumnDocumentWithIndex(String value, int i) {
        return "Data." + value.replace("*", "") + i + ".in";
    }

    public static String buildColumnDocumentWithIndexForShow(String value, int i) {
        return "Data." + value.replace("*", "") + i + "_show.in";
    }

    public static String buildColumnDocumentWithIndexForShowComp(String value, String comp) {
        return "Data." + value.trim().replace("*", "") + comp + "_show.in";
    }

    public static String buildColumnDocument(String value) {
        return "Data." + value.trim().replace("*", "") + ".in";
    }

    public static String buildDifferenceDocument(int i, int j) {
        return "Difference" + i + "_" + j + ".in";
    }

    public static String buildNumberDifferenceDocument(int i, int j) {
        return "NumberOfDifferences" + i + "_" + j + ".in";
    }

    private static String buildColumnDocumentForShow(String value) {
        return "Data." + value + "_show.in";
    }

    public static List<String> getKeys(List<objToHandle> o) {
        List<String> keys = new ArrayList<>();
        for (objToHandle dummy : o) {
            keys.add(dummy.getKeyObj());
        }
        return keys;
    }

    private static List<Float> getValues(List<objToHandle> o) {
        List<Float> values = new ArrayList<>();
        for (objToHandle dummy : o) {
            values.add(dummy.getValueObj());
        }
        return values;
    }

    public static int groupThemTogether(List<String> keys, List<String> values) throws IOException, InterruptedException {
        List<objToHandle> rulesToGroup = getConfidentRules(keys, values);
        List<String> objKeys = getKeys(rulesToGroup);
        if (objKeys.size() != 0) {
            findRulesForKeys(objKeys);
            findDifferencesBetweenKeys(objKeys);
            findNumberOfDifferencesBetweenKeys(objKeys);
            // Integer[] result=populateSimilarRulesList(rulesToGroup);
            setCandidateRules(rulesToGroup);
            return 0;
        }
        return -1;
    }

    private static String formatValueForGREP(String value) {
        String temp = value.split("<- ")[1];
        String res = temp.replaceAll(" ", ".*");
        return res;
    }

    private static List<objToHandle> getConfidentRules(List<String> keys, List<String> values) {
        List<objToHandle> temp = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            if (Float.parseFloat(values.get(i).toString()) > support) {
                temp.add(new objToHandle((keys.get(i)), Float.parseFloat(values.get(i).toString())));
            }
        }
        for (int i = 0; i < temp.size() - 1; i++) {
            for (int j = i + 1; j < temp.size(); j++) {
                if (temp.get(i).getValueObj() > temp.get(j).getValueObj()) {
                    Collections.swap(temp, i, j);
                }
            }
        }
        return temp;
    }

    private static void findRulesForKeys(List<String> keys) throws IOException {
        String tempCol = keys.get(0).split(" <-")[0];
        String columnFile = buildColumnDocument(tempCol);
        String outputFile = "";
        for (int i = 0; i < keys.size(); i++) {
            outputFile = buildColumnDocumentWithIndex(tempCol, i);
            System.out.println("grep " + formatValueForGREP(keys.get(i)) + " " + currPath + columnFile + " > " + currPath + outputFile);
            List<String> filteredLines = LinesFilterer.filterInputData(currPath + columnFile, formatValueForGREP(keys.get(i)));
            Files.write(Paths.get(currPath + outputFile), filteredLines);
        }
    }

    public static void findRulesForKeysForShow(String key) throws IOException {
        String tempCol = key.split(" <-")[0];
        String columnFile = buildColumnDocument(tempCol);
        String outputFile = "";
        outputFile = buildColumnDocumentWithIndexForShow(tempCol, 0);
        System.out.println("grep -n " + formatValueForGREP(key + " " + currPath + columnFile + " > " + currPath + outputFile));
        List<String> filteredLines = LinesFilterer.filterInputData(currPath + columnFile, formatValueForGREP(key));
        List<String> numberedFilteredLines = new ArrayList<>(filteredLines.size());

        for (int i =0; i < filteredLines.size(); i++){
            numberedFilteredLines.add((i+1) + ":" +filteredLines.get(i));
        }

        Files.write(Paths.get(currPath + outputFile), numberedFilteredLines);
    }

    public static void findRulesForKeysForShowComparison(String key, String index) throws IOException {
        String tempCol = key.split(" <-")[0];
        String columnFile = buildColumnDocument(tempCol);
        String outputFile = "";
        outputFile = buildColumnDocumentWithIndexForShowComp(tempCol, index);
        System.out.println("grep -n " + formatValueForGREP(key + " " + currPath + columnFile + " > " + currPath + outputFile));
        List<String> filteredLines = LinesFilterer.filterInputData(currPath + columnFile, formatValueForGREP(key));
        List<String> numberedFilteredLines = new ArrayList<>(filteredLines.size());

        for (int i =0; i < filteredLines.size(); i++){
            numberedFilteredLines.add((i+1) + ":" +filteredLines.get(i));
        }

        Files.write(Paths.get(currPath + outputFile), numberedFilteredLines);
    }

    private static void findDifferencesBetweenKeys(List<String> keys) throws IOException {
        String tempCol = keys.get(0).split(" <-")[0];
        for (int i = 0; i < keys.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                if (i != j) {

                    List<String> original = Files.readAllLines(Paths.get(currPath + buildColumnDocumentWithIndex(tempCol, i)));
                    List<String> revised = Files.readAllLines(Paths.get(currPath + buildColumnDocumentWithIndex(tempCol, j)));
                    List<String> extraOriginalRows = new ArrayList<>();
                    // Compute diff. Get the Patch object. Patch is the container for computed deltas.
                    Patch patch = DiffUtils.diff(original, revised);
                    List<Delta> deltas = patch.getDeltas();
                    for (Delta delta : deltas) {
                        System.out.println(delta);
                        extraOriginalRows.addAll(delta.getOriginal().getLines());
                    }
                    Files.write(Paths.get(currPath + buildDifferenceDocument(i, j)), extraOriginalRows);
                }
//                Runtime.getRuntime().exec(new String[]{"sh","-c","diff "+currPath+buildColumnDocumentWithIndex(tempCol,i)+" "+currPath+buildColumnDocumentWithIndex(tempCol,j)+" | grep \"^<\" "
//                        +" > "+currPath+buildDifferenceDocument(i,j)
//                });


            }
        }
    }

    private static void findNumberOfDifferencesBetweenKeys(List<String> keys) throws IOException, InterruptedException {
        String tempCol = keys.get(0).split(" <-")[0];
        for (int i = 0; i < keys.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                if (i != j) {
                    List<String> original = Files.readAllLines(Paths.get(currPath + buildColumnDocumentWithIndex(tempCol, i)));
                    List<String> reviewed = Files.readAllLines(Paths.get(currPath + buildColumnDocumentWithIndex(tempCol, j)));
                    List<String> differencesDocument = Files.readAllLines(Paths.get(currPath + buildDifferenceDocument(i, j)));

                    List<String> numberOfDifferenceDocContent = new ArrayList<>();
                    numberOfDifferenceDocContent.add(original.size() + "");
                    numberOfDifferenceDocContent.add(reviewed.size() + "");
                    numberOfDifferenceDocContent.add(differencesDocument.size() + "");

                    Files.write(Paths.get(currPath + buildNumberDifferenceDocument(i,j)), numberOfDifferenceDocContent);
                }
            }
        }
    }

    private static Integer[] populateSimilarRulesList(List<String> keys) throws IOException {
        Integer[] pointsArray = new Integer[keys.size()];
        Arrays.fill(pointsArray, new Integer(0));
        for (int i = 0; i < keys.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                if (i != j) {
                    final BufferedReader reader = new BufferedReader(new FileReader(currPath + buildNumberDifferenceDocument(i, j)));
                    final ArrayList<Integer> similarRulesList = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        similarRulesList.add(Integer.parseInt(line));
                    }
                    if (similarRulesList.get(0) != 0) {
                        if ((similarRulesList.get(0) < similarRulesList.get(1)) && (similarRulesList.get(2) == 0)) {
                            //THE RULES ARE EXACTLY THE SAME
                            //The first rule is included in the second one
                            System.out.println("The first rule is included in the second one");
                            pointsArray[i] += 2;
                        } else {
                            if ((similarRulesList.get(2) / similarRulesList.get(0) < stepValue) && (similarRulesList.get(1) / similarRulesList.get(0) < stepValue)) {
                                //The rules are almost similar
                                System.out.println("The rules are almost similar");
                                pointsArray[i] += 1;
                            } else {
                                pointsArray[i] -= 1;
                            }
                        }
                        System.out.print(pointsArray);
                    }
                }

            }

        }
        int max = 0;
        for (int i = 0; i < pointsArray.length; i++) {
            if (pointsArray[i] > max) {
                max = pointsArray[i];
            }
        }
        int counter = -1;
        for (int i = 0; i < pointsArray.length - 1; i++) {
            for (int j = 1; j < pointsArray.length; j++) {
                if ((pointsArray[i] == pointsArray[j]) && (pointsArray[i] == max)) {
                    final String tempI = keys.get(i);
                    final String tempJ = keys.get(j);
                    if ((tempI.length() - tempI.replace(" ", "").length()) > (tempJ.length() - tempJ.replace(" ", "").length())) {
                        counter = i;
                        final BufferedReader reader = new BufferedReader(new FileReader(currPath + buildNumberDifferenceDocument(i, j)));
                        final ArrayList<Integer> similarRulesList = new ArrayList<>();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            similarRulesList.add(Integer.parseInt(line));
                        }

                        if ((similarRulesList.get(0).equals(similarRulesList.get(1))) && (similarRulesList.get(2) == 0)) {
                            String temporary;
                            if (!keys.get(j).contains("*")) {
                                temporary = "*" + keys.get(j);
                            } else {
                                temporary = keys.get(j);
                            }
                            keys.set(j, temporary);
                        }
                    } else {

                        if ((tempJ.length() - tempJ.replace(" ", "").length()) > (tempI.length() - tempI.replace(" ", "").length())) {
                            counter = j;
                            final BufferedReader reader = new BufferedReader(new FileReader(currPath + buildNumberDifferenceDocument(j, i)));
                            final ArrayList<Integer> similarRulesList = new ArrayList<>();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                similarRulesList.add(Integer.parseInt(line));
                            }
                            if ((similarRulesList.get(0).equals(similarRulesList.get(1))) && (similarRulesList.get(2) == 0)) {
                                String temporary;
                                if (!keys.get(i).contains("*")) {
                                    temporary = "*" + keys.get(i);
                                } else {
                                    temporary = keys.get(i);
                                }
                                keys.set(i, temporary);
                            }
                        }
                    }
                }
            }
        }
        if (counter != -1) {
            pointsArray[counter]++;
        }
        return pointsArray;
    }

    private static List<objToHandle> getCandidateRules() {
        return candidateRules;
    }

    private static void setCandidateRules(List<objToHandle> candidateRules) {
        GroupRules.candidateRules = candidateRules;
    }

    //List<String> keys,List<String> values
    public static List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < getCandidateRules().size(); i++) {
            keys.add(getCandidateRules().get(i).getKeyObj());
        }
        return keys;
    }

    public static List<String> getValues() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < getCandidateRules().size(); i++) {
            values.add(getCandidateRules().get(i).getValueObj().toString());
        }
        return values;
    }

    public static int dominantPossition(List<String> keys) throws IOException, InterruptedException {
        Integer[] possitions = populateSimilarRulesList(keys);
        int max = 0;
        int counter = 0;
        for (int i = 0; i < possitions.length; i++) {
            if (possitions[i] > max) {
                max = possitions[i];
                counter = i;
            }
        }
        return counter;
    }
}
