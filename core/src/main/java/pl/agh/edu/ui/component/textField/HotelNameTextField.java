package pl.agh.edu.ui.component.textField;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;

public class HotelNameTextField extends TextField {
	public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();

	public HotelNameTextField(Skin skin, String style) {
		super("", skin, style);
		String hotelName = game.engine.hotel.getHotelName();
		setText(hotelName);

		setMaxLength();
		setAlignment(center);
		saveTextOnChange();

		addClickListener();
	}

	private void addClickListener() {
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				CLICK.playSound();
			}
		});
	}

	public void setMaxLength() {
		switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> this.setMaxLength(16);
			case MEDIUM, LARGE -> this.setMaxLength(14);
		}
	}

	public void saveTextOnChange() {
		this.setTextFieldListener((textField, c) -> game.engine.hotel.setHotelName(textField.getText()));
	}

}
