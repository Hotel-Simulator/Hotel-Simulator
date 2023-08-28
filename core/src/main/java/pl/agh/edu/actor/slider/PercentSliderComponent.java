package pl.agh.edu.actor.slider;

import java.math.BigDecimal;

public class PercentSliderComponent extends SliderComponent{
    public PercentSliderComponent(String name, SliderSize sliderSize) {
        super(name, "%", BigDecimal.ZERO, BigDecimal.valueOf(100), BigDecimal.ONE, sliderSize);
    }

}

