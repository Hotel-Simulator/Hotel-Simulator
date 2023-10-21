package pl.agh.edu.actor.component.modal.options;

import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.actor.component.button.LabeledButton;
import pl.agh.edu.actor.component.selectMenu.SelectMenu;
import pl.agh.edu.actor.component.selectMenu.SelectMenuBoolean;
import pl.agh.edu.actor.component.selectMenu.SelectMenuItem;
import pl.agh.edu.actor.component.selectMenu.SelectMenuLanguage;
import pl.agh.edu.actor.component.selectMenu.SelectMenuResolutionItem;
import pl.agh.edu.actor.component.slider.PercentSliderComponent;
import pl.agh.edu.actor.component.slider.SliderComponent;
import pl.agh.edu.actor.utils.resolution.Size;
import pl.agh.edu.actor.utils.wrapper.WrapperTable;
import pl.agh.edu.audio.SoundAudio;
import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.config.LanguageConfig;

public class OptionModal extends WrapperTable {
	private final SelectMenu selectResolutionMenu = createSelectMenuForResolution();
	private final SelectMenu selectFullScreenMenu = createSelectMenuForFullScreenMode();
	private final SelectMenu selectLanguageMenu = createSelectMenuForLanguage();

	public OptionModal(Runnable closeHandler) {
		this.setBackground("frame-glass-background");

		innerTable.add(selectResolutionMenu).grow().row();
		innerTable.add(selectFullScreenMenu).grow().row();
		SliderComponent musicVolumeSlider = createSliderComponentForMusicVolume();
		innerTable.add(musicVolumeSlider).grow().row();
		SliderComponent soundVolumeSlider = createSliderComponentForSoundVolume();
		innerTable.add(soundVolumeSlider).grow().row();
		innerTable.add(selectLanguageMenu).grow();
		innerTable.row().padTop(OptionFrameStyle.getPadding() / 2);

		Table ButtonTable = new Table();
		LabeledButton backButton = new LabeledButton(Size.LARGE, "optionsFrame.label.back");
		ButtonTable.add(backButton).grow().uniform();
		LabeledButton saveButton = new LabeledButton(Size.LARGE, "optionsFrame.label.save");
		ButtonTable.add(saveButton).grow().uniform();

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundAudio.BUTTON_1.play();
				closeHandler.run();
			}
		});

		saveButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundAudio.BUTTON_1.play();
				Gdx.app.exit();
			}
		});

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

	private static class OptionFrameStyle {
		public static float getFrameHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 400f + 2 * getPadding();
				case MEDIUM -> 500f + 2 * getPadding();
				case LARGE -> 600f + 2 * getPadding();
			};
		}

		public static float getFrameWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 650f + 2 * getPadding();
				case MEDIUM -> 750f + 2 * getPadding();
				case LARGE -> 900f + 2 * getPadding();
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
