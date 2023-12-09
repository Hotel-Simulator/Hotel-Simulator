package pl.agh.edu.ui.component.slider;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static com.badlogic.gdx.utils.Align.right;
import static pl.agh.edu.ui.audio.SoundAudio.PIP;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE2;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE3;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperDoubleTable;
import pl.agh.edu.utils.LanguageString;

public abstract class SliderComponent extends WrapperDoubleTable {

	protected final String suffix;
	protected final CustomLabel valueLabel = new CustomLabel(SliderStyle.getFont());
	protected final LanguageLabel nameLabel;
	private final Slider slider;

	public SliderComponent(LanguageString languageString, String suffix, float minValue, float maxValue, float step) {
		super();
		this.suffix = suffix;

		this.set10PatchBackground("split-frame-up-10");

		nameLabel = new LanguageLabel(languageString, SliderStyle.getFont());
		nameLabel.setAlignment(center, left);

		valueLabel.setAlignment(center, right);

		slider = new Slider(minValue, maxValue, step, false, getGameSkin());
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				PIP.playSound();
				stateChangeHandler();
			}
		});

		this.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				set10PatchBackground("split-frame-over-10");
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
				set10PatchBackground("split-frame-up-10");
			}
		});
		// slider.setFillParent(true);
		Container<Slider> sliderContainer = new Container<>(slider);
		sliderContainer.fill();
		// sliderContainer.width(SliderStyle.getWidth()/2-10*SliderStyle.getInnerPadding());
		sliderContainer.pad(0);

		leftTable.add(nameLabel).left().grow().uniform();
		leftTable.add(valueLabel).right().grow().uniform();
		rightTable.add(sliderContainer).center().grow();

		this.setResolutionChangeHandler(this::changeResolutionHandler);
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

	public void setDisable(Boolean disable) {
		slider.setDisabled(disable);
	}

	public void setValue(float value) {
		slider.setValue(value);
	}

	private void changeResolutionHandler() {
		leftTable.padLeft(SliderStyle.getInnerPadding());
		leftTable.padRight(SliderStyle.getInnerPadding());

		rightTable.padLeft(SliderStyle.getInnerPadding());
		rightTable.padRight(SliderStyle.getInnerPadding());

		this.nameLabel.setFont(SliderStyle.getFont());
		this.valueLabel.setFont(SliderStyle.getFont());
		this.size(SliderStyle.getWidth(), SliderStyle.getHeight());
	}

	private static class SliderStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 45f + 2 * getPadding();
				case MEDIUM -> 50f + 2 * getPadding();
				case LARGE -> 60f + 2 * getPadding();
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 500f;
				case MEDIUM -> 700f;
				case LARGE -> 1000f;
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

		public static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SUBTITLE3.getName();
				case MEDIUM, LARGE -> SUBTITLE2.getName();
			};
		}
	}
}
