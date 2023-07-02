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
    private final Skin skin;

    private Table bottomNavBar;

    public Navbar(String styleName) {
        skin = HotelSkin.getInstance();
        NavbarStyle navbarStyle = skin.get(styleName, NavbarStyle.class);

        Table topNavBar = new Table();
        topNavBar.align(Align.top);
        topNavBar.top();

        moneyPanel = new MoneyPanel();
        topNavBar.add(moneyPanel).expand().top().padTop(10);

        timePanel = new TimePanel();
        topNavBar.add(timePanel).expand().top();

        accelerationPanel = new AccelerationPanel();
        topNavBar.add(accelerationPanel).expand().top().padTop(10);
        topNavBar.pad(0, 140, 0, 140);

        bottomNavBar = new Table();

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
    public void setTime(String time){
        this.timePanel.setTime(time);
    }

    public void setMoney(String money){
        this.moneyPanel.setMoney(money);
    }
    public void setAcceration(String acceleration){
        this.accelerationPanel.setAcceleration(acceleration);
    }

    public void changeBottomNavbarState(BottomNavbarState state){
        switch(state){
            case MAIN:
                bottomNavBar.add(new NavbarButton("bank"));
                bottomNavBar.add(new NavbarButton("hotel"));
                bottomNavBar.add(new NavbarButton("employee"));
                bottomNavBar.add(new NavbarButton("tax"));
                bottomNavBar.add(new NavbarButton("add"));

                break;
            case BANK:
                break;
            case EMPLOYEE:
                break;
            case TAXES:
                break;
            case ADS:
                break;
        }
    }
}