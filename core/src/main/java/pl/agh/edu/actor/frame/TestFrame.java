package pl.agh.edu.actor.frame;

import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.slider.MoneySliderComponent;
import pl.agh.edu.actor.slider.PercentSliderComponent;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		root.add(new MoneySliderComponent(
				"Tax",
				new BigDecimal("0"),
				new BigDecimal("1000000000000000000000000000000"),
				selectedOption -> {
					System.out.println("Selected: " + selectedOption);
					return null;
				}));

		root.row();

		root.add(new PercentSliderComponent(
				"Tax",
				selectedOption -> {
					System.out.println("Selected: " + selectedOption);
					return null;
				}));

		this.add(root);
	}
}
