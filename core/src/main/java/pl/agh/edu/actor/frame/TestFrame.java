package pl.agh.edu.actor.frame;

import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.slider.MoneySliderComponent;
import pl.agh.edu.actor.utils.Size;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();
		root.add(new MoneySliderComponent("Tax", new BigDecimal("0"), new BigDecimal("1000000000000000000000000000000"), Size.LARGE));
		root.row();

		this.add(root);
	}
}
