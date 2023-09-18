package pl.agh.edu.actor.slider;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.GameConfig;
import pl.agh.edu.actor.HotelSkin;

public class SliderComponent extends Table {
	protected Label valueLabel;
	private Slider slider;
	protected Skin skin = HotelSkin.getInstance();
	protected String suffix;

	public SliderComponent(String name, String suffix, float minValue, float maxValue, float step) {
		this.suffix = suffix;

		Stack componentStack = new Stack();
		componentStack.add(new Image(skin.getPatch("slider-component-background")));
		componentStack.add(new SliderRowTable(name, minValue, maxValue, step));

		this.add(componentStack).height(SliderStyle.getHeight());
	}

	protected float getMaxValue() {
		return slider.getMaxValue();
	}

	protected float getMinValue() {
		return slider.getMinValue();
	}

	private class SliderRowTable extends Table {

		public SliderRowTable(String name, float minValue, float maxValue, float step) {
			Label nameLabel = new Label(name + ":", skin, "subtitle1_label");
			nameLabel.setAlignment(Align.left, Align.center);
			add(nameLabel).width(SliderStyle.getWidth() / 4 - SliderStyle.getOuterPadding()).padLeft(SliderStyle.getOuterPadding());

			valueLabel = new Label("", skin, "subtitle1_label");
			valueLabel.setAlignment(Align.right);
			valueLabel.setText(String.format("%.0f", maxValue) + suffix);
			add(valueLabel).width(SliderStyle.getWidth() / 4);

			Drawable lineDrawable = skin.getDrawable("separator-line");
			lineDrawable.setMinHeight(SliderStyle.getHeight());
			lineDrawable.setMinWidth(2);

			Image separator = new Image(lineDrawable);
			add(separator).bottom().padLeft(SliderStyle.getInnerPadding()).padRight(SliderStyle.getInnerPadding()).setActorX(SliderStyle.getWidth() / 2);

			slider = new Slider(minValue, maxValue, step, false, skin);
			slider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					stateChangeHandler();
				}
			});
			add(slider).width(SliderStyle.getWidth() / 2 - SliderStyle.getOuterPadding()).padRight(SliderStyle.getOuterPadding());
		}
	}

	protected void stateChangeHandler() {
		String displayedValue = String.format("%.1f", this.getValue());
		valueLabel.setText(displayedValue + suffix);
	}

	public float getValue() {
		return slider.getValue();
	}

	private static class SliderStyle {
		public static float getHeight() {
			return switch (GameConfig.getResolution().SIZE) {
			case SMALL -> 40f;
			case MEDIUM -> 50f;
			case LARGE -> 60f;
			};
		}

		public static float getWidth() {
			return switch (GameConfig.getResolution().SIZE) {
			case SMALL -> 500f;
			case MEDIUM -> 600f;
			case LARGE -> 800f;
			};
		}

		public static float getInnerPadding() {
			return switch (GameConfig.getResolution().SIZE) {
			case SMALL -> 20f;
			case MEDIUM -> 30f;
			case LARGE -> 40f;
			};
		}

		public static float getOuterPadding() {
			return switch (GameConfig.getResolution().SIZE) {
			case SMALL -> 20f;
			case MEDIUM -> 20f;
			case LARGE -> 20f;
			};
		}
	}
}
