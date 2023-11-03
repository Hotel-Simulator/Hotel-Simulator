package pl.agh.edu.ui.component.modal.options;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.audio.SoundAudio.BUTTON_1;
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
import pl.agh.edu.ui.component.button.LabeledButton;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.selectMenu.SelectMenu;
import pl.agh.edu.ui.component.selectMenu.SelectMenuBoolean;
import pl.agh.edu.ui.component.selectMenu.SelectMenuItem;
import pl.agh.edu.ui.component.selectMenu.SelectMenuLanguage;
import pl.agh.edu.ui.component.selectMenu.SelectMenuResolutionItem;
import pl.agh.edu.ui.component.slider.PercentSliderComponent;
import pl.agh.edu.ui.component.slider.SliderComponent;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class OptionModal extends WrapperTable {
	private final SelectMenu selectResolutionMenu = createSelectMenuForResolution();
	private final SelectMenu selectFullScreenMenu = createSelectMenuForFullScreenMode();
	private final SelectMenu selectLanguageMenu = createSelectMenuForLanguage();

	public OptionModal(Runnable closeHandler) {
		this.setBackground("modal-glass-background");

		LanguageLabel titleLabel = new LanguageLabel(new LanguageString("optionsFrame.label.title"), H2.getName());
		titleLabel.setAlignment(center, center);
		innerTable.add(titleLabel).growX().center().row();

		innerTable.add(selectResolutionMenu).grow().pad(OptionFrameStyle.getInnerPadding()).row();
		innerTable.add(selectFullScreenMenu).grow().pad(OptionFrameStyle.getInnerPadding()).row();
		SliderComponent musicVolumeSlider = createSliderComponentForMusicVolume();
		innerTable.add(musicVolumeSlider).grow().pad(OptionFrameStyle.getInnerPadding()).row();
		SliderComponent soundVolumeSlider = createSliderComponentForSoundVolume();
		innerTable.add(soundVolumeSlider).grow().pad(OptionFrameStyle.getInnerPadding()).row();
		innerTable.add(selectLanguageMenu).grow().pad(OptionFrameStyle.getInnerPadding()).row();

		Table ButtonTable = new Table();
		LabeledButton backButton = new LabeledButton(LARGE, new LanguageString("optionsFrame.label.back"));
		ButtonTable.add(backButton).pad(OptionFrameStyle.getInnerPadding()).grow().uniform();
		LabeledButton saveButton = new LabeledButton(LARGE, new LanguageString("optionsFrame.label.save"));
		ButtonTable.add(saveButton).pad(OptionFrameStyle.getInnerPadding()).grow().uniform();

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				BUTTON_1.playAudio();
				closeHandler.run();
			}
		});

		saveButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				BUTTON_1.playAudio();
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

	@Override
	public void validate() {
		super.validate();
		if (this.getParent() != null) {
			innerTable.setBounds(
					this.getParent().getX(),
					this.getParent().getY(),
					this.getWidth(),
					this.getHeight());
			this.setResetAnimationPosition(
					this.getParent().getX() + (GraphicConfig.getResolution().WIDTH - this.getWidth()) / 2,
					this.getParent().getY() + (GraphicConfig.getResolution().HEIGHT - this.getHeight()) / 2);
		}
	}

	private static class OptionFrameStyle {
		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}

		public static float getInnerPadding() {
			return 0f;
		}
	}
}
