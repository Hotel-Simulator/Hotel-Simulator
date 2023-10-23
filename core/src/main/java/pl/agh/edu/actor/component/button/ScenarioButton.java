package pl.agh.edu.actor.component.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.enums.HotelType;

public class ScenarioButton extends Table {
	private final Skin skin = GameSkin.getInstance();
	public final HotelType hotelType;
	private boolean isSelected = false;
	public int width;
	public int height;
	public final NinePatchDrawable unselected = new NinePatchDrawable(skin.getPatch("button"));
	public final NinePatchDrawable selected = new NinePatchDrawable(skin.getPatch("button_selected"));

	public ScenarioButton(String title, String image, String description, String season, HotelType hotelType, String titleFont, String textFont) {
		super();
		setSize();
		this.hotelType = hotelType;
		pad(30, 20, 30, 20);

		NinePatchDrawable buttonBackground = new NinePatchDrawable(skin.getPatch("button"));
		this.setBackground(buttonBackground);

		Label.LabelStyle titleLabel = new Label.LabelStyle();
		titleLabel.font = skin.getFont(titleFont);
		titleLabel.font.getData().setLineHeight((int) (height / 30));
		titleLabel.fontColor = Color.YELLOW;
		Label labelTitle = new Label(title, titleLabel);
		labelTitle.setWrap(true);
		labelTitle.setAlignment(getAlign());
		add(labelTitle).width(5 * width / 24).height(height / 12);

		row();

		Image icon = new Image(skin.getDrawable(image));
		add(icon).top().width(width / 12).height(height / 12).padTop(height / 40).center();

		row();

		Label.LabelStyle descriptionLabel = new Label.LabelStyle();
		descriptionLabel.font = skin.getFont(textFont);
		descriptionLabel.font.getData().setLineHeight((int) (height / 40));
		descriptionLabel.fontColor = Color.YELLOW;
		Label descriptionText = new Label(description, descriptionLabel);
		descriptionText.setWrap(true);
		descriptionText.setAlignment(getAlign());
		add(descriptionText).width(5 * width / 24).height(height / 12).padTop(height / 40);

		row();

		Label seasonLabel = new Label("Popular in " + season, descriptionLabel);
		seasonLabel.setWrap(true);
		seasonLabel.setAlignment(getAlign());
		add(seasonLabel).width(3 * width / 24).padTop(height / 40).center();

		setTouchable(Touchable.enabled);
	}

	public boolean getSelected() {
		return this.isSelected;
	}

	public void setSelected() {
		isSelected = true;
		setBackground(selected);
	}

	public void setUnselected() {
		isSelected = false;
		setBackground(unselected);
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

}
