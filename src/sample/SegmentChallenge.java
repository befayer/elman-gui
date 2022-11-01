package sample;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SegmentChallenge {

    ArrayList<Double> results = new ArrayList<>();
    ArrayList<String> rows = new ArrayList<>();

    public void classificate(int countEpochs, double alpha, double K) {
        try {
            ArrayList<Double> arrayList = new ArrayList<>();
            String fileName = "segment-challenge.data";
            double[][] allData = data2Matrix(fileName, 1500, 20);
            System.out.println("All data:");
            System.out.println(Arrays.deepToString(allData));
            double[][] trainData = new double[1430][20];
            double[][] testData = new double[70][20];
            int countClass = 7;
            int partLen = allData.length / countClass;
            int testPartLen = testData.length / countClass;
            for (int k = 0; k < countClass; k++) {
                for (int i = k * partLen; i < k * partLen + partLen; i++) {
                    for (int j = 0; j < allData[i].length; j++) {
                        if (i < partLen * (k + 1) - testPartLen)
                            trainData[i - testPartLen * k][j] = allData[i][j];
                        else testData[i - (partLen - testPartLen) * (k + 1)][j] = allData[i][j];
                    }
                }
            }
            System.out.println("Train data:");
            System.out.println(Arrays.deepToString(trainData));
            trainData = shuffleMatrix(trainData);
            System.out.println("Train data suffle:");
            System.out.println(Arrays.deepToString(trainData));
            System.out.println("Test data:");
            System.out.println(Arrays.deepToString(testData));
            Network neuralNetwork = new Network(countEpochs, alpha, K, 19, countClass, trainData);
            neuralNetwork.elman();
            neuralNetwork.setEntersZero();
            System.out.println("Output:");
            rows.add("Output:");
            int cl = 1;
            int countTrue = 0;
            int maxI = 0;
            for (int i = 0; i < testData.length; i++) {
                if (i % 10 == 0) {
                    System.out.println("Class " + cl + ":");
                    rows.add("Class " + cl + ":");
                    cl++;
                }
                double[] arr = neuralNetwork.directDistribution(testData[i]);
                double max = Double.MIN_VALUE;
                for (int j = 0; j < arr.length; j++) {
                    if (arr[j] > max) {
                        max = arr[j];
                        maxI = j;
                    }
                }
                if (maxI + 1 == testData[i][testData[0].length - 1]) {
                    countTrue++;
                }
                System.out.println(Arrays.toString(arr));
                rows.add(Arrays.toString(arr));
            }
            System.out.println("Correct/all: " + countTrue + " / " + testData.length);
            rows.add("Correct/all: " + countTrue + " / " + testData.length);
            System.out.println("K: " +  (double) countTrue/testData.length);
            rows.add("K: " +  (double) countTrue/testData.length);
            results.add((double)countTrue);
            results.add((double)testData.length);
            results.add((double)countTrue/testData.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double[][] data2Matrix(String fileName, int count, int entersLen) throws IOException {
        double[][] allData = new double[count][entersLen];
        ArrayList<String> result = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();
        String[] mas;
        int i = 0;
        while (line != null && !line.equals("")) {
            mas = line.replaceAll(" ", "").split(",");
            for (int j = 0; j < mas.length - 1; j++) {
                allData[i][j] = Double.parseDouble(mas[j]);
            }
            if (!result.contains(mas[mas.length - 1])) {
                result.add(mas[mas.length - 1]);
            }
            allData[i][allData[0].length - 1] = (result.indexOf(mas[mas.length - 1]) + 1);
            line = bufferedReader.readLine();
            i++;
        }
        return allData;
    }

    public ArrayList<String> getRows(){
        return this.rows;
    }

    public ArrayList<Double> getResults(){
        return this.results;
    }

    public static double[][] shuffleMatrix(double[][] matrix) {
        List<double[]> rows = new ArrayList<>(Arrays.asList(matrix));
        Collections.shuffle(rows);
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = rows.get(i);
        }
        return matrix;
    }

}
