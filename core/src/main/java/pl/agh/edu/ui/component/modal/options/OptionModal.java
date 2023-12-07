package pl.agh.edu.ui.component.modal.options;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.resolution.Size.LARGE;
import static pl.agh.edu.ui.utils.SkinFont.H2;

import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.config.LanguageConfig;
import pl.agh.edu.serialization.GameSaveHandler;
import pl.agh.edu.ui.component.button.LabeledButton;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.modal.utils.BaseModal;
import pl.agh.edu.ui.component.selectMenu.SelectMenu;
import pl.agh.edu.ui.component.selectMenu.SelectMenuBoolean;
import pl.agh.edu.ui.component.selectMenu.SelectMenuItem;
import pl.agh.edu.ui.component.selectMenu.SelectMenuLanguage;
import pl.agh.edu.ui.component.selectMenu.SelectMenuResolutionItem;
import pl.agh.edu.ui.component.slider.PercentSliderComponent;
import pl.agh.edu.ui.component.slider.SliderComponent;
import pl.agh.edu.utils.LanguageString;

public class OptionModal extends BaseModal {
	private final SelectMenu selectResolutionMenu = createSelectMenuForResolution();
	private final SelectMenu selectFullScreenMenu = createSelectMenuForFullScreenMode();
	private final SelectMenu selectLanguageMenu = createSelectMenuForLanguage();

	public OptionModal() {
		super();

		LanguageLabel titleLabel = new LanguageLabel(new LanguageString("optionsFrame.label.title"), H2.getName());
		titleLabel.setAlignment(center, center);
		innerTable.add(titleLabel).growX().center().row();

		innerTable.add(selectResolutionMenu).growX().expandY().row();
		innerTable.add(selectFullScreenMenu).growX().expandY().row();
		SliderComponent musicVolumeSlider = createSliderComponentForMusicVolume();
		innerTable.add(musicVolumeSlider).growX().expandY().row();
		SliderComponent soundVolumeSlider = createSliderComponentForSoundVolume();
		innerTable.add(soundVolumeSlider).growX().expandY().row();
		innerTable.add(selectLanguageMenu).growX().expandY().row();

		Table ButtonTable = new Table();
		LabeledButton backButton = new LabeledButton(LARGE, new LanguageString("optionsFrame.label.back"));
		ButtonTable.add(backButton).growX().expandY().uniform();
		LabeledButton saveButton = new LabeledButton(LARGE, new LanguageString("optionsFrame.label.save"));
		ButtonTable.add(saveButton).growX().expandY().uniform();

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				CLICK.playSound();
				ModalManager.getInstance().closeModal();
			}
		});

		saveButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameSaveHandler.getInstance().saveGame(getEngine());
				CLICK.playSound();
				Gdx.app.exit();
			}
		});

		innerTable.add(ButtonTable).growX();
		innerTable.pad(OptionFrameStyle.getPadding());

		setStartingValue();
		this.resize();
		this.setResolutionChangeHandler(this::resize);
		this.onLanguageChange();
	}

	private void resize() {
		this.resetAnimationPosition();
		this.validate();
		innerTable.pad(OptionFrameStyle.getPadding());
	}

	private SelectMenu createSelectMenuForResolution() {

		Function<? super SelectMenuItem, Void> function = selectedOption -> {
			if (selectedOption instanceof SelectMenuResolutionItem resolutionItem && resolutionItem.resolution != GraphicConfig.getResolution())
				GraphicConfig.changeResolution(resolutionItem.resolution);
			return null;
		};

		return new SelectMenu(
				new LanguageString("optionsFrame.resolution.label"),
				SelectMenuResolutionItem.getArray(),
				function);
	}

	private SelectMenu createSelectMenuForFullScreenMode() {

		Function<? super SelectMenuItem, Void> function = selectedOption -> {
			if (selectedOption instanceof SelectMenuBoolean resolutionItem && resolutionItem.value != GraphicConfig.isFullscreen())
				GraphicConfig.setFullscreenMode(resolutionItem.value);
			return null;
		};

		return new SelectMenu(
				new LanguageString("optionsFrame.fullScreen.label"),
				SelectMenuBoolean.getArray(),
				function);
	}

	private SelectMenu createSelectMenuForLanguage() {

		Function<? super SelectMenuItem, Void> function = selectedOption -> {
			if (selectedOption instanceof SelectMenuLanguage languageItem)
				LanguageConfig.setLanguage(languageItem.value);
			return null;
		};

		return new SelectMenu(
				new LanguageString("optionsFrame.language.label"),
				SelectMenuLanguage.getArray(),
				function);
	}

	private SliderComponent createSliderComponentForMusicVolume() {
		PercentSliderComponent sliderComponent = new PercentSliderComponent(
				new LanguageString("optionsFrame.music.label"),
				selectedOption -> {
					AudioConfig.setMusicVolume(selectedOption);
					return null;
				});
		sliderComponent.setValue(AudioConfig.getMusicVolume());
		return sliderComponent;
	}

	private SliderComponent createSliderComponentForSoundVolume() {
		PercentSliderComponent sliderComponent = new PercentSliderComponent(
				new LanguageString("optionsFrame.sound.label"),
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
		selectLanguageMenu.setItem(LanguageConfig.getLanguage().languageString.path);
	}

	private static class OptionFrameStyle {
		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}
	}
}
