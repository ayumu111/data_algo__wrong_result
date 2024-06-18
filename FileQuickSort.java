import java.io.*;
import java.util.*;

public class FileQuickSort {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No files provided.");
            return;
        }

        // Loop to execute quicksort 10 times
        long totalExecutionTime = 0;
        for (int i = 0; i < 10; i++) {
            long startTime = System.currentTimeMillis();

            try {
                // Process each file in the list
                for (String filename : args) {
                    // Read the file into an array
                    int[] array = readArrayFromFile(filename);

                   // System.out.println("Execution " + (i + 1) + " sorting " + filename);
                    // Sort the array using quicksort
                    quickSort(array, 0, array.length - 1);

                    // Output the sorted array to a new file or console
                    outputSortedArray(filename, array);
                }
            } catch (IOException e) {
                System.err.println("Execution " + (i + 1) + " encountered an error processing files.");
                e.printStackTrace();
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            totalExecutionTime += executionTime;

            //System.out.println("Execution " + (i + 1) + " start: " + startTime + " [ms]");
           // System.out.println("Execution " + (i + 1) + " end: " + endTime + " [ms]");
            System.out.println("Execution " + (i + 1) + " duration: " + executionTime + " [ms]");
        }

        long averageExecutionTime = totalExecutionTime / 10;
        System.out.println("Average execution time: " + averageExecutionTime + " ms");
    }

    // Reads an array from a file
    private static int[] readArrayFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        ArrayList<Integer> list = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            list.add(Integer.parseInt(line));
        }

        reader.close();

        // Convert ArrayList to int array
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    // Outputs the sorted array to a new file or console
    private static void outputSortedArray(String filename, int[] array) throws IOException {
        String newFilename;
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex != -1) {
            newFilename = filename.substring(0, dotIndex) + "_sorted" + filename.substring(dotIndex);
        } else {
            newFilename = filename + "_sorted";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFilename))) {
            for (int num : array) {
                writer.write(Integer.toString(num));
                writer.newLine();
            }
        }
    }

    // Quicksort implementation
    private static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);

            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    // Partition function for quicksort
    private static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = (low - 1);
        for (int j = low; j <= high - 1; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }
}
