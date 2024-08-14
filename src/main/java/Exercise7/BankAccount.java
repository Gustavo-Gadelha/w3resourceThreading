package Exercise7;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private String name;
    private double balance;
    private final ReentrantLock lock;

    public BankAccount(String name) {
        this.name = name;
        this.balance = 0.0;
        this.lock = new ReentrantLock();
    }

    public boolean deposit(double amount) {
        boolean status = false;

        if (amount < 0) {
            throw new RuntimeException("Deposit amount below 0");
        }

        lock.lock();
        try {
            this.balance += amount;
            System.out.printf("ACCOUNT NAME: %s -> CURRENT BALANCE: %,.2f AFTER DEPOSIT OF: $%,.2f\n", this.name, this.balance, amount);
            status = true;
        } finally {
            lock.unlock();
        }

        return status;
    }

    public boolean withdraw(double amount) {
        boolean status = false;

        if (amount < 0) {
            throw new RuntimeException("Withdraw amount below 0");
        }

        lock.lock();
        try {
            if (amount <= this.balance) {
                this.balance -= amount;
                System.out.printf("ACCOUNT NAME: %s -> CURRENT BALANCE: %,.2f AFTER WITHDRAW OF: $%,.2f\n", this.name, this.balance, amount);
                status = true;
            } else {
                System.out.println("Withdraw amount greater than available balance, withdraw rejected");
                status = false;
            }
        } finally {
            lock.unlock();
        }

        return status;
    }

    public boolean transferTo(BankAccount account, double amount) {
        boolean status = false;

        if (this == account) {
            throw new RuntimeException("Account cannot transfer to itself");
        }

        if (amount < 0) {
            throw new RuntimeException("Amount to transfer amount below 0");
        }

        if (this.withdraw(amount)) {
            account.deposit(amount);
            System.out.printf("TRANSFER OF %,.2f TO \"%s\"\n", amount, account.name);
            status = true;
        } else {
            System.out.printf("TRANSFER OF \"%s\" TO \"%s\" REJECTED\n", this.name, account.name);
            status = false;
        }

        return status;
    }
}
