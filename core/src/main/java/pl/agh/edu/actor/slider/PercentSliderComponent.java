package pl.agh.edu.actor.slider;

import pl.agh.edu.actor.utils.Size;

public class PercentSliderComponent extends SliderComponent {
	public PercentSliderComponent(String name, Size size) {
		super(name, "%", 0f, 100f, 1f, size);
		setField();
	}

}
