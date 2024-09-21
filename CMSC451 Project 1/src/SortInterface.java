public interface SortInterface {

    int[] iterativeSort(int[] array, int low, int high) throws UnsortedException;
    int[] recursiveSort(int[] array, int low, int high) throws UnsortedException;

    int getCount();
    long getTime();
}
