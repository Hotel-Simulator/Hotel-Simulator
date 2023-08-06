package pl.agh.edu.model.bank;

import pl.agh.edu.model.time.Time;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Deposit {
    private final BigDecimal placedValue;
    private final int period;

    private final int interestRate;
    private final LocalDateTime beginDate;
    private final LocalDateTime endDate;
    private final BigDecimal endValue;


    public Deposit(BigDecimal placedValue, int period) {
        this.placedValue = placedValue;
        this.period = period;

        this.interestRate = Bank.getInstance().getDepositInterestRate();
        this.beginDate = Time.getInstance().getTime();
        this.endDate = beginDate.plusMonths(period);
        BigDecimal profitPerMonth = this.placedValue.multiply(BigDecimal.valueOf(interestRate)).divide(BigDecimal.valueOf(12));
        this.endValue = profitPerMonth.multiply(BigDecimal.valueOf(period)).add(placedValue);
    }




}
