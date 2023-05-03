package pl.agh.edu.model;

import java.math.BigDecimal;

public class Bank {
    private int balance = 0;

    public int getBalance() {
        return balance;
    }

    public String printBalance(){
        return Integer.toString(balance)+".00$";
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean operationAbility(double d){
        if(balance<d){
            return false;
        }
        return true;
    }

}
