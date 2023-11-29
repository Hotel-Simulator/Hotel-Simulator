package pl.agh.edu.ui.component.calendar;

import java.time.LocalDate;
import java.util.function.Consumer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;
import pl.agh.edu.ui.utils.ShadowBackground;

public class CalendarLayer extends Stack implements ResolutionChangeListener {
	private final CalendarComponent calendarComponent;
	private final Actor parent;

	public CalendarLayer(Actor parent, LocalDate chosenDate, Boolean isBlockedByTime, Consumer<LocalDate> dateChangeHandler) {
		super();

		calendarComponent = new CalendarComponent(chosenDate, preAction(dateChangeHandler), isBlockedByTime, true);
		this.parent = parent;

		this.init();
	}

	public CalendarLayer(Actor parent, LocalDate chosenDate, Boolean isBlockedByTime) {
		super();

		calendarComponent = new CalendarComponent(chosenDate, null, isBlockedByTime, false);
		this.parent = parent;

		this.init();
	}

	public void init() {
		this.add(new ShadowBackground(calendarComponent, this::clearAll));
		this.setUpCalendarComponent();
		ResolutionManager.addListener(this);
	}

	private void setUpCalendarComponent() {
		calendarComponent.setTouchable(Touchable.enabled);
		this.add(calendarComponent);
		setPosition();
	}

	private void setPosition() {
		Vector2 position = getPosition(parent);
		calendarComponent.setPosition(position.x, position.y);
	}

	private Consumer<LocalDate> preAction(Consumer<LocalDate> handler) {
		return handler.andThen((date) -> clearAll());
	}

	private void clearAll() {
		this.clear();
		this.clearChildren();
		this.remove();
	}

	@Override
	public void layout() {}

	@Override
	public void validate() {
		setSize(this.getStage().getWidth(), this.getStage().getHeight());
	}

	private Vector2 getPosition(Actor parent) {
		Vector2 vector2 = parent.localToStageCoordinates(new Vector2(0, 0));
		vector2.x += parent.getWidth() / 2;
		vector2.y -= calendarComponent.getActor().getPrefHeight() / 2;
		return vector2;
	}

	@Override
	public Actor onResolutionChange() {
		this.setPosition();
		return this;
	}
}
