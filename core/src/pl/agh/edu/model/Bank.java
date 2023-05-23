package pl.agh.edu.model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Bank {
    private static volatile Bank instance = null;
    private BigDecimal balance = new BigDecimal(0);

    private List loans = new LinkedList();

    public int getInterestRate() {
        return interestRate;
    }

    private int interestRate;


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

    public Bank setInterestRate(int interestRate){
        this.interestRate = interestRate;
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


    public Loan obtainLoan(BigDecimal value,int period){
        Loan loan = new Loan(value,period);
        return loan;
    }



}
