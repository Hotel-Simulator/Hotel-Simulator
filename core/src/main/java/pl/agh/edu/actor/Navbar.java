package pl.agh.edu.actor;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.actor.button.NavbarButton;
import pl.agh.edu.actor.panel.navbar.AccelerationPanel;
import pl.agh.edu.actor.panel.navbar.MoneyPanel;
import pl.agh.edu.actor.panel.navbar.TimePanel;
import pl.agh.edu.enums.BottomNavbarState;

public class Navbar extends Table {

	private final MoneyPanel moneyPanel;
	private final TimePanel timePanel;
	private final AccelerationPanel accelerationPanel;
	private final Table bottomNavBar;

	public Navbar(String styleName) {
		Skin skin = HotelSkin.getInstance();
		NavbarStyle navbarStyle = skin.get(styleName, NavbarStyle.class);

		Table topNavBar = new Table();
		topNavBar.align(Align.top);
		topNavBar.top();

		moneyPanel = new MoneyPanel();
		topNavBar.add(moneyPanel).expandX().left();

		timePanel = new TimePanel();
		topNavBar.add(timePanel).expandX().top();

		accelerationPanel = new AccelerationPanel();
		topNavBar.add(accelerationPanel).expandX().right();
		float navbarHorizontalPadding = 190f;
		float navbarTopPadding = 15f;
		topNavBar.pad(navbarTopPadding, navbarHorizontalPadding, 0, navbarHorizontalPadding);

		bottomNavBar = new Table();
		bottomNavBar.bottom();

		Stack topStack = new Stack();
		Image topNavbarImage = new Image(navbarStyle.topBackground);
		topNavbarImage.setScaling(Scaling.none);
		topNavbarImage.setAlign(Align.top);
		topStack.add(topNavbarImage);
		topStack.add(topNavBar);

		Stack bottomStack = new Stack();
		Image bottomNavbarImage = new Image(navbarStyle.bottomBackground);
		bottomNavbarImage.setScaling(Scaling.none);
		bottomNavbarImage.setAlign(Align.bottom);
		bottomStack.add(bottomNavbarImage);
		bottomStack.add(bottomNavBar);

		add(topStack).growY().top();
		row();
		add(bottomStack).growY().bottom();
		this.changeBottomNavbarState(BottomNavbarState.MAIN);
	}

	public static class NavbarStyle {
		public Drawable topBackground;
		public Drawable bottomBackground;

		public String name;

		public NavbarStyle() {

		}

		public NavbarStyle(Navbar.NavbarStyle style) {
			topBackground = style.topBackground;
			bottomBackground = style.bottomBackground;
			name = style.name;
		}
	}

	public void setTime(String time) {
		this.timePanel.setTime(time);
	}

	public void setMoney(String money) {
		this.moneyPanel.setMoney(money);
	}

	public void setAcceration(String acceleration) {
		this.accelerationPanel.setAcceleration(acceleration);
	}

	public void changeBottomNavbarState(BottomNavbarState state) {
		bottomNavBar.clear();
		switch (state) {
		case MAIN -> {
			bottomNavBar.add(new NavbarButton("bank", () -> changeBottomNavbarState(BottomNavbarState.BANK)));
			bottomNavBar.add(new NavbarButton("hotel", () -> changeBottomNavbarState(BottomNavbarState.HOTEL)));
			bottomNavBar.add(new NavbarButton("employee", () -> changeBottomNavbarState(BottomNavbarState.EMPLOYEE)));
			bottomNavBar.add(new NavbarButton("tax", () -> changeBottomNavbarState(BottomNavbarState.TAXES)));
			bottomNavBar.add(new NavbarButton("add", () -> changeBottomNavbarState(BottomNavbarState.ADS)));
		}
		case HOTEL -> {
			bottomNavBar.add(new NavbarButton("board", () -> {}));
			bottomNavBar.add(new NavbarButton("rooms", () -> {}));
			bottomNavBar.add(new NavbarButton("places", () -> {}));
			bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
		}
		case BANK -> {
			bottomNavBar.add(new NavbarButton("offer", () -> {}));
			bottomNavBar.add(new NavbarButton("account", () -> {}));
			bottomNavBar.add(new NavbarButton("deposit", () -> {}));
			bottomNavBar.add(new NavbarButton("credit", () -> {}));
			bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
		}
		case EMPLOYEE -> {
			bottomNavBar.add(new NavbarButton("hire", () -> {}));
			bottomNavBar.add(new NavbarButton("manage", () -> {}));
			bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
		}
		case TAXES -> {
			bottomNavBar.add(new NavbarButton("offer", () -> {}));
			bottomNavBar.add(new NavbarButton("history", () -> {}));
			bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
		}
		case ADS -> {
			bottomNavBar.add(new NavbarButton("add", () -> {}));
			bottomNavBar.add(new NavbarButton("history", () -> {}));
			bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
		}
		}
	}
}
