package pl.agh.edu.actor.frame;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.selectMenu.SelectMenu;
import pl.agh.edu.actor.component.selectMenu.SelectMenuBoolean;
import pl.agh.edu.actor.component.selectMenu.SelectMenuItem;
import pl.agh.edu.actor.component.selectMenu.SelectMenuResolutionItem;
import pl.agh.edu.actor.slider.PercentSliderComponent;
import pl.agh.edu.actor.slider.SliderComponent;
import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.config.GraphicConfig;

public class OptionFrame extends Stack {
	private final Table table = new Table();
	private final SelectMenu selectResolutionMenu = createSelectMenuForResolution();
	private final SelectMenu selectFullScreenMenu = createSelectMenuForFullScreenMode();
	private final SliderComponent musicVolumeSlider = createSliderComponentForMusicVolume();
	private final SliderComponent soundVolumeSlider = createSliderComponentForSoundVolume();

	public OptionFrame() {
		super();
		Skin skin = HotelSkin.getInstance();
		NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("frame-glass-background"));
		add(new Image(background, Scaling.stretch, Align.center));

		this.add(table);
		table.add(selectResolutionMenu).grow();
		table.row();
		table.add(selectFullScreenMenu).grow();
		table.row();
		table.add(musicVolumeSlider).grow();
		table.row();
		table.add(soundVolumeSlider).grow();

		setStartingValue();
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
				"Resolution",
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
				"Full screen mode",
				SelectMenuBoolean.getArray(),
				function);
	}

	private SliderComponent createSliderComponentForMusicVolume() {
		PercentSliderComponent sliderComponent = new PercentSliderComponent(
				"Music",
				selectedOption -> {
					AudioConfig.setMusicVolume(selectedOption);
					return null;
				});
		sliderComponent.setValue(AudioConfig.getMusicVolume());
		return sliderComponent;
	}

	private SliderComponent createSliderComponentForSoundVolume() {
		PercentSliderComponent sliderComponent = new PercentSliderComponent(
				"Sound",
				selectedOption -> {
					AudioConfig.setAudioVolume(selectedOption);
					return null;
				});
		sliderComponent.setValue(AudioConfig.getAudioVolume());
		return sliderComponent;
	}

	private void setStartingValue() {
		selectResolutionMenu.setItem(GraphicConfig.getResolution().toString());
		selectFullScreenMenu.setItem(GraphicConfig.isFullscreen() ? "Yes" : "No");
	}

	private float getFrameWidth() {
		return (float) GraphicConfig.getResolution().WIDTH / 2;
	}

	private float getFrameHeight() {
		return (float) GraphicConfig.getResolution().HEIGHT / 2;
	}

	@Override
	public void layout() {
		super.layout();
		this.setBounds(
				getFrameWidth() / 2,
				getFrameHeight() / 2,
				getFrameWidth(),
				getFrameHeight());
		this.selectResolutionMenu.layout();
		this.table.layout();
	}

}
