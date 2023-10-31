package pl.agh.edu.ui.component.selection;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.label.CustomLabel;
import static pl.agh.edu.ui.utils.FontType.BODY_1;

import java.time.YearMonth;
import java.util.function.Consumer;

public class YearSelection extends BaseSelection<YearMonth> {

    private final Time time = Time.getInstance();
    private final boolean isBlockedByTime;

    public YearSelection(YearMonth startValue, Consumer<YearMonth> action,Boolean isBlockedByTime) {
        super(startValue, createNewLabel(), action);
        this.isBlockedByTime = isBlockedByTime;
    }

    private static CustomLabel createNewLabel(){
        return new CustomLabel(BODY_1.getName());
    }

    public void updateState(YearMonth yearMonth){
        setValue(yearMonth);
        this.updateLabel();
        this.checkButtons();
    }
    @Override
    protected boolean isNextButtonCheck() {
        if(isBlockedByTime)return getValue().plusYears(1).isBefore(time.getYearMonth().plusYears(2));
        return true;
    }
    @Override
    protected boolean isPreviousButtonCheck() {
        if(isBlockedByTime)return (getValue().minusYears(1).isAfter(time.getYearMonth().minusYears(2))
                && !getValue().minusYears(1).isBefore(YearMonth.from(time.startingTime)));
        return true;
    }
    @Override
    protected void nextButtonHandler() {
        setValue(getValue().plusYears(1));
    }

    @Override
    protected void previousButtonHandler() {
        setValue(getValue().minusYears(1));
    }
    @Override
    protected void updateLabel(Label label) {
        label.setText(String.valueOf(getValue().getYear()));
    }
}
