package Exercise2;

public class Main {
    private static final int MAX_NUMBER = 2000;
    private static final Object lock = new Object();
    private static boolean oddTurn = false;

    public static void main(String[] args) {
        Thread evenThread = new Thread(() -> {
            for (int i = 0; i <= MAX_NUMBER; i += 2) {
                synchronized (lock) {
                    if (oddTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    System.out.println("EVEN: " + i);
                    Main.oddTurn = true;
                    lock.notify();
                }
            }
        });

        Thread oddThread = new Thread(() -> {
            for (int i = 1; i <= MAX_NUMBER; i += 2) {
                synchronized (lock) {
                    if (!oddTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    System.out.println("ODD : " + i);
                    Main.oddTurn = false;
                    lock.notify();
                }
            }
        });

        evenThread.start();
        oddThread.start();
    }
}

