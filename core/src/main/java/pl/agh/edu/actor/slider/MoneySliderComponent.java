package pl.agh.edu.actor.slider;

import java.math.BigDecimal;

public class MoneySliderComponent extends SliderComponent{
        public MoneySliderComponent(String name, BigDecimal min, BigDecimal max, SliderSize sliderSize) {
        super(name, "$", min, max, BigDecimal.ONE, sliderSize);

    }

    @Override
    protected void setField() {
        BigDecimal value = getValue();
//        slider.setStepSize(1);
//        if(value/1000>1){
//            slider.setStepSize(100);
//        }
//        if(value/1000000>1){
//            slider.setStepSize(100000);
//        }
//        String displayedValue;
//        if (value < 1000) {
//            displayedValue = String.format("%.0f", value);
//        } else if (value < 1000000) {
//            displayedValue = String.format("%.1fk", value / 1000);
//        } else {
//            displayedValue = String.format("%.1fm", value / 1000000);
//        }
        valueLabel.setText(value + suffix);
    }
}
