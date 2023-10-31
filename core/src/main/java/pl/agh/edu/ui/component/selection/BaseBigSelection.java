package pl.agh.edu.ui.component.selection;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.function.Consumer;

public abstract class BaseBigSelection<T> extends BaseSelection<T>{

    public BaseBigSelection(T value, Label label, Consumer<T> action) {
        super(value, label, action);
    }
    @Override
    protected Button createLeftButton(){
        return new Button(skin, "selection-left-big");
    }
    @Override
    protected Button createRightButton(){
        return new Button(skin, "selection-right-big");
    }
}
