package pl.agh.edu.model;

import pl.agh.edu.time.Time;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class Loan {
    private final BigDecimal value;
    private final int period;

    private BigDecimal paidValue;
    private final LocalDateTime beginDate;
    private final LocalDateTime endDate;
    private final int interestRate;
    private final BigDecimal monthlyPayments;


    public Loan(BigDecimal value,int period){
        this.beginDate = Time.getInstance().getTime();
        this.endDate = beginDate.plusMonths(period);
        this.value = value;
        this.period = period;
        this.interestRate = Bank.getInstance().getInterestRate();
        this.monthlyPayments = value.divideToIntegralValue(BigDecimal.valueOf(period));

    }

    public int getMonthsLeft(){
        LocalDateTime curr = Time.getInstance().getTime();
        Duration.between(endDate,curr);

        //TODO odjąć curr od this.endDate i zwrócić miesiące
        return -1;
    }
    public BigDecimal getValue(){
        return value;
    }


    public BigDecimal getMonthlyPayments() {

        return monthlyPayments;
    }

    public BigDecimal getPaidValue() {
        return paidValue;
    }
}
