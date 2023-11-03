package pl.agh.edu.ui.component.navbar;

import static com.badlogic.gdx.utils.Align.bottom;
import static com.badlogic.gdx.utils.Scaling.none;
import static pl.agh.edu.ui.audio.SoundAudio.KNOCK_1;
import static pl.agh.edu.ui.component.navbar.BottomNavbarState.AD_MENU;
import static pl.agh.edu.ui.component.navbar.BottomNavbarState.BANK_MENU;
import static pl.agh.edu.ui.component.navbar.BottomNavbarState.EMPLOYEE_MENU;
import static pl.agh.edu.ui.component.navbar.BottomNavbarState.HOTEL_MENU;
import static pl.agh.edu.ui.component.navbar.BottomNavbarState.MAIN_MENU;
import static pl.agh.edu.ui.component.navbar.BottomNavbarState.TAX_MENU;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.ACCOUNT;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.AD;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.BACK;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.BANK;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.BOARD;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.CREDIT;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.DEPOSIT;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.EMPLOYEE;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.HIRE;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.HISTORY;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.HOTEL;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.MANAGE;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.OFFER;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.PLACES;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.ROOMS;
import static pl.agh.edu.ui.component.navbar.NavbarButtonType.TAX;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.button.NavbarButton;
import pl.agh.edu.ui.screen.main.MainScreen;

public class NavbarBottom extends Table {
	private final Table bottomNavBar = new Table();
	private final MainScreen mainScreen;
	private NavbarButtonType currentNavbarButtonType = BOARD;
	private BottomNavbarState currentBottomNavbarState = HOTEL_MENU;

	public NavbarBottom(String styleName, MainScreen screen) {
		mainScreen = screen;

		Skin skin = GameSkin.getInstance();
		NavbarBottomStyle navbarBottomStyle = skin.get(styleName, NavbarBottomStyle.class);

		bottomNavBar.bottom();

		Stack bottomStack = new Stack();
		Image bottomNavbarImage = new Image(navbarBottomStyle.bottomBackground);
		bottomNavbarImage.setScaling(none);
		bottomNavbarImage.setAlign(bottom);
		bottomStack.add(bottomNavbarImage);
		bottomStack.add(bottomNavBar);

		add(bottomStack).growY().bottom();

		changeBottomNavbarState(currentBottomNavbarState);
	}

	private NavbarButton currentNavbarButton = createNavbarButtonWithChangeFrame(currentNavbarButtonType, BANK_MENU);

	private NavbarButton createNavbarButtonWithChangeFrame(NavbarButtonType type, BottomNavbarState state) {
		NavbarButton navbarButton = new NavbarButton(
				type,
				state);
		navbarButton.setTouchUpAction(() -> {
			if (mainScreen.frameStack.isActionPossible()) {
				mainScreen.frameStack.changeFrame(type.getFrame());
				currentBottomNavbarState = state;
				currentNavbarButtonType = type;
				currentNavbarButton.setDisabled(false);
				currentNavbarButton = navbarButton;
				navbarButton.setDisabled(true);
				KNOCK_1.play();
			}
		});
		if (navbarButton.compare(currentNavbarButtonType, currentBottomNavbarState)) {
			currentNavbarButton.setDisabled(false);
			navbarButton.setDisabled(true);
			currentNavbarButton = navbarButton;
		}
		if (mainScreen != null)
			type.getFrame().game = mainScreen.game;
		return navbarButton;
	}

	private NavbarButton createNavbarButtonWithChangeState(NavbarButtonType type, BottomNavbarState newState) {
		NavbarButton navbarButton = new NavbarButton(
				type,
				MAIN_MENU);
		navbarButton.setTouchUpAction(() -> changeBottomNavbarState(newState));
		return navbarButton;
	}

	public void changeBottomNavbarState(BottomNavbarState state) {
		bottomNavBar.clear();
		switch (state) {
			case MAIN_MENU -> {
				bottomNavBar.add(createNavbarButtonWithChangeState(BANK, BANK_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(HOTEL, HOTEL_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(EMPLOYEE, EMPLOYEE_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(TAX, TAX_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(AD, AD_MENU));
			}
			case HOTEL_MENU -> {
				bottomNavBar.add(createNavbarButtonWithChangeFrame(BOARD, HOTEL_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeFrame(ROOMS, HOTEL_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeFrame(PLACES, HOTEL_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(BACK, MAIN_MENU));
			}
			case BANK_MENU -> {
				bottomNavBar.add(createNavbarButtonWithChangeFrame(OFFER, BANK_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeFrame(ACCOUNT, BANK_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeFrame(DEPOSIT, BANK_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeFrame(CREDIT, BANK_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(BACK, MAIN_MENU));
			}
			case EMPLOYEE_MENU -> {
				bottomNavBar.add(createNavbarButtonWithChangeFrame(HIRE, EMPLOYEE_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeFrame(MANAGE, EMPLOYEE_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(BACK, MAIN_MENU));
			}
			case TAX_MENU -> {
				bottomNavBar.add(createNavbarButtonWithChangeFrame(OFFER, TAX_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeFrame(HISTORY, TAX_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(BACK, MAIN_MENU));
			}
			case AD_MENU -> {
				bottomNavBar.add(createNavbarButtonWithChangeFrame(AD, AD_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeFrame(HISTORY, AD_MENU));
				bottomNavBar.add(createNavbarButtonWithChangeState(BACK, MAIN_MENU));
			}
		}
	}

	public static class NavbarBottomStyle {
		public Drawable bottomBackground;

		public NavbarBottomStyle() {}

		public NavbarBottomStyle(NavbarBottomStyle style) {
			bottomBackground = style.bottomBackground;
		}
	}
}
