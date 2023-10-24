package pl.agh.edu.actor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.config.GraphicConfig;

import java.awt.*;

public class ScenariosSettings {
	private GameSkin skin = GameSkin.getInstance();
	private int width;
	private int height;
	private TextButton.TextButtonStyle difficultyButtonStyle;
	private TextButton.TextButtonStyle playBackButtonStyle;
	private BitmapFont titleFont;
	private BitmapFont scenarioTitleFont;
	private int diffWidth;
	private int diffHeight;
	private float largePaddingMultiplier;

	public ScenariosSettings() {
		setParams();
	}

	public void setParams() {
		setSize();
		defaultValuesSet();
		switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> {
				titleFont = skin.getFont("white-h3");
				difficultyButtonStyle = skin.get("difficulty-button-subtitle1", TextButton.TextButtonStyle.class);
				playBackButtonStyle = skin.get("scenarios-play-h4", TextButton.TextButtonStyle.class);
				scenarioTitleFont = skin.getFont("white-h4");
			}
			case MEDIUM -> {
				titleFont = skin.getFont("white-h2");
				difficultyButtonStyle = skin.get("difficulty-button-h4", TextButton.TextButtonStyle.class);
				playBackButtonStyle = skin.get("scenarios-play-h2", TextButton.TextButtonStyle.class);
				scenarioTitleFont = skin.getFont("white-h3");
			}
			case LARGE -> {
				titleFont = skin.getFont("white-h1");
				difficultyButtonStyle = skin.get("difficulty-button-h3", TextButton.TextButtonStyle.class);
				playBackButtonStyle = skin.get("scenarios-play-h1", TextButton.TextButtonStyle.class);
				scenarioTitleFont = skin.getFont("white-h2");
				diffHeight = height / 12;
				diffWidth = width / 8;
				largePaddingMultiplier = height / 1000f * 0.85f;
			}
		}
	}

	private void defaultValuesSet() {
		diffWidth = width / 7;
		diffHeight = height / 7;
		largePaddingMultiplier = 1;
	}

	public void setSize() {
		if (GraphicConfig.isFullscreen()) {
			this.width = Gdx.graphics.getWidth();
			this.height = Gdx.graphics.getHeight();
		}
		else {
			this.width = GraphicConfig.getResolution().WIDTH;
			this.height = GraphicConfig.getResolution().HEIGHT;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public TextButton.TextButtonStyle getDifficultyButtonStyle() {
		return difficultyButtonStyle;
	}

	public TextButton.TextButtonStyle getPlayBackButtonStyle() {
		return playBackButtonStyle;
	}

	public BitmapFont getTitleFont() {
		return titleFont;
	}

	public BitmapFont getScenarioTitleFont() {
		return scenarioTitleFont;
	}

	public int getDiffWidth() {
		return diffWidth;
	}

	public int getDiffHeight() {
		return diffHeight;
	}

	public float getLargePaddingMultiplier() {
		return largePaddingMultiplier;
	}
}
