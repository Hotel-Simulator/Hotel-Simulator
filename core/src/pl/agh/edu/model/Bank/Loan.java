package pl.agh.edu.model.Bank;

import pl.agh.edu.model.Bank.Bank;
import pl.agh.edu.time.Time;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public class Loan {
    private final BigDecimal loanValue; // value of loan that player gets



    private final BigDecimal loanValueToPay; // value of loan that player pays
    private final int period; // months to pay

    private BigDecimal paidValue; // amount that already has been paid
    private final LocalDateTime beginDate;
    private final LocalDateTime endDate;
    private final int interestRate; // Bank's interest rate for loans
    private final BigDecimal monthlyPayments; // Amount that player has to pay each month
    private boolean isPaid = false;


    public Loan(BigDecimal loanValue,int period){
        this.beginDate = Time.getInstance().getTime();
        this.endDate = beginDate.plusMonths(period);
        this.period = period;
        this.interestRate = Bank.getInstance().getInterestRate();
        this.loanValue = loanValue;
        this.loanValueToPay = loanValue.multiply(BigDecimal.valueOf(100+interestRate)).divide(BigDecimal.valueOf(100), RoundingMode.CEILING);
        this.monthlyPayments = this.loanValueToPay.divideToIntegralValue(BigDecimal.valueOf(period));
    }

    public Loan(int loanValue, int period){ // int version for convenience
        this(new BigDecimal(loanValue),period);
    }

    public void payMonth(){
        paidValue.add(monthlyPayments);
        if(paidValue.compareTo(loanValueToPay)==0){
            isPaid = true;
        }
    }
    public void payAll(){
        paidValue = loanValueToPay;
        isPaid = true;
    }

    public int getMonthsLeft(){
        LocalDateTime curr = Time.getInstance().getTime();
        Period diff = Period.between(curr.toLocalDate(),endDate.toLocalDate());
        return diff.getMonths();
    }
    public BigDecimal getLoanValue(){
        return loanValue;
    }


    public BigDecimal getMonthlyPayments() {

        return monthlyPayments;
    }

    public BigDecimal getPaidValue() {
        return paidValue;
    }
    public BigDecimal getLoanValueToPay() {
        return loanValueToPay;
    }

    public int getPeriod() {
        return period;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getInterestRate() {
        return interestRate;
    }

    public static void main(String args[]){
        Bank b = Bank.getInstance().setInterestRate(10);
        Time.getInstance();
        Loan l = new Loan(100,2);
        System.out.println(Time.getInstance().getTime());

        System.out.println(Time.getInstance().getTime());

        System.out.println(l.getLoanValue());
        System.out.println(l.getLoanValueToPay());
        System.out.println(l.getMonthsLeft());
        System.out.println(l.getMonthlyPayments());
        System.out.println(l.getEndDate());


    }
}
