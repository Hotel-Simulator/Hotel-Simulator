package pl.agh.edu.actor.component.navbar;

import pl.agh.edu.actor.frame.BaseFrame;
import pl.agh.edu.actor.frame.TestFrame;

public enum NavbarButtonType {
    BANK("bank", new TestFrame("bank")),
    HOTEL("hotel", new TestFrame("hotel")),
    EMPLOYEE("employee", new TestFrame("employee")),
    TAX("tax", new TestFrame("tax")),
    AD("ad", new TestFrame("ad")),
    BOARD("board", new TestFrame("board")),
    ROOMS("rooms", new TestFrame("rooms")),
    PLACES("places", new TestFrame("places")),
    BACK("back", new TestFrame("back")),
    OFFER("offer", new TestFrame("offer")),
    ACCOUNT("account", new TestFrame("account")),
    DEPOSIT("deposit", new TestFrame("deposit")),
    CREDIT("credit", new TestFrame("credit")),
    HIRE("hire", new TestFrame("hire")),
    MANAGE("manage", new TestFrame("manage")),
    HISTORY("history", new TestFrame("history"));

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
