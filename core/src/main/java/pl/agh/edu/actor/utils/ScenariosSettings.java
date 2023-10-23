package pl.agh.edu.actor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.config.GraphicConfig;

public class ScenariosSettings {
	private int width;
	private int height;
	private TextButton.TextButtonStyle difficultyButtonStyle;
	private TextButton.TextButtonStyle playBackButtonStyle;
	private String titleFont;
	private String scenarioTitleFont;
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
				titleFont = "white-h3";
				difficultyButtonStyle = GameSkin.getInstance().get("difficulty-button-subtitle1", TextButton.TextButtonStyle.class);
				playBackButtonStyle = GameSkin.getInstance().get("scenarios-play-h4", TextButton.TextButtonStyle.class);
				scenarioTitleFont = "white-h4";
			}
			case MEDIUM -> {
				titleFont = "white-h2";
				difficultyButtonStyle = GameSkin.getInstance().get("difficulty-button-h4", TextButton.TextButtonStyle.class);
				playBackButtonStyle = GameSkin.getInstance().get("scenarios-play-h2", TextButton.TextButtonStyle.class);
				scenarioTitleFont = "white-h3";
			}
			case LARGE -> {
				titleFont = "white-h1";
				difficultyButtonStyle = GameSkin.getInstance().get("difficulty-button-h3", TextButton.TextButtonStyle.class);
				playBackButtonStyle = GameSkin.getInstance().get("scenarios-play-h1", TextButton.TextButtonStyle.class);
				scenarioTitleFont = "white-h2";
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

	public String getTitleFont() {
		return titleFont;
	}

	public String getScenarioTitleFont() {
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
