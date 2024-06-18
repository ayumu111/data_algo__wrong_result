import java.io.*;
import java.util.*;

public class FileRadixSort {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No files provided.");
            return;
        }

        // Create an array to hold the threads
        RadixSortThread[] threads = new RadixSortThread[10];

        // Create and start 10 threads, each with a unique identifier
        for (int i = 0; i < 10; i++) {
            threads[i] = new RadixSortThread(args, i);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < 10; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Print the execution times for each thread and calculate the average
        long totalExecutionTime = 0;
        for (int i = 0; i < 10; i++) {
            long executionTime = threads[i].getExecutionTime();
            System.out.println("Thread " + threads[i].getThreadNumber() + " execution time: " + executionTime + " ms");
            totalExecutionTime += executionTime;
        }
        long averageExecutionTime = totalExecutionTime / 10;
        System.out.println("Average execution time: " + averageExecutionTime + " ms");
    }

    // A thread class for sorting files
    static class RadixSortThread extends Thread {
        private String[] filenames;
        private int threadNumber;
        private long executionTime;

        public RadixSortThread(String[] filenames, int threadNumber) {
            this.filenames = filenames;
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            try {
                // Process each file in the list
                for (String filename : filenames) {
                    // Read the file into an array
                    int[] array = readArrayFromFile(filename);

                    System.out.println("Thread " + threadNumber + " sorting " + filename);

                    // Sort the array using radix sort
                    radixSort(array);

                    // Output the sorted array to a new file or console
                    outputSortedArray(filename, array);
                }
            } catch (IOException e) {
                System.err.println("Thread " + threadNumber + " encountered an error processing files.");
                e.printStackTrace();
            }

            long endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;
            System.out.println("Thread " + threadNumber + " start: " + startTime + " [ms]");
            System.out.println("Thread " + threadNumber + " end: " + endTime + " [ms]");
            System.out.println("Thread " + threadNumber + " duration: " + executionTime + " [ms]");
        }

        // Reads an array from a file
        private int[] readArrayFromFile(String filename) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            ArrayList<Integer> list = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                list.add(Integer.parseInt(line));
            }

            reader.close();

            int[] array = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }

            return array;
        }

        // Outputs the sorted array to a new file or console
        private void outputSortedArray(String filename, int[] array) throws IOException {
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

        public int getThreadNumber() {
            return threadNumber;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        // Radix sort algorithm
        public static void radixSort(int[] array) {
            int max = getMax(array);
            for (int exp = 1; max / exp > 0; exp *= 10) {
                countSort(array, exp);
            }
        }

        // Counting sort algorithm used by radix sort
        public static void countSort(int[] array, int exp) {
            int n = array.length;
            int[] output = new int[n];
            int[] count = new int[10];

            // Count occurrences of digits
            for (int i = 0; i < n; i++) {
                count[(array[i] / exp) % 10]++;
            }

            // Update count array to contain actual positions
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }

            // Build output array
            for (int i = n - 1; i >= 0; i--) {
                int digit = (array[i] / exp) % 10;
                output[count[digit] - 1] = array[i];
                count[digit]--;
            }

            // Copy output array to original array
            System.arraycopy(output, 0, array, 0, n);
        }

        // Get the maximum value in the array
        public static int getMax(int[] array) {
            int max = array[0];
            for (int value : array) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }

        // Print the array contents
        public static void printArray(int[] array) {
            for (int i : array) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}
