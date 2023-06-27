package pl.agh.edu.model.bank;

import pl.agh.edu.model.Time;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Period;

//TODO charge monthlyPayment each month (Time observer?)
public class Credit {
    private final BigDecimal creditValue; // value of credit that player gets
    private final BigDecimal creditValueToPay; // value of credit that player pays
    private final int period; // months to pay
    private BigDecimal paidValue; // amount that already has been paid
    private final LocalDateTime beginDate;
    private final LocalDateTime endDate;
    private final int interestRate; // Bank's interest rate for credits
    private final BigDecimal monthlyPayments; // Amount that player has to pay each month
    private boolean isPaid = false;
    private LocalDateTime nextPaymentDate;


    public Credit(BigDecimal creditValue, int period){
        this.beginDate = Time.getInstance().getTime();
        this.endDate = beginDate.plusMonths(period);
        this.period = period;
        this.interestRate = Bank.getInstance().getInterestRate();
        this.creditValue = creditValue;
        this.creditValueToPay = creditValue.multiply(BigDecimal.valueOf(100+interestRate)).divide(BigDecimal.valueOf(100), RoundingMode.CEILING);
        this.monthlyPayments = this.creditValueToPay.divideToIntegralValue(BigDecimal.valueOf(period));
        this.nextPaymentDate = beginDate.plusMonths(1);
    }

    public Credit(int creditValue, int period){ // int version for convenience
        this(new BigDecimal(creditValue),period);
    }

    public void payMonth(){
        paidValue.add(monthlyPayments);
        nextPaymentDate = nextPaymentDate.plusMonths(1);
        if(paidValue.compareTo(creditValueToPay)==0){
            isPaid = true;
            nextPaymentDate = null;
        }
    }
    public void payAll(){
        paidValue = creditValueToPay;
        isPaid = true;
        nextPaymentDate = null;
    }

    public int getMonthsLeft(){
        LocalDateTime curr = Time.getInstance().getTime();
        Period diff = Period.between(curr.toLocalDate(),endDate.toLocalDate());
        return diff.getMonths();
    }
    public BigDecimal getCreditValue(){
        return creditValue;
    }


    public BigDecimal getMonthlyPayments() {

        return monthlyPayments;
    }

    public BigDecimal getPaidValue() {
        return paidValue;
    }
    public BigDecimal getCreditValueToPay() {
        return creditValueToPay;
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
        Bank b = Bank.getInstance();
        b.setInterestRate(10);
        Time.getInstance();
        Credit l = new Credit(100,2);
        System.out.println(Time.getInstance().getTime());

        System.out.println(Time.getInstance().getTime());

        System.out.println(l.getCreditValue());
        System.out.println(l.getCreditValueToPay());
        System.out.println(l.getMonthsLeft());
        System.out.println(l.getMonthlyPayments());
        System.out.println(l.getEndDate());


    }
}
