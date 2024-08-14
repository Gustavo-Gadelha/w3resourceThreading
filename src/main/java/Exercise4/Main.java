package Exercise4;

public class Main {
    private static final int MATRIX_SIZE = 3;
    private static final int TOTAL_THREADS = 2;

    public static void main(String[] args) {
        int[][] matrix1 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] matrix2 = {
                {9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}
        };

        int[][] result = new int[MATRIX_SIZE][MATRIX_SIZE];

        Thread[] threads = new Thread[TOTAL_THREADS];
        int segmentSize = MATRIX_SIZE / TOTAL_THREADS;

        for (int i = 0; i < TOTAL_THREADS; i++) {
            int startIndex = i * segmentSize;
            int endIndex = (i == TOTAL_THREADS - 1) ? MATRIX_SIZE - 1 : (startIndex + segmentSize - 1);
            threads[i] = new Thread(new MultiplicationTask(matrix1, matrix2, result, startIndex, endIndex));
            threads[i].start();
        }

        for (Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Print the result matrix
        System.out.println("Result:");
        for (int[] row: result) {
            for (int element: row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    static class MultiplicationTask implements Runnable {
        private final int[][] matrix1;
        private final int[][] matrix2;
        private final int[][] result;
        private final int startIndex;
        private final int endIndex;

        public MultiplicationTask(int[][] matrix1, int[][] matrix2, int[][] result, int startIndex, int endIndex) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.result = result;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            int cols = matrix2[0].length;

            for (int i = startIndex; i <= endIndex; i++) {
                for (int j = 0; j < cols; j++) {
                    for (int k = 0; k < MATRIX_SIZE; k++) {
                        result[i][j] += matrix1[i][k] * matrix2[k][j];
                    }
                }
            }
        }
    }
}