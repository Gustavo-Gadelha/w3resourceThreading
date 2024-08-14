package Exercise5;

public class PrimeSumRunnable implements Runnable {
    private final int start;
    private final int end;
    private long sum;

    public PrimeSumRunnable(int start, int end) {
        this.start = start;
        this.end = end;
        this.sum = 0;
    }

    private static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        } else if (number == 2) {
            return true;
        } else if (number % 2 == 0) {
            return false;
        }

        for (int i = 3; i <= Math.sqrt(number); i += 2) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void run() {
        int number = start;

        if (number < 2) {
            this.sum += 2;
            number = 3;
        } else if (number % 2 == 0) {
            number++;
        }

        for (; number < this.end; number += 2) {
            if (PrimeSumRunnable.isPrime(number)) {
                this.sum += number;
            }
        }
    }

    public long getSum() {
        return this.sum;
    }
}
