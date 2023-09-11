package pl.agh.edu.actor.slider;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.utils.Size;

public class SliderComponent extends Table {
	private final float SLIDER_WIDTH;
	private final float SLIDER_HEIGHT;
	private final float HORIZONTAL_OUTER_PADDING;
	private final float HORIZONTAL_INNER_PADDING;
	protected Label valueLabel;
	protected Slider slider;
	protected Skin skin;
	protected String suffix;
	private float minValue;
	private float maxValue;
	private float step;
	Label realLabel;

	public SliderComponent(String name, String suffix, float minValue, float maxValue, float step, Size size) {

		SliderSize sliderSize = SliderSize.valueOf(size.name());
		SLIDER_WIDTH = sliderSize.getSLIDER_WIDTH();
		SLIDER_HEIGHT = sliderSize.getSLIDER_HEIGHT();
		HORIZONTAL_OUTER_PADDING = sliderSize.getHORIZONTAL_OUTER_PADDING();
		HORIZONTAL_INNER_PADDING = sliderSize.getHORIZONTAL_INNER_PADDING();

		Stack componentStack = new Stack();
		add(componentStack).height(SLIDER_HEIGHT);
		skin = HotelSkin.getInstance();
		NinePatch background = skin.getPatch("slider-component-background");
		componentStack.add(new Image(background));
		this.suffix = suffix;
		componentStack.add(new SliderRowTable(name, minValue, maxValue, step));

	}

	private class SliderRowTable extends Table {

		public SliderRowTable(String name, float minValue, float maxValue, float step) {
			Label nameLabel = new Label(name + ":", skin, "subtitle1_label");
			nameLabel.setAlignment(Align.left, Align.center);
			add(nameLabel).width(SLIDER_WIDTH / 4 - HORIZONTAL_OUTER_PADDING).padLeft(HORIZONTAL_OUTER_PADDING);

			valueLabel = new Label("", skin, "subtitle1_label");
			valueLabel.setAlignment(Align.right);
			valueLabel.setText(String.format("%.0f", maxValue) + suffix);
			add(valueLabel).width(SLIDER_WIDTH / 4);

			Drawable lineDrawable = skin.getDrawable("separator-line");
			lineDrawable.setMinHeight(SLIDER_HEIGHT);
			lineDrawable.setMinWidth(2);
			Image separator = new Image(lineDrawable);
			add(separator).bottom().padLeft(HORIZONTAL_INNER_PADDING).padRight(HORIZONTAL_INNER_PADDING).setActorX(SLIDER_WIDTH / 2);
			slider = new Slider(minValue, maxValue, step, false, skin);
			slider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setField();
				}
			});
			add(slider).width(SLIDER_WIDTH / 2 - HORIZONTAL_OUTER_PADDING).padRight(HORIZONTAL_OUTER_PADDING);
			realLabel = new Label("", skin);

		}

	}

	protected void setField() {

		String displayedValue;

		if (Math.floor(step) == step) {
			displayedValue = String.format("%.0f", slider.getValue());
		} else {
			displayedValue = String.format("%.1f", slider.getValue());
		}
		valueLabel.setText(displayedValue + suffix);
	}

	public float getValue() {
		return slider.getValue();
	}

	private enum SliderSize {
		SMALL(500f, 40f, 20f, 20f),
		MEDIUM(600f, 50f, 30f, 20f),
		LARGE(800f, 60f, 40f, 20f),
		;

		private final float SLIDER_WIDTH;
		private final float SLIDER_HEIGHT;
		private final float HORIZONTAL_OUTER_PADDING;
		private final float HORIZONTAL_INNER_PADDING;

		SliderSize(float slider_width, float slider_height, float horizontal_outer_padding, float horizontal_inner_padding) {
			SLIDER_WIDTH = slider_width;
			SLIDER_HEIGHT = slider_height;
			HORIZONTAL_OUTER_PADDING = horizontal_outer_padding;
			HORIZONTAL_INNER_PADDING = horizontal_inner_padding;
		}

		public float getSLIDER_WIDTH() {
			return SLIDER_WIDTH;
		}

		public float getSLIDER_HEIGHT() {
			return SLIDER_HEIGHT;
		}

		public float getHORIZONTAL_OUTER_PADDING() {
			return HORIZONTAL_OUTER_PADDING;
		}

		public float getHORIZONTAL_INNER_PADDING() {
			return HORIZONTAL_INNER_PADDING;
		}
	}

}
