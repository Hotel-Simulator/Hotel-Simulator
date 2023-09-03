package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.slider.MoneySliderComponent;
import pl.agh.edu.actor.slider.SliderSize;

import java.math.BigDecimal;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		this.add(new Label(name, HotelSkin.getInstance()));
		this.add(new MoneySliderComponent("Tax fraud", BigDecimal.valueOf(1), BigDecimal.valueOf(100000000), SliderSize.LARGE));
	}
}
