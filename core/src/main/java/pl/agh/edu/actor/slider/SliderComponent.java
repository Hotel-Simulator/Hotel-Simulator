package pl.agh.edu.actor.slider;

import java.util.function.Function;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;

public class SliderComponent extends Table {
	private int SLIDER_WIDTH = 600;
	private int HORIZONTAL_PADDING = 20;
	private Label valueLabel;
	private Slider slider;
	private Skin skin;
	private String suffix;
	private Function<Float, Float> mapping;

	public SliderComponent(String name, String suffix, float min, float max, float step, Function<Float, Float> mapping) {
		Stack stack = new Stack();
		add(stack);
		skin = HotelSkin.getInstance();
		NinePatch texture = skin.getPatch("textfield-background");
		stack.add(new Image(texture));
		this.mapping = mapping;
		this.suffix = suffix;
		stack.add(new SliderRowTable(name, min, max, step));
	}

	public class SliderRowTable extends Table {

		public SliderRowTable(String name, float min, float max, float step) {
			Label nameLabel = new Label(name + ":", skin);
			nameLabel.setDebug(true);
			add(nameLabel).width(SLIDER_WIDTH / 6).padLeft(HORIZONTAL_PADDING);

			valueLabel = new Label("", skin);
			valueLabel.setAlignment(Align.right);
			add(valueLabel).width(SLIDER_WIDTH / 4).padRight(HORIZONTAL_PADDING);

			slider = new Slider(min, max, step, false, skin);
			slider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setField();
				}
			});
			add(slider).width(SLIDER_WIDTH / 3).padRight(HORIZONTAL_PADDING);

			setField();
		}

		private void setField() {
			Float value = mapping.apply(slider.getValue());
			if (value - value.intValue() == 0)
				valueLabel.setText(value.intValue() + suffix);
			else
				valueLabel.setText(Math.round(value * 10) / 10 + suffix);
		}
	}

	public Float getValue() {
		return mapping.apply(slider.getValue());
	}

}
