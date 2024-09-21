/*
 * Name: Sairam Soundararajan
 * Date: 11-13-22
 * Course: CMSC451: Design and Analysis of Computer Algorithms
 * Instructor: Prof. Dennis Didulo
 * Institution: University of Maryland Global Campus
 *
 * Purpose: This program serves as the main program to test whether iterative or recursive quicksort runs faster
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.stream.IntStream;



public class BenchmarkSorts {

    private final int NUMBER_OF_RUNS = 50;

    private int[] array;
    private static BenchmarkSorts benchmarkSorts;
    private int iterativeCount = 0;
    private int recursiveCount = 0;

    private int iterativeIndex = 0;
    private int recursiveIndex = 0;

    private long iterativeTime, recursiveTime;

    private final int [] iterativeCountLog = new int[NUMBER_OF_RUNS];
    private final int [] recursiveCountLog = new int[NUMBER_OF_RUNS];
    private final long [] iterativeTimeLog = new long[NUMBER_OF_RUNS];
    private final long [] recursiveTimeLog = new long[NUMBER_OF_RUNS];

    private static final QuickSort quickSort = new QuickSort();

    public static void main(String[] args) throws Exception {

    	int[] sizes = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        benchmarkSorts = new BenchmarkSorts(sizes);



    }


    public BenchmarkSorts(int[] sizes) {
        // Creates benchmarks based on the input array size
        IntStream.range(0, sizes.length).forEach(i -> {
            try {
                new BenchmarkSorts(sizes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private BenchmarkSorts(int number) throws IOException {
        // Outer loop 50 times (NUMBER_OF_RUNS)
        IntStream.range(0, NUMBER_OF_RUNS).forEach(i -> {
            array = new int[number];
            // Inner loop based on the array size (n)
            IntStream.range(0, number).forEach(j -> {
                Random random = new Random();
                array[j] = random.nextInt(1000);
            });
            // Runs the sort and displays output if an UnsortedException is found
            try {
                runSorts();
            }
            catch (UnsortedException e) {
                System.out.println(e.getMessage());
            }
        });
        displayReport(number);
    }

    private void runSorts() throws UnsortedException {
        // Runs iterative sort
        int returnCount;
        long returnTime;
       // System.out.println("\nITERATIVE SORT");
        int[] newArray1 = quickSort.iterativeSort(array, 0, array.length -1);
        //quickSort.printArr(newArray1, newArray1.length);
        //System.out.println("\n");
        quickSort.checkSortedArray(newArray1);
        returnCount = quickSort.getCount();
        returnTime = quickSort.getTime();
        iterativeCount = iterativeCount + returnCount;
        iterativeTime = iterativeTime + returnTime;
        iterativeCountLog[iterativeIndex] = iterativeCount;
        iterativeTimeLog[iterativeIndex] = iterativeTime;
        iterativeIndex++;

        // Runs recursive sort
        //System.out.println("\nRECURSIVE SORT");
        int[] newArray2 = quickSort.recursiveSort(array, 0, array.length-1);
        //quickSort.printArr(newArray2, newArray2.length);
        quickSort.checkSortedArray(newArray2);
        returnCount = quickSort.getCount();
        returnTime = quickSort.getTime();
        recursiveCount = recursiveCount + returnCount;
        recursiveTime = recursiveTime + returnTime;
        recursiveCountLog[recursiveIndex] = recursiveCount;
        recursiveTimeLog[recursiveIndex] = recursiveTime;
        recursiveIndex++;
    }

    private void displayReport(int arraySize) throws IOException {

        // Sets local variables
        double iterativeAverageCount = 0;
        double iterativeAverageTime = 0;
        double recursiveAverageCount = 0;
        double recursiveAverageTime = 0;
        double iterativeVarianceCount = 0;
        double iterativeVarianceTime = 0;
        double recursiveVarianceCount = 0;
        double recursiveVarianceTime = 0;
        double iterativeSDCount = 0;
        double iterativeSDTime = 0;
        double recursiveSDCount = 0;
        double recursiveSDTime = 0;

        // Calculates averages
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            iterativeAverageCount += iterativeCountLog[i];
            iterativeAverageTime += iterativeTimeLog[i];
            recursiveAverageCount += recursiveCountLog[i];
            recursiveAverageTime += recursiveTimeLog[i];
        }

        iterativeAverageCount = iterativeAverageCount / arraySize;
        iterativeAverageTime = iterativeAverageTime / arraySize;
        recursiveAverageCount = recursiveAverageCount / arraySize;
        recursiveAverageTime = recursiveAverageTime / arraySize;

        // Calculates standard deviations
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            iterativeVarianceCount += Math.pow(iterativeAverageCount - iterativeCountLog[i], 2);
            iterativeVarianceTime += Math.pow(iterativeAverageTime - iterativeTimeLog[i], 2);
            recursiveVarianceCount += Math.pow(recursiveAverageCount - recursiveCountLog[i], 2);
            recursiveVarianceTime += Math.pow(recursiveAverageTime - recursiveTimeLog[i], 2);
        }

        iterativeVarianceCount = iterativeVarianceCount / arraySize;
        iterativeVarianceTime = iterativeVarianceTime / arraySize;
        recursiveVarianceCount = recursiveVarianceCount / arraySize;
        recursiveVarianceTime = recursiveVarianceTime / arraySize;

        iterativeSDCount = Math.sqrt(iterativeVarianceCount);
        iterativeSDTime = Math.sqrt(iterativeVarianceTime);
        recursiveSDCount = Math.sqrt(recursiveVarianceCount);
        recursiveSDTime = Math.sqrt(recursiveVarianceTime);

        // Produces output
        System.out.println("Data Set Size (n): " + arraySize +
                "\n\tIterative Quick Sort Results: \t\t\t\t\tRecursive Quick Sort Results:" +
                "\n\tAverage Critical Operation Count: " + Math.round(iterativeAverageCount) +
                "\t\t\tAverage Critical Operation Count: " + Math.round(recursiveAverageCount) +
                "\n\tStandard Deviation of Count: " + Math.round(iterativeSDCount) +
                "\t\t\t\t\tStandard Deviation of Count: " + Math.round(recursiveSDCount) +
                "\n\tAverage Execution Time: " + Math.round(iterativeAverageTime) +
                "\t\t\t\t\t\tAverage Execution Time: " + Math.round(recursiveAverageTime) +
                "\n\tStandard Deviation of Time: " + Math.round(iterativeSDTime) +
                "\t\t\t\t\t\tStandard Deviation of Time: " + Math.round(recursiveSDTime));
        
        FileWriter rf_writer, if_writer;
        
        // open files in append mode
        rf_writer = new FileWriter(new File("RecursiveQuickSortBenchmark.txt"), true);
        if_writer = new FileWriter(new File("IterativeQuickSortBenchmark.txt"), true);
        
        if_writer.write(arraySize+"");
        for(int i = 0; i < iterativeCountLog.length; i++) {
        	if_writer.write("," + iterativeCountLog[i] + "," + iterativeTimeLog[i]);
        }
        if_writer.write("\n");
        
        rf_writer.write(arraySize+"");
        for(int i = 0; i < iterativeCountLog.length; i++) {
        	rf_writer.write("," + recursiveCountLog[i] + "," + recursiveTimeLog[i]);
        }
        rf_writer.write("\n");
        
        /*
        rf_writer.write("Data Set Size (n): " + arraySize + 
        		"\n\tRecursive QuickSort Results:" +
        		"\n\tAverage Critical Operation Count: " + Math.round(recursiveAverageCount) + 
        		"\n\tStandard Deviation of Count: " + Math.round(recursiveSDCount) +
        		"\n\tAverage Execution Time: " + Math.round(recursiveAverageTime) +
        		"\n\tStandard Deviation of Time: " + Math.round(recursiveSDTime) + "\n");
        
        if_writer.write("Data Set Size (n): " + arraySize + 
        		"\n\tIterative QuickSort Results:" +
        		"\n\tAverage Critical Operation Count: " + Math.round(iterativeAverageCount) + 
        		"\n\tStandard Deviation of Count: " + Math.round(iterativeSDCount) +
        		"\n\tAverage Execution Time: " + Math.round(iterativeAverageTime) +
        		"\n\tStandard Deviation of Time: " + Math.round(iterativeSDTime) + "\n");
        */
        // close files
        rf_writer.close();
        if_writer.close();
        
    }
}