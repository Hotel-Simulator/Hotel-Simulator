package pl.agh.edu.actor.slider;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;

public class SliderComponent extends Table {
	private final float SLIDER_WIDTH = 600f;
	private final float SLIDER_HEIGHT = 60f;
	private final float HORIZONTAL_OUTER_PADDING = 50f;
	private final float HORIZONTAL_INNER_PADDING = 30f;
	private Label valueLabel;
	private Slider slider;
	private Skin skin;
	private String suffix;

	public SliderComponent(String name, String suffix, float min, float max, float step) {
		Stack stack = new Stack();
		add(stack).height(SLIDER_HEIGHT);
		skin = HotelSkin.getInstance();
		NinePatch texture = skin.getPatch("gray-100-border-round");
		stack.add(new Image(texture));
		this.suffix = suffix;
		stack.add(new SliderRowTable(name, min, max, step));

	}

	private class SliderRowTable extends Table {

		public SliderRowTable(String name, float min, float max, float step) {
			Label nameLabel = new Label(name + ":", skin, "subtitle1_label");
			add(nameLabel).minWidth(SLIDER_WIDTH / 4).padLeft(HORIZONTAL_OUTER_PADDING);

			valueLabel = new Label("", skin, "subtitle1_label");
			valueLabel.setAlignment(Align.right);
			valueLabel.setText(String.format("%.0f", max) + suffix);
			add(valueLabel).minWidth(SLIDER_WIDTH / 4);

			Drawable drawable = skin.getDrawable("gray-500-borderless");
			drawable.setMinHeight(SLIDER_HEIGHT);
			drawable.setMinWidth(2);
			Image separator = new Image(drawable);
			add(separator).bottom().expandY().padLeft(HORIZONTAL_INNER_PADDING).padRight(HORIZONTAL_INNER_PADDING);

			slider = new Slider(min, max, step, false, skin);
			slider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setField();
				}
			});
			add(slider).width(SLIDER_WIDTH / 3).padRight(HORIZONTAL_OUTER_PADDING);

			setField();
		}

		private void setField() {
			valueLabel.setText(String.format("%.0f", getValue()) + suffix);
		}
	}

	public Float getValue() {
		return slider.getValue();
	}

}
