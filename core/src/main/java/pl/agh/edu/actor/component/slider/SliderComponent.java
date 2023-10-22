package pl.agh.edu.actor.component.slider;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.FontType;
import pl.agh.edu.actor.utils.wrapper.WrapperDoubleTable;
import pl.agh.edu.audio.SoundAudio;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.language.LanguageChangeListener;

public abstract class SliderComponent extends WrapperDoubleTable implements LanguageChangeListener {

	protected Skin skin = GameSkin.getInstance();
	private final Slider slider;
	protected String suffix;
	private final Label nameLabel = new Label("", skin, FontType.SUBTITLE2.getName());
	protected final Label valueLabel = new Label("100 %", skin, FontType.SUBTITLE2.getName());

	public SliderComponent(String languagePath, String suffix, float minValue, float maxValue, float step) {
		super(languagePath);

		this.set10PatchBackground("slider-background-10-patch");

		this.suffix = suffix;

		this.setLanguageChangeHandler(nameLabel::setText);
		this.setResolutionChangeHandler(this::changeResolutionHandler);

		nameLabel.setAlignment(Align.center, Align.center);
		valueLabel.setAlignment(Align.right, Align.right);

		slider = new Slider(minValue, maxValue, step, false, skin);
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundAudio.PIP_1.play();
				stateChangeHandler();
			}
		});
		leftTable.add(nameLabel).left().grow().uniform().padLeft(SliderStyle.getInnerPadding());
		leftTable.add(valueLabel).right().grow().uniform().padRight(SliderStyle.getInnerPadding());
		rightTable.add(slider).center().grow().padLeft(SliderStyle.getInnerPadding()).padRight(SliderStyle.getInnerPadding());

		this.changeResolutionHandler();
	}

	protected float getMaxValue() {
		return slider.getMaxValue();
	}

	protected float getMinValue() {
		return slider.getMinValue();
	}

	protected void stateChangeHandler() {
		updateValueLabel();
	}

	private void updateValueLabel() {
		String displayedValue = String.format("%.1f", this.getValue());
		valueLabel.setText(displayedValue + suffix);
	}

	public float getValue() {
		return slider.getValue();
	}

	public void setValue(float value) {
		slider.setValue(value);
	}

	private void changeResolutionHandler() {
		this.size(SliderStyle.getWidth(), SliderStyle.getHeight());
	}

	private static class SliderStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f + 2 * getPadding();
				case MEDIUM -> 35f + 2 * getPadding();
				case LARGE -> 40f + 2 * getPadding();
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 650f;
				case MEDIUM -> 750f;
				case LARGE -> 900f;
			};
		}

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f;
				case MEDIUM -> 10f;
				case LARGE -> 15f;
			};
		}

		public static float getInnerPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 15f;
				case MEDIUM -> 30f;
				case LARGE -> 50f;
			};
		}
	}
}
