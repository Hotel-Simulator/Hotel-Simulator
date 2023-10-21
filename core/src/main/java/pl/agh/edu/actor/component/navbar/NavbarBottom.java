package pl.agh.edu.actor.component.navbar;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.component.button.NavbarButton;
import pl.agh.edu.actor.frame.TestFrame;
import pl.agh.edu.enums.BottomNavbarState;
import pl.agh.edu.screen.MainScreen;

public class NavbarBottom extends Table {
	private final Table bottomNavBar = new Table();

	private final MainScreen mainScreen;

	public NavbarBottom(String styleName, MainScreen screen) {
		mainScreen = screen;

		Skin skin = GameSkin.getInstance();
		NavbarBottomStyle navbarBottomStyle = skin.get(styleName, NavbarBottomStyle.class);

		bottomNavBar.bottom();

		Stack bottomStack = new Stack();
		Image bottomNavbarImage = new Image(navbarBottomStyle.bottomBackground);
		bottomNavbarImage.setScaling(Scaling.none);
		bottomNavbarImage.setAlign(Align.bottom);
		bottomStack.add(bottomNavbarImage);
		bottomStack.add(bottomNavBar);

		add(bottomStack).growY().bottom();
		this.changeBottomNavbarState(BottomNavbarState.MAIN);
	}

	public static class NavbarBottomStyle {
		public Drawable bottomBackground;

		public NavbarBottomStyle() {

		}

		public NavbarBottomStyle(NavbarBottomStyle style) {
			bottomBackground = style.bottomBackground;
		}
	}

	public void changeBottomNavbarState(BottomNavbarState state) {
		bottomNavBar.clear();
		switch (state) {
			case MAIN -> {
				bottomNavBar.add(new NavbarButton("bank", () -> changeBottomNavbarState(BottomNavbarState.BANK)));
				bottomNavBar.add(new NavbarButton("hotel", () -> changeBottomNavbarState(BottomNavbarState.HOTEL)));
				bottomNavBar.add(new NavbarButton("employee", () -> changeBottomNavbarState(BottomNavbarState.EMPLOYEE)));
				bottomNavBar.add(new NavbarButton("tax", () -> changeBottomNavbarState(BottomNavbarState.TAXES)));
				bottomNavBar.add(new NavbarButton("ad", () -> changeBottomNavbarState(BottomNavbarState.ADS)));
			}
			case HOTEL -> {
				bottomNavBar.add(new NavbarButton("board", () -> mainScreen.changeFrame(new TestFrame("board"))));
				bottomNavBar.add(new NavbarButton("rooms", () -> mainScreen.changeFrame(new TestFrame("rooms"))));
				bottomNavBar.add(new NavbarButton("places", () -> mainScreen.changeFrame(new TestFrame("places"))));
				bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
			}
			case BANK -> {
				bottomNavBar.add(new NavbarButton("offer", () -> mainScreen.changeFrame(new TestFrame("offer"))));
				bottomNavBar.add(new NavbarButton("account", () -> mainScreen.changeFrame(new TestFrame("account"))));
				bottomNavBar.add(new NavbarButton("deposit", () -> mainScreen.changeFrame(new TestFrame("deposit"))));
				bottomNavBar.add(new NavbarButton("credit", () -> mainScreen.changeFrame(new TestFrame("credit"))));
				bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
			}
			case EMPLOYEE -> {
				bottomNavBar.add(new NavbarButton("hire", () -> mainScreen.changeFrame(new TestFrame("hire"))));
				bottomNavBar.add(new NavbarButton("manage", () -> mainScreen.changeFrame(new TestFrame("manage"))));
				bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
			}
			case TAXES -> {
				bottomNavBar.add(new NavbarButton("offer", () -> mainScreen.changeFrame(new TestFrame("offer"))));
				bottomNavBar.add(new NavbarButton("history", () -> mainScreen.changeFrame(new TestFrame("history"))));
				bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
			}
			case ADS -> {
				bottomNavBar.add(new NavbarButton("ad", () -> mainScreen.changeFrame(new TestFrame("ad"))));
				bottomNavBar.add(new NavbarButton("history", () -> mainScreen.changeFrame(new TestFrame("history"))));
				bottomNavBar.add(new NavbarButton("back", () -> changeBottomNavbarState(BottomNavbarState.MAIN)));
			}
		}
	}
}
