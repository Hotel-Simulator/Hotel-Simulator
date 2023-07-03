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
    private List deposits = new LinkedList();
    private int creditInterestRate;

    private int depositInterestRate;
    private BigDecimal accountFee;
    private LocalDateTime nextAccountFeeCharge;
    private List<Transaction> incomes = new LinkedList<>();
    private List<Transaction> expenses = new LinkedList<>();
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


    private Bank(){
        nextAccountFeeCharge = Time.getInstance().getTime().plusMonths(1);
    }

    public void chargeAccountFee(){
        chargeBalance(accountFee);
        nextAccountFeeCharge = nextAccountFeeCharge.plusMonths(1);
    }

    public String printBalance(){return balance.toString()+".00$";}


    public void addBalance(BigDecimal value){
        this.balance.add(value);
        this.incomes.add(new Transaction(Time.getInstance().getTime(),value));
    }

    public void chargeBalance(BigDecimal value){
        this.balance.subtract(value);
        this.expenses.add(new Transaction(Time.getInstance().getTime(),value));
    }

    public boolean operationAbility(BigDecimal value){
        if(balance.compareTo(value)<0){
            return false;
        }
        return true;
    }


    public void obtainCredit(BigDecimal value, int period){
        Credit credit = new Credit(value,period);
        credits.add(credit);
        addBalance(value); // automatically adds value to balance
    }

    public void placeDeposit(BigDecimal value, int period){
        Deposit deposit = new Deposit(value,period);
        deposits.add(deposit);
        chargeBalance(value);
    }


    public void setAccountFee(int fee){
        this.accountFee.add(BigDecimal.valueOf(fee));
    }

    public void setCreditInterestRate(int interestRate) {
        this.creditInterestRate = interestRate;
    }
    public void setDepositsInterestRate(int interestRate) {
        this.creditInterestRate = interestRate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getCreditInterestRate() {
        return creditInterestRate;
    }
    public int getDepositInterestRate() {
        return depositInterestRate;
    }




}
