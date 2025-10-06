package Multithreading;

class Account extends Thread{
    private int balance=1000;
    public void withdraw(int amount, String name){
        if(balance>=amount && balance>=0){
            System.out.println(name+"is withdrawing "+amount);
            balance-=amount;
            System.out.println("Balance : "+balance);
        }
        else{
            System.out.println("Insufficient Balance...");
        }
    }
}

public class BankAccount {
    public static void main(String args[]){
        Account acc=new Account();

        Thread t1=new Thread(()->acc.withdraw(800,"Thread 1"));
        Thread t2=new Thread(()->acc.withdraw(700,"Thread 2"));

        t1.start();
        t2.start();
    }
}
