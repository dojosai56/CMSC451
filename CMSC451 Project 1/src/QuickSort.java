/*
 * Name: Sairam Soundararajan
 * Date: 11-13-22
 * Course: CMSC451: Design and Analysis of Computer Algorithms
 * Instructor: Prof. Dennis Didulo
 * Institution: University of Maryland Global Campus
 *
 * Purpose: This class is simply a sorting algorithm that aids in the Benchmarking process
 */

public class QuickSort implements SortInterface {

    private static int counter = 0;
    private static long timeStart = 0;
    private static long timeEnd = 0;

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    //www.geeksforgeeks.org/quick-sort
    // this is where I referred to for creating quicksort
    public static int partition(int[] array, int low, int high) {

        int pivot = array[high];
        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {
            if(array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    //  function to recursively sort the list
    public int[] recursiveSort(int[] array, int low, int high) throws UnsortedException {
        counter++;
        timeStart = System.nanoTime();
        int i = partition(array, low, high);

        if(low < i - 1) {
            recursiveSort(array, low, i - 1);
        }

        if(high > i + 1) {
            recursiveSort(array, i + 1, high);
        }
        timeEnd = System.nanoTime();
        return array;
    }

    // www.geeksforgeeks.org/iterative-quick-sort
    // reference for making iteration for quicksort
    public int[] iterativeSort(int[] array, int low, int high) throws UnsortedException {
        timeStart = System.nanoTime();
        int stack[] = new int[high - low + 1];
        int top = -1;

        stack[++top] = low;
        stack[++top] = high;

        while (top >= 0) {
            high = stack[top--];
            low = stack[top--];

            int pivot = partition(array, low, high);

            if(low < pivot - 1) {
                stack[++top] = low;
                stack[++top] = pivot - 1;
            }

            if(pivot + 1 < high) {
                stack[++top] = pivot + 1;
                stack[++top] = high;
            }
            counter++;
        }
        timeEnd = System.nanoTime();
        return array;
    }

    //www.geeksforgeeks.org/quick-sort
    // this is where I referred to for creating quicksort
    public int[] printArr(int array[], int n)
    {
        int i;
        for (i = 0; i < n; ++i) {
            System.out.print(array[i] + " ");
        }
        return array;
    }

    public int getCount() {
        int result = counter;
        counter = 0;
        return result;
    }

    public long getTime() {
        long time = timeEnd - timeStart;
        timeEnd = 0;
        timeStart = 0;
        return time;
    }

    public void checkSortedArray(int[] list) throws UnsortedException {
        if(list == null || list.length <= 1) {
            System.out.println("Array is sorted: null or 1 element");
        }

        for (int i = 0; i < list.length - 1; i++) {
            if (list[i] > list[i + 1]) {
                try {
                    for (int j = 0; i < list.length - 1; j++) {
                        System.out.println(" " + list[j]);
                    }
                    System.out.println("array is sorted");
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println("array index out of bounds");
                }
                throw new UnsortedException("The array was not sorted correctly: \n" +
                        list[i] + " at index " + i + " and " + list[i + 1] + " at index " + (i + 1));
            }
        }
    }
}