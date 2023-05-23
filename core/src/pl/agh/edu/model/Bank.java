package pl.agh.edu.model;

import java.math.BigDecimal;

public class Bank {
    private static volatile Bank instance = null;
    private BigDecimal balance = new BigDecimal(0);


    public static Bank getInstance(){
        if(instance == null){
            synchronized (Bank.class){
                if(instance == null){
                    instance = new Bank();
                }
            }
        }
        return instance;
    }
    private Bank(){}


    public BigDecimal getBalance() {
        return balance;
    }

    public String printBalance(){return balance.toString()+".00$";}

    public void addBalance(int value) {this.balance.add(BigDecimal.valueOf(value));}
    public void addBalance(BigDecimal value){this.balance.add(value);}
    public void chargeBalance(int value){this.balance.subtract(BigDecimal.valueOf(value));}
    public void chargeBalance(BigDecimal value){this.balance.subtract(value);}

    public boolean operationAbility(double d){
        if(balance.compareTo(BigDecimal.valueOf(d))<0){
            return false;
        }
        return true;
    }



}
