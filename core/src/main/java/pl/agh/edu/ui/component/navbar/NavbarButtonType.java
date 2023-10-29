package pl.agh.edu.ui.component.navbar;

import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.ui.frame.TestFrame;

public enum NavbarButtonType {
	BANK("bank", new TestFrame("frame.title.bank")),
	HOTEL("hotel", new TestFrame("frame.title.hotel")),
	EMPLOYEE("employee", new TestFrame("frame.title.employee")),
	TAX("tax", new TestFrame("frame.title.tax")),
	AD("ad", new TestFrame("frame.title.ad")),
	BOARD("board", new TestFrame("frame.title.board")),
	ROOMS("rooms", new TestFrame("frame.title.rooms")),
	PLACES("places", new TestFrame("frame.title.places")),
	BACK("back", new TestFrame("frame.title.back")),
	OFFER("offer", new TestFrame("frame.title.offer")),
	ACCOUNT("account", new TestFrame("frame.title.account")),
	DEPOSIT("deposit", new TestFrame("frame.title.deposit")),
	CREDIT("credit", new TestFrame("frame.title.credit")),
	HIRE("hire", new TestFrame("frame.title.hire")),
	MANAGE("manage", new TestFrame("frame.title.manage")),
	HISTORY("history", new TestFrame("frame.title.history"));

	private final String styleName;
	private final BaseFrame frame;

	NavbarButtonType(String styleName, BaseFrame frame) {
		this.styleName = styleName;
		this.frame = frame;
	}

	public String getStyleName() {
		return styleName;
	}

	public BaseFrame getFrame() {
		return frame;
	}
}
