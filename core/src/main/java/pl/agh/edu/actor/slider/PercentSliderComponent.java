package pl.agh.edu.actor.slider;

public class PercentSliderComponent extends SliderComponent {
	public PercentSliderComponent(String name, SliderSize sliderSize) {
		super(name, "%", 0f, 100f, 1f, sliderSize);
		setField();
	}

}
