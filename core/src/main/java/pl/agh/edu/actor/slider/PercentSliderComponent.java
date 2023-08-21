package pl.agh.edu.actor.slider;

public class PercentSliderComponent extends SliderComponent{
    public PercentSliderComponent(String name, String suffix, SliderSize sliderSize) {
        super(name, "%", 0, 100, 1, sliderSize);
    }

}

