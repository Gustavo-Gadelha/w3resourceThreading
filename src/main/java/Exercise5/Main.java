package Exercise5;


import java.time.Duration;
import java.time.Instant;

public class Main {
    private static final int TOTAL_THREADS = 8;
    private static final int LIMIT = 8_000_000;

    public static void main(String[] args) {
        long result = 0;

        Thread[] threads = new Thread[TOTAL_THREADS];
        PrimeSumRunnable[] tasks = new PrimeSumRunnable[TOTAL_THREADS];
        int segmentSize = Math.ceilDiv(LIMIT, TOTAL_THREADS);

        Instant startTime = Instant.now();
        for (int i = 0; i < threads.length; i++) {
            int start = i * segmentSize;
            int end = Math.min(start + segmentSize, LIMIT);
            tasks[i] = new PrimeSumRunnable(start, end);

            if (start < end) {
                threads[i] = new Thread(tasks[i]);
                threads[i].start();
            }
        }

        // Wait for all threads to complete
        for (int i = 0; i < TOTAL_THREADS; i++) {
            if (threads[i] == null) continue;

            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            result += tasks[i].getSum();
        }

        Instant endTime = Instant.now();
        System.out.printf("SUM OF ALL PRIMES UP TO %d: %d\n", LIMIT, result);
        System.out.printf("DURATION: %dms", Duration.between(startTime, endTime).toMillis());
    }
}