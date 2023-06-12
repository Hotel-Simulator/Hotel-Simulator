package pl.agh.edu.model.bank;

import pl.agh.edu.model.Time;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Period;

//TODO charge monthlyPayment each month (Time observer?)
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
    private LocalDateTime nextPaymentDate;


    public Loan(BigDecimal loanValue,int period){
        this.beginDate = Time.getInstance().getTime();
        this.endDate = beginDate.plusMonths(period);
        this.period = period;
        this.interestRate = Bank.getInstance().getInterestRate();
        this.loanValue = loanValue;
        this.loanValueToPay = loanValue.multiply(BigDecimal.valueOf(100+interestRate)).divide(BigDecimal.valueOf(100), RoundingMode.CEILING);
        this.monthlyPayments = this.loanValueToPay.divideToIntegralValue(BigDecimal.valueOf(period));
        this.nextPaymentDate = beginDate.plusMonths(1);
    }

    public Loan(int loanValue, int period){ // int version for convenience
        this(new BigDecimal(loanValue),period);
    }

    public void payMonth(){
        paidValue.add(monthlyPayments);
        nextPaymentDate = nextPaymentDate.plusMonths(1);
        if(paidValue.compareTo(loanValueToPay)==0){
            isPaid = true;
            nextPaymentDate = null;
        }
    }
    public void payAll(){
        paidValue = loanValueToPay;
        isPaid = true;
        nextPaymentDate = null;
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
    public boolean isPaid(){return isPaid; }
    public LocalDateTime getNextPaymentDate(){return nextPaymentDate;}

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
