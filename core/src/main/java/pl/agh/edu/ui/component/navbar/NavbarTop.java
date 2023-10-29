package pl.agh.edu.ui.component.navbar;

import static com.badlogic.gdx.utils.Align.top;
import static com.badlogic.gdx.utils.Scaling.none;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import pl.agh.edu.ui.GameSkin;

public class NavbarTop extends Table {

	private static final float navbarHorizontalPadding = 190f;
	private static final float navbarTopPadding = 10f;

	public NavbarTop(String styleName) {
		Skin skin = GameSkin.getInstance();
		NavbarTopStyle navbarTopStyle = skin.get(styleName, NavbarTopStyle.class);

		Table topNavBar = new Table();
		topNavBar.align(top);

		MoneyPanel moneyPanel = new MoneyPanel();
		topNavBar.add(moneyPanel).expandX().left();

		TimePanel timePanel = new TimePanel();
		topNavBar.add(timePanel).expandX().top();

		AccelerationPanel accelerationPanel = new AccelerationPanel();
		topNavBar.add(accelerationPanel).expandX().right();
		topNavBar.pad(navbarTopPadding, navbarHorizontalPadding, 0, navbarHorizontalPadding);

		Stack topStack = new Stack();
		Image topNavbarImage = new Image(navbarTopStyle.topBackground);
		topNavbarImage.setScaling(none);
		topNavbarImage.setAlign(top);
		topStack.add(topNavbarImage);
		topStack.add(topNavBar);

		add(topStack).growY().top();
	}

	public static class NavbarTopStyle {
		public Drawable topBackground;

		public NavbarTopStyle() {}

		public NavbarTopStyle(NavbarTopStyle style) {
			topBackground = style.topBackground;
		}
	}
}
