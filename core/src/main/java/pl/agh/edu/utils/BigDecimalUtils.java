package pl.agh.edu.utils;

public class BigDecimalUtils {
    public static String customToString(Float value) {
        String displayedValue;
        String decimalPart;
        if(Math.floor(value) == value){
            decimalPart = "0";
        }
        else{
            decimalPart = "1";
        }
        if (value < 1000) {
            displayedValue = String.format("%."+decimalPart+"f", value);
        } else if (value < 1000000) {
            displayedValue = String.format("%."+decimalPart+"f", value / 1000);
        } else {
            displayedValue = String.format("%."+decimalPart+"f", value / 1000000);
        }
        return displayedValue;
    }
}
