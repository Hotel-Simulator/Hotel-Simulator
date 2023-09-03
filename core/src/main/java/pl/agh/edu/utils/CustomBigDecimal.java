package pl.agh.edu.utils;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class CustomBigDecimal{
    private BigDecimal value;
    private MathContext mc = new MathContext(100,RoundingMode.HALF_UP);

    public CustomBigDecimal(String val) {
        this.value = new BigDecimal(val);
    }
    public CustomBigDecimal(BigDecimal bigDecimal){
        this.value = bigDecimal;
    }

    public CustomBigDecimal(float val) {
        this.value = new BigDecimal(val, mc);
    }

    public CustomBigDecimal add(CustomBigDecimal other) {
        return new CustomBigDecimal(this.value.add(other.value));
    }

    public CustomBigDecimal subtract(CustomBigDecimal other) {
        return new CustomBigDecimal(this.value.subtract(other.value));
    }

    public CustomBigDecimal multiply(CustomBigDecimal other) {
        return new CustomBigDecimal(this.value.multiply(other.value));
    }

    public CustomBigDecimal divide(CustomBigDecimal other) {
        if (other.value.equals(BigDecimal.ZERO)) {
            throw new ArithmeticException("Division by zero");
        }
        return new CustomBigDecimal(this.value.divide(other.value, mc));
    }

    public int compareTo(CustomBigDecimal other) {
        return this.value.compareTo(other.value);
    }

    public CustomBigDecimal pow(int n) {
        return new CustomBigDecimal(this.value.pow(n, mc));
    }

    public CustomBigDecimal log() {
        return new CustomBigDecimal(BigDecimalMath.log(this.value,mc));
    }


    @Override
    public String toString(){
        int zeros = BigDecimalMath.log10(this.value,mc).intValue();
        int triZeros = zeros/3;
        Prefix prefix = Prefix.getPrefixByValue(BigDecimalMath.pow(BigDecimal.valueOf(1000),BigDecimal.valueOf(triZeros),mc));
//        System.out.println("pref"+prefix);
        String number = value.toString().split("\\.")[0];
//        System.out.println("NUMBER "+number);
        int tmp = prefix.getValue().toString().length();
//        System.out.println("tmp"+tmp);
        String beforeComa = number.substring(0,number.length()-tmp+1);
        String result = beforeComa;
//        System.out.println("beforeComa "+beforeComa);
        if(prefix.getValue().compareTo(BigDecimal.ONE)>0){
            String afterComa = number.substring(number.length()-tmp+1,number.length());
//            System.out.println("after coma "+afterComa);
            result += beforeComa.length() >=3 ? "" : ("." + afterComa.substring(0, 3 - Math.min(3, beforeComa.length())));
            result+= prefix.name();
        }
        return result;
    }

    public void roundPrefix(){

    }


    public enum Prefix {
        n(BigDecimal.valueOf(1)),
        k(BigDecimal.valueOf(1000)),
        M(BigDecimal.valueOf(1000000)),
        B(BigDecimal.valueOf(1000000000)),
        T(new BigDecimal("1000000000000"));


        private final BigDecimal value;

        public static Prefix getPrefixByValue(BigDecimal value) {
            for (Prefix prefix : Prefix.values()) {
                if (value.equals(prefix.getValue()))
                    return prefix;
            }
            return Prefix.T;


        }
        Prefix(BigDecimal value){
            this.value = value;
        }
        public BigDecimal getValue() {
            return value;
        }
    }
}
