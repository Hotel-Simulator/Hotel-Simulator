package pl.agh.edu.ui.component.selection;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import static com.badlogic.gdx.utils.Align.center;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

import java.util.function.Consumer;

public abstract class BaseSelection<T> extends WrapperTable {
    protected final Skin skin = GameSkin.getInstance();
    private final Button leftButton = createLeftButton();
    private final Button rightButton = createRightButton();
    private T value;
    private final Label label;

    private final boolean isBig;

    public BaseSelection(T value, Label label, Consumer<T> action){
        this(value, label, action, false);
    }
    public BaseSelection(T value, Label label, Consumer<T> action,Boolean isBig){
        this.value = value;
        this.label = label;
        this.isBig = isBig;
        leftButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isPreviousButtonCheck()){
                    leftButton.setDisabled(true);
                    return true;
                }
                return !leftButton.isDisabled();
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(leftButton.isDisabled()) return;
                SoundAudio.BUTTON_3.play();
                rightButton.setDisabled(false);
                previousButtonHandler();
                updateLabel(label);
                action.accept(getValue());
            }
        });

        rightButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isNextButtonCheck()){
                    rightButton.setDisabled(true);
                    return true;
                }
                return !rightButton.isDisabled();
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(rightButton.isDisabled()) return;
                SoundAudio.BUTTON_3.play();
                leftButton.setDisabled(false);
                nextButtonHandler();
                updateLabel(label);
                action.accept(getValue());
            }
        });
        label.setAlignment(center,center);
        updateLabel(label);

        innerTable.setFillParent(false);

        innerTable.add().uniform();
        innerTable.add(leftButton).uniform();
        innerTable.add(label).grow();
        innerTable.add(rightButton).uniform();
        innerTable.add().uniform().row();

        innerTable.debugCell();
    }
    protected Button createLeftButton(){
        return new Button(skin, "selection-left" + (isBig ? "-big" : ""));
    }
    protected Button createRightButton(){
        return new Button(skin, "selection-right" + (isBig ? "-big" : ""));
    }

    protected abstract boolean isNextButtonCheck();
    protected abstract boolean isPreviousButtonCheck();
    protected abstract void nextButtonHandler();
    protected abstract void previousButtonHandler();
    protected abstract void updateLabel(Label label);
    public void updateLabel(){
        updateLabel(label);
    }
    public T getValue() {
        return value;
    }
    protected void setValue(T value) {
        this.value = value;
    }
    protected void checkButtons(){
        rightButton.setDisabled(!isNextButtonCheck());
        leftButton.setDisabled(!isPreviousButtonCheck());
    }

}
