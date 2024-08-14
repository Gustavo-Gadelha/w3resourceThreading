package Exercise3;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class Main {
    private static final int ARRAY_SIZE = 8_000_000;
    private static final int TOTAL_THREADS = 8;

    public static void main(String[] args) {
        int[] array = createArray(ARRAY_SIZE);
        //System.out.println("Array before sorting: " + Arrays.toString(array));

        // Create a single thread pool to manage thread execution
        Thread[] threads = new Thread[TOTAL_THREADS];
        int segmentSize = Math.ceilDiv(ARRAY_SIZE, TOTAL_THREADS);

        Instant startTime = Instant.now();
        // Start each thread with its designated segment of the array
        System.out.printf("SEGMENTS: %d SIZE: %d\n", TOTAL_THREADS, segmentSize);
        for (int i = 0; i < TOTAL_THREADS; i++) {
            int start = i * segmentSize;
            int end = Math.min(start + segmentSize, ARRAY_SIZE);

            if (start < end) {
                System.out.printf("starting thread (%d) -> ", i);
                System.out.printf("START: %d END: %d\n", start, end);
                threads[i] = new Thread(() -> mergeSort(array, start, end));
                threads[i].start();
            }
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            if (thread == null) continue;

            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        int sortedSegments = (int) Arrays.stream(threads).filter(Objects::nonNull).count();
        mergeSegments(array, sortedSegments, segmentSize);

        // After all threads have completed sorting, print the sorted array
        Instant endTime = Instant.now();
        //System.out.println("Array after sorting : " + Arrays.toString(array));
        System.out.println(isSorted(array) ? "> ARRAY IS SORTED" : "> ARRAY IS NOT SORTED");
        System.out.printf("> Time: %dms", Duration.between(startTime, endTime).toMillis());
    }

    private static int[] createArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            // Generate random numbers between 0 and size
            array[i] = (int) (Math.random() * size);
        }
        return array;
    }

    private static void mergeSegments(int[] array, int sortedSegments, int segmentSize) {
        int remainingSegments = sortedSegments;
        int currentSize = segmentSize;

        while (remainingSegments > 1) {
            remainingSegments = Math.ceilDiv(remainingSegments, 2);
            currentSize *= 2;

            System.out.printf("SEGMENTS: %d SIZE: %d\n", remainingSegments, currentSize);
            for (int i = 0; i < remainingSegments; i++) {
                int start = i * currentSize;
                int middle = start + currentSize / 2;
                int end = Math.min(start + currentSize, array.length);

                if (middle < end) {
                    System.out.printf("merging segment (%d) -> ", i);
                    System.out.printf("START: %d MIDDLE: %d END: %d\n", start, middle, end);
                    merge(array, start, middle, end);
                } else {
                    break;
                }
            }

        }
    }

    private static void mergeSort(int[] array, int start, int end) {
        // Recursion stops when the range contains only one element
        if (end - start <= 1) return;
        int middle = Math.floorDiv(end + start, 2);
        mergeSort(array, start, middle);
        mergeSort(array, middle, end);
        merge(array, start, middle, end);
    }


    private static void merge(int[] array, int start, int middle, int end) {
        // System.out.printf("START: %d MIDDLE: %d END: %d\n", start, middle, end);
        int[] temp = new int[end - start];
        int tempIndex = 0;

        int leftIndex = start;
        int rightIndex = middle;

        do {
            if (array[leftIndex] < array[rightIndex]) {
                temp[tempIndex++] = array[leftIndex++];
            } else {
                temp[tempIndex++] = array[rightIndex++];
            }
        } while (leftIndex < middle && rightIndex < end);

        while (leftIndex < middle) {
            temp[tempIndex++] = array[leftIndex++];
        }

        while (rightIndex < end) {
            temp[tempIndex++] = array[rightIndex++];
        }

        System.arraycopy(temp, 0, array, start, temp.length);
    }

    private static boolean isSorted(int[] array) {
        return IntStream.range(0, array.length - 1).noneMatch(i -> array[i] > array[i + 1]);
    }
}