package Exercise1;

public class Main {
    public static void main(String[] args) {
        Thread helloWorld = new Thread(() -> System.out.println("Hello, World!"));
        helloWorld.start();
    }
}