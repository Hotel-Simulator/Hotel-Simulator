package pl.agh.edu.ui.component.navbar;

import java.util.function.Supplier;

import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.ui.frame.TestFrame;
import pl.agh.edu.ui.frame.employee.HireEmployeeFrame;
import pl.agh.edu.ui.frame.employee.ManageEmployeeFrame;
import pl.agh.edu.ui.frame.hotel.HotelFrame;
import pl.agh.edu.utils.LanguageString;

public enum NavbarButtonType {
	BANK("bank", () -> new TestFrame(new LanguageString("frame.title.bank"))),
	HOTEL("hotel", () -> new TestFrame(new LanguageString("frame.title.hotel"))),
	EMPLOYEE("employee", () -> new TestFrame(new LanguageString("frame.title.employee"))),
	TAX("tax", () -> new TestFrame(new LanguageString("frame.title.tax"))),
	AD("ad", () -> new TestFrame(new LanguageString("frame.title.ad"))),
	BOARD("board", HotelFrame::new),
	ROOMS("rooms", () -> new TestFrame(new LanguageString("frame.title.rooms"))),
	PLACES("places", () -> new TestFrame(new LanguageString("frame.title.places"))),
	BACK("back", () -> new TestFrame(new LanguageString("frame.title.back"))),
	OFFER("offer", () -> new TestFrame(new LanguageString("frame.title.offer"))),
	ACCOUNT("account", () -> new TestFrame(new LanguageString("frame.title.account"))),
	DEPOSIT("deposit", () -> new TestFrame(new LanguageString("frame.title.deposit"))),
	CREDIT("credit", () -> new TestFrame(new LanguageString("frame.title.credit"))),
	HIRE("hire", HireEmployeeFrame::new),
	MANAGE("manage", ManageEmployeeFrame::new),
	HISTORY("history", () -> new TestFrame(new LanguageString("frame.title.history")));

	private final String styleName;
	private final Supplier<BaseFrame> frameCreator;

	NavbarButtonType(String styleName, Supplier<BaseFrame> frameCreator) {
		this.styleName = styleName;
		this.frameCreator = frameCreator;
	}

	public String getStyleName() {
		return styleName;
	}

	public BaseFrame getFrameCreator() {
		return frameCreator.get();
	}
}
