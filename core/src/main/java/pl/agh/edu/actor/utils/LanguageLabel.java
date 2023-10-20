package pl.agh.edu.actor.utils;


import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.language.LanguageChangeListener;
import pl.agh.edu.language.LanguageManager;

public class LanguageLabel extends CustomLabel implements LanguageChangeListener {
	private final String languagePath;

	public LanguageLabel(String languagePath, String font) {
		super(font);
		this.languagePath = languagePath;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	@Override
	public void onLanguageChange() {
		this.	setText(LanguageManager.get(languagePath));
	}

	public void setUnderscore() {
		setDebug(true);
		LabelStyle labelStyle = new LabelStyle(getStyle());
		NinePatchDrawable underscore =  new NinePatchDrawable(HotelSkin.getInstance().getPatch("underscore"));

		underscore = underscore.tint(HotelSkin.getInstance().getColor("secondary-500"));

		labelStyle.background = underscore;
		setStyle(labelStyle);
	}
}
