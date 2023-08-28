package pl.agh.edu.actor.slider;

import java.math.BigDecimal;

public enum Prefix {
    n(BigDecimal.valueOf(1)),
    k(BigDecimal.valueOf(1000)),
    M(BigDecimal.valueOf(1000000)),
    B(BigDecimal.valueOf(1000000000)),
    T(new BigDecimal("1 000 000 000 000"));
    private final BigDecimal value;

    Prefix(BigDecimal value){
        this.value = value;
    }


}