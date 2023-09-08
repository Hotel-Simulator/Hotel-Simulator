package pl.agh.edu.actor.component.navbar;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.panel.navbar.AccelerationPanel;
import pl.agh.edu.actor.component.panel.navbar.MoneyPanel;
import pl.agh.edu.actor.component.panel.navbar.TimePanel;

public class NavbarTop extends Table {

	private static final float navbarHorizontalPadding = 190f;
	private static final float navbarTopPadding = 15f;

	public NavbarTop(String styleName) {
		Skin skin = HotelSkin.getInstance();
		NavbarTopStyle navbarTopStyle = skin.get(styleName, NavbarTopStyle.class);

		Table topNavBar = new Table();
		topNavBar.align(Align.top);

		MoneyPanel moneyPanel = new MoneyPanel();
		topNavBar.add(moneyPanel).expandX().left();

		TimePanel timePanel = new TimePanel();
		topNavBar.add(timePanel).expandX().top();

		AccelerationPanel accelerationPanel = new AccelerationPanel();
		topNavBar.add(accelerationPanel).expandX().right();
		topNavBar.pad(navbarTopPadding, navbarHorizontalPadding, 0, navbarHorizontalPadding);

		Stack topStack = new Stack();
		Image topNavbarImage = new Image(navbarTopStyle.topBackground);
		topNavbarImage.setScaling(Scaling.none);
		topNavbarImage.setAlign(Align.top);
		topStack.add(topNavbarImage);
		topStack.add(topNavBar);

		add(topStack).growY().top();
	}

	public static class NavbarTopStyle {
		public Drawable topBackground;

		public NavbarTopStyle() {

		}

		public NavbarTopStyle(NavbarTopStyle style) {
			topBackground = style.topBackground;
		}
	}
}
