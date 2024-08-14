package Exercise7;

public class Main {
    public static void main(String[] args) {
        BankAccount account1 = new BankAccount("Jonathan");
        BankAccount account2 = new BankAccount("Martha");

        Thread deposit1Account1 = new Thread(() -> account1.deposit(3775));
        Thread deposit2Account1 = new Thread(() -> account1.deposit(3225));
        Thread withdraw1Account1 = new Thread(() -> account1.withdraw(1000));
        Thread transferTo1Account1 = new Thread(() -> account1.transferTo(account2, 1000));

        Thread deposit1Account2 = new Thread(() -> account2.deposit(225));
        Thread deposit2Account2 = new Thread(() -> account2.deposit(775));
        Thread withdraw1Account2 = new Thread(() -> account2.withdraw(100));
        Thread transferTo1Account2 = new Thread(() -> account2.transferTo(account1, 100));

        deposit1Account1.start();
        deposit2Account1.start();
        withdraw1Account1.start();
        transferTo1Account1.start();

        deposit1Account2.start();
        deposit2Account2.start();
        withdraw1Account2.start();
        transferTo1Account2.start();
    }
}