package pl.agh.edu.model.bank;

import pl.agh.edu.model.Time;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Bank {
    private static volatile Bank instance = null;
    private BigDecimal balance = new BigDecimal(0);

    private List credits = new LinkedList();

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

    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }


    private Bank(){
        nextAccountFeeCharge = Time.getInstance().getTime().plusMonths(1);
    }

    public void setAccountFee(int fee){
        this.accountFee.add(BigDecimal.valueOf(fee));
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
            obtainCredit(BigDecimal.valueOf(100000),12);
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


    public Credit obtainCredit(BigDecimal value, int period){
        Credit credit = new Credit(value,period);
        credits.add(credit);
        addBalance(credit.getCreditValue()); // automatically adds value to balance
        return credit;
    }



}
