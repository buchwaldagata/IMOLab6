package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GreedyCycleAlgorithmRunner {
    public static void run() throws IOException {

        Instance kroA200 = new Instance("src/main/resources/kroA200.tsp");
        Instance kroB200 = new Instance("src/main/resources/kroB200.tsp");
        Instance kro = kroA200;
        CoordinateList coordinateList = new CoordinateList("src/main/resources/kroA200.tsp");
        int[][] intCoordinateList = coordinateList.intCoordinateList;
        DistanceMatrix distanceMatrix = new DistanceMatrix(intCoordinateList);
        Long[][] distanceMatrix2 = distanceMatrix.distanceMatrix;

        long startTime = System.nanoTime();
        GreedyCycleAlgorithm greedyCycleAlgorithm = new GreedyCycleAlgorithm();
        Long maxGreedyCycle = (long) -1;
        Long minGreedyCycle = 1000000L;
        int minIndexGreedyCycle = -1;
        int totalLengthGreedyCycle = 0;
        List<List<List<Integer>>> fullList =  new ArrayList<>();
        for (int i = 0 ; i < 1000; i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(200);
            List<List<Integer>> listOfListOfCycle = greedyCycleAlgorithm.runAlgorithm(randomNumber, intCoordinateList, distanceMatrix2);
//            List<Integer> firstCycle = listOfListOfCycle.get(0);
//            List<Integer> secondCycle = listOfListOfCycle.get(1);
            new CandidatesMoves(kro, listOfListOfCycle);
            fullList.add(listOfListOfCycle);
        }
        PrawdopobieństwoLiczbyWspólnychWierzchołkówDośredniegoRozwiązania(fullList);

//        drugi//
        prawdopobieństwoLiczbyWspólnychWierzchołkówDoNajlepszegoRozwiązania(kro, fullList);


//
//        int  liczbaWspolnychKrawedzi = 0;
//        for (int i = 0 ; i < fullList.size(); i++){
//            for (int j = 0 ; j < fullList.size(); j++){
//                if (i != j){
//                    List<Integer> firstCycleInFirst = fullList.get(i).get(0);
//                    List<Integer> secondCycleInFirst = fullList.get(i).get(1);
//                    List<Integer> firstCycleInSecond = fullList.get(j).get(0);
//                    List<Integer> secondCycleInSecond = fullList.get(j).get(1);
//                    System.out.println("HEJ");
//
//                    int commonEdgesCount = countCommonEdges(firstCycleInFirst, firstCycleInSecond, secondCycleInSecond);
//                    liczbaWspolnychKrawedzi  += commonEdgesCount;
//                }
//            }
//
//        }
//        System.out.println("Liczba wspolnych krawedzi wynosi" + liczbaWspolnychKrawedzi);



    }

    private static void prawdopobieństwoLiczbyWspólnychWierzchołkówDoNajlepszegoRozwiązania(Instance kroA200, List<List<List<Integer>>> fullList) throws IOException {
        HAE hae = new HAE(kroA200);
        List<Integer> firstBestCycle = hae.cycles_X.get(0);
        List<Integer> secondBestCycle = hae.cycles_X.get(1);
//
        Map<Integer, List<Integer>> probabilitiDictionary = new HashMap<>();
        List<Integer> probabilityList = new ArrayList<>(100);
        List<Integer> probability ;
        for (int i = 0; i < fullList.size(); i++){
            probability= new ArrayList<>();
            List<Integer> firstCycleInFirst = fullList.get(i).get(0);
            List<Integer> secondCycleInFirst = fullList.get(i).get(1);

            if (numberOfCommonELements(firstCycleInFirst, firstBestCycle) > numberOfCommonELements(firstCycleInFirst, secondBestCycle)){
                int result = numberOfCommonELements(firstCycleInFirst, firstBestCycle) + numberOfCommonELements(secondCycleInFirst, secondBestCycle);
                probability.add(result);
            } else{
                int result = numberOfCommonELements(firstCycleInFirst, secondBestCycle) + numberOfCommonELements(secondCycleInFirst, firstBestCycle);
                probability.add(result);
            }
            probabilitiDictionary.put(i, probability);
        }

        Map<Integer, Double> averageDictionary = new HashMap<>();
        for (Integer key : probabilitiDictionary.keySet()) {
            List<Integer> valueList = probabilitiDictionary.get(key);
            double sum = 0;
            for (Integer value : valueList) {
                sum += value;
            }
            double average = sum / (valueList.size()* 200);
            averageDictionary.put(key, average);
        }


        solutionToCsv("Prawdopobieństwo liczby wspólnych wierzchołków do najlepszego rozwiązania.csv",  averageDictionary);
    }

    private static void PrawdopobieństwoLiczbyWspólnychWierzchołkówDośredniegoRozwiązania(List<List<List<Integer>>> fullList) throws IOException {
        Map<Integer, List<Integer>> probabilitiDictionary = new HashMap<>();
        List<Integer> probabilityList = new ArrayList<>(100);
//        System.out.println("JEJ");
        List<Integer> probability;

        for (int i = 0; i < fullList.size(); i++){
            probability= new ArrayList<>();
            for (int j = 0; j < fullList.size(); j++){
                if (i != j){
                    List<Integer> firstCycleInFirst = fullList.get(i).get(0);
                    List<Integer> secondCycleInFirst = fullList.get(i).get(1);
                    List<Integer> firstCycleInSecond = fullList.get(j).get(0);
                    List<Integer> secondCycleInSecond = fullList.get(j).get(1);

                    if (numberOfCommonELements(firstCycleInFirst, firstCycleInSecond) > numberOfCommonELements(firstCycleInFirst, secondCycleInSecond)){
                        int result = numberOfCommonELements(firstCycleInFirst, firstCycleInSecond) + numberOfCommonELements(secondCycleInFirst, secondCycleInSecond);
                        probability.add(result);
                    } else{
                        int result = numberOfCommonELements(firstCycleInFirst, secondCycleInSecond) + numberOfCommonELements(secondCycleInFirst, firstCycleInSecond);
                        probability.add(result);
                    }


                }
//                else {
//                    probability.add(1);
//                }
            }
            probabilitiDictionary.put(i, probability);
        }
        Map<Integer, Double> averageDictionary = new HashMap<>();
        for (Integer key : probabilitiDictionary.keySet()) {
            List<Integer> valueList = probabilitiDictionary.get(key);
            double sum = 0;
            for (Integer value : valueList) {
                sum += value;
            }
            double average = sum / (valueList.size()* 200);
            averageDictionary.put(key, average);
        }


        solutionToCsv("sredniewierzcholki.csv",  averageDictionary);
    }

    public static int countCommonEdges(List<Integer> firstCycleInFirst, List<Integer> firstCycleInSecond, List<Integer>secondCycleInSecond) {
        Set<String> edgesFirst = extractEdges(firstCycleInFirst);
        Set<String> edgesSecond = extractEdges(firstCycleInSecond);
        Set<String> edgesThird = extractEdges(secondCycleInSecond);

        Set<String> commonEdges = new HashSet<>(edgesFirst);
        commonEdges.retainAll(edgesSecond);
        commonEdges.retainAll(edgesThird);

        return commonEdges.size();
    }
    public static Set<String> extractEdges(List<Integer> cycle) {
        Set<String> edges = new HashSet<>();
        for (int i = 0; i < cycle.size() - 1; i++) {
            edges.add(cycle.get(i) + "-" + cycle.get(i + 1));
        }
        return edges;
    }




    static void saveToFile(BufferedWriter bufferedWriter, int firstVertex, int x, int y, String nameOfFile){
        try {
            writePointToCsv(bufferedWriter, firstVertex, x, y);
//            System.out.println("Liczby zostały zapisane do pliku " + nameOfFile);

        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }

    private static void writePointToCsv(BufferedWriter bufferedWriter, int vertex, int x, int y) throws IOException {
        bufferedWriter.write(vertex + "," + x + "," + y + "\n");
    }
    public static Long getLengthFromCycle(Map<Integer, Integer> cycle, Long[][] distanceMatrix) {
        Long len = 0L;
        for(int i=0; i<cycle.keySet().size()-1; i++) {
            int fromVertex = cycle.get(i);
            int toVertex = cycle.get(i+1);
            len += distanceMatrix[fromVertex][toVertex];
        }
        return len;
    }

    static void saveCycle(int[][] coordinateList, Map<Integer, Integer> cycle, String filename) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for(int i: cycle.keySet()) {
            int vertex = cycle.get(i);
            for (int[] coordinateRow: coordinateList) {
                if(vertex==coordinateRow[0]) {
                    GreedyCycleAlgorithmRunner.saveToFile(bufferedWriter, coordinateRow[0], coordinateRow[1], coordinateRow[2], filename);
                }
            }
        }
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int numberOfCommonELements(List<Integer> lista1, List<Integer> lista2) {
        int count = 0;
        for (Integer element : lista1) {
            if (lista2.contains(element)) {
                count++;
            }
        }
        return count;
    }

    public static void solutionToCsv(String path, Map<Integer, Double> averageDictionary) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("x;y\n");
        for (Map.Entry<Integer, Double> entry : averageDictionary.entrySet()) {
            printWriter.printf("%d;%f\n", entry.getKey(), entry.getValue());
        }
//        for (Integer a : cycles.get(0)) {
//            printWriter.printf("%s,%d,%d\n","a", probabilitiDictionary.get(a), instance.coordinates.get(a).getValue());
//        }
//        for (Integer a : cycles.get(1)) {
//            printWriter.printf("%s,%d,%d\n","b", instance.coordinates.get(a).getKey(), instance.coordinates.get(a).getValue());
//        }
        printWriter.close();
    }
}