package pl.agh.edu.ui.component.calendar;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import pl.agh.edu.ui.audio.SoundAudio;

import java.time.LocalDate;
import java.util.function.Consumer;

public class CalendarLayer extends Stack {
    private final CalendarComponent calendarComponent;
    public CalendarLayer(Actor parent, LocalDate chosenDate,Boolean isBlockedByTime,Consumer<LocalDate> dateChangeHandler) {
        super();
        this.setUpInvisibleBackground();
        calendarComponent = new CalendarComponent(chosenDate, preAction(dateChangeHandler),isBlockedByTime,true);
        this.setUpCalendarComponent(calendarComponent,parent);
    }

    public CalendarLayer(Actor parent, LocalDate chosenDate,Boolean isBlockedByTime) {
        super();
        this.setUpInvisibleBackground();
        calendarComponent = new CalendarComponent(chosenDate, null,isBlockedByTime,false);
        this.setUpCalendarComponent(calendarComponent,parent);
    }

    private void setUpInvisibleBackground(){
        Table invisibleTable = new Table();
        invisibleTable.setTouchable(Touchable.enabled);
        invisibleTable.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isOverCalendar(x,y)){
                    SoundAudio.CLICK_2.play();
                    clearAll();
                }
                return true;
            }
        });
        invisibleTable.setFillParent(true);
        this.add(invisibleTable);
    }

    private void setUpCalendarComponent(CalendarComponent calendarComponent,Actor parent){
        calendarComponent.setTouchable(Touchable.enabled);
        this.add(calendarComponent);
        Vector2 position = getPosition(parent);
        calendarComponent.setPosition(position.x,position.y);
    }
    private Consumer<LocalDate> preAction(Consumer<LocalDate> handler){
        clearAll();
        return handler;
    }

    private void clearAll(){
        this.clear();
        this.clearChildren();
        this.remove();
    }

    private boolean isOverCalendar(float x, float y) {
        Vector2 vector2 = calendarComponent.localToStageCoordinates(new Vector2(0, 0));

        float calendarComponentWidth = calendarComponent.getActor().getWidth();
        float calendarComponentHeight = calendarComponent.getActor().getHeight();

        vector2.x -= calendarComponentWidth/2;
        vector2.y -= calendarComponentHeight/2;

        return x >= vector2.x && x <= vector2.x + calendarComponentWidth &&
                y >= vector2.y && y <= vector2.y + calendarComponentHeight;
    }

    @Override
    public void layout(){}

    @Override
    public void validate(){
        setSize(this.getStage().getWidth(), this.getStage().getHeight());
    }

    private Vector2 getPosition(Actor parent){
        Vector2 vector2 = parent.localToStageCoordinates(new Vector2(0,0));
        vector2.y -= this.getHeight() + parent.getHeight();
        vector2.x += parent.getWidth()/2;
        return vector2;
    }

}
