package pl.agh.edu.actor.frame;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.button.LabeledButton;
import pl.agh.edu.actor.component.selectMenu.*;
import pl.agh.edu.actor.component.slider.PercentSliderComponent;
import pl.agh.edu.actor.component.slider.SliderComponent;
import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.actor.utils.WrapperTable;
import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.config.LanguageConfig;

public class OptionFrame extends WrapperTable {
	private final SelectMenu selectResolutionMenu = createSelectMenuForResolution();
	private final SelectMenu selectFullScreenMenu = createSelectMenuForFullScreenMode();
	private final SliderComponent musicVolumeSlider = createSliderComponentForMusicVolume();
	private final SliderComponent soundVolumeSlider = createSliderComponentForSoundVolume();
	private final SelectMenu selectLanguageMenu = createSelectMenuForLanguage();
	private final LabeledButton backButton = new LabeledButton(Size.LARGE,"optionsFrame.label.back");
	private final LabeledButton saveButton = new LabeledButton(Size.LARGE,"optionsFrame.label.save");
	public OptionFrame() {
		Skin skin = HotelSkin.getInstance();
		NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("frame-glass-background"));
		this.setBackground(background);

		innerTable.add(selectResolutionMenu).grow().row();
		innerTable.add(selectFullScreenMenu).grow().row();
		innerTable.add(musicVolumeSlider).grow().row();
		innerTable.add(soundVolumeSlider).grow().row();
		innerTable.add(selectLanguageMenu).grow();
		innerTable.row().padTop(OptionFrameStyle.getPadding()/2);

		Table ButtonTable = new Table();
		ButtonTable.add(backButton).grow().uniform();
		ButtonTable.add(saveButton).grow().uniform();

		innerTable.add(ButtonTable).grow();

		innerTable.pad(OptionFrameStyle.getPadding());

		setStartingValue();
		this.setResolutionChangeHandler(this::resize);
	}
	private void resize() {
		size(OptionFrameStyle.getFrameWidth(), OptionFrameStyle.getFrameHeight());
		layout();
	}

	private SelectMenu createSelectMenuForResolution() {

		Function<? super SelectMenuItem, Void> function = selectedOption -> {
			if (selectedOption instanceof SelectMenuResolutionItem resolutionItem) {
				if (resolutionItem.resolution != GraphicConfig.getResolution()) {
					GraphicConfig.changeResolution(resolutionItem.resolution);
				}
			}
			return null;
		};

		return new SelectMenu(
				"optionsFrame.resolution.label",
				SelectMenuResolutionItem.getArray(),
				function);
	}

	private SelectMenu createSelectMenuForFullScreenMode() {

		Function<? super SelectMenuItem, Void> function = selectedOption -> {
			if (selectedOption instanceof SelectMenuBoolean resolutionItem) {
				if (resolutionItem.value != GraphicConfig.isFullscreen()) {
					GraphicConfig.setFullscreenMode(resolutionItem.value);
				}
			}
			return null;
		};

		return new SelectMenu(
				"optionsFrame.fullScreen.label",
				SelectMenuBoolean.getArray(),
				function);
	}

	private SelectMenu createSelectMenuForLanguage() {

		Function<? super SelectMenuItem, Void> function = selectedOption -> {
			if (selectedOption instanceof SelectMenuLanguage languageItem) {
				LanguageConfig.setLanguage(languageItem.value);
			}
			return null;
		};

		return new SelectMenu(
				"optionsFrame.language.label",
				SelectMenuLanguage.getArray(),
				function);
	}

	private SliderComponent createSliderComponentForMusicVolume() {
		PercentSliderComponent sliderComponent = new PercentSliderComponent(
				"optionsFrame.music.label",
				selectedOption -> {
					AudioConfig.setMusicVolume(selectedOption);
					return null;
				});
		sliderComponent.setValue(AudioConfig.getMusicVolume());
		return sliderComponent;
	}

	private SliderComponent createSliderComponentForSoundVolume() {
		PercentSliderComponent sliderComponent = new PercentSliderComponent(
				"optionsFrame.sound.label",
				selectedOption -> {
					AudioConfig.setAudioVolume(selectedOption);
					return null;
				});
		sliderComponent.setValue(AudioConfig.getAudioVolume());
		return sliderComponent;
	}

	private void setStartingValue() {
		selectResolutionMenu.setItem(GraphicConfig.getResolution().toString());
		selectFullScreenMenu.setItem("selectMenu.boolean." + (GraphicConfig.isFullscreen() ? "yes" : "no"));
		selectLanguageMenu.setItem(LanguageConfig.getLanguage().languagePath);
	}

	@Override
	public void layout() {
		super.layout();
		this.setBounds(
				(float) GraphicConfig.getResolution().WIDTH / 2 - OptionFrameStyle.getFrameWidth() / 2,
				(float) GraphicConfig.getResolution().HEIGHT / 2 - OptionFrameStyle.getFrameHeight() / 2,
				OptionFrameStyle.getFrameWidth(),
				OptionFrameStyle.getFrameHeight());
		this.innerTable.layout();
	}
	private static class OptionFrameStyle{
		public static float getFrameHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 400f + 2 * getPadding();
				case MEDIUM -> 500f + 2 * getPadding();
				case LARGE -> 600f + 2 * getPadding();
			};
		}

		public static float getFrameWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 650f + 2*getPadding();
				case MEDIUM -> 750f + 2*getPadding();
				case LARGE -> 900f + 2*getPadding();
			};
		}

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}
	}
}
