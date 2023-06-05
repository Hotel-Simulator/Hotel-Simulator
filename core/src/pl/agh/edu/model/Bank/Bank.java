package pl.agh.edu.model.Bank;

import pl.agh.edu.time.Time;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private BigDecimal accountFee;
    private LocalDateTime nextAccountFeeCharge;


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

    public Bank setInterestRate(int interestRate) {
        this.interestRate = interestRate;
        return instance;
    }


    private Bank(){
        nextAccountFeeCharge = Time.getInstance().getTime().plusMonths(1);
    }

    public Bank setAccountFee(int fee){
        this.accountFee.add(BigDecimal.valueOf(fee));
        return instance;
    }
    public void chargeAccountFee(){
        chargeBalance(accountFee);
        nextAccountFeeCharge = nextAccountFeeCharge.plusMonths(1);
    }




    public BigDecimal getBalance() {
        return balance;
    }

    public String printBalance(){return balance.toString()+".00$";}

    public void addBalance(int value) {this.balance.add(BigDecimal.valueOf(value));}
    public void addBalance(BigDecimal value){this.balance.add(value);}
    public void chargeBalance(int value){
        if(operationAbility(value))
            this.balance.subtract(BigDecimal.valueOf(value));
        else{
            obtainLoan(BigDecimal.valueOf(100000),12);
        }
    }
    public void chargeBalance(BigDecimal value){
        this.balance.subtract(value);
    }

    public boolean operationAbility(double d){
        if(balance.compareTo(BigDecimal.valueOf(d))<0){
            return false;
        }
        return true;
    }


    public Loan obtainLoan(BigDecimal value, int period){
        Loan loan = new Loan(value,period);
        loans.add(loan);
        addBalance(loan.getLoanValue()); // automatically adds value to balance
        return loan;
    }



}
