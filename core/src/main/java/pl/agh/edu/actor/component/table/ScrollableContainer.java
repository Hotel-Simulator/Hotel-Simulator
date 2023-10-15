package pl.agh.edu.actor.component.table;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

import pl.agh.edu.actor.HotelSkin;

public class ScrollableContainer extends ScrollPane {
	public ScrollableContainer(Actor actor) {
		super(actor, HotelSkin.getInstance(), "default");
		ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle(getStyle());
		scrollPaneStyle.background = null;
		setStyle(scrollPaneStyle);
		setScrollbarsVisible(true);
	}
}
