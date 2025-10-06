package Proxy;

interface Account {
    void withdraw(String userRole, double amount); 
}

class realBank implements Account{
    private double balance = 1000;

    public void withdraw(String userRole, double amount){
        if(balance>=amount){
            balance-=amount;
            System.out.println("Withdrawn : " + amount +"Balance : "+balance);
        } else {
            System.out.println("Insufficient Balance !");
        }
    }
}

class AccountProxy implements Account {
    private realBank Bank;

    public AccountProxy(realBank Bank){
        this.Bank = Bank;
    }
    public void withdraw(String userRole, double amount){
        if(!userRole.equals("ADMIN")){
            System.out.println("Access Denied! only ADMIN can withdraw...");
            return;
        }
        Bank.withdraw(userRole, amount);
    }
}

public class StaticProxyBank {
    public static void main(String args[]){
        Account account = new AccountProxy(new realBank());
        account.withdraw("USER",500);
        account.withdraw("ADMIN", 100);
    }
}
