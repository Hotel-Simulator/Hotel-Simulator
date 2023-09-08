package pl.agh.edu.actor.frame;

import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.slider.MoneySliderComponent;
import pl.agh.edu.actor.slider.SliderSize;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();
		root.add(new MoneySliderComponent("Tax", new BigDecimal("1"), new BigDecimal("100"), SliderSize.LARGE));
		root.row();

		this.add(root);
	}
}
