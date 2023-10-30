package pl.agh.edu.ui.component.selector;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import static pl.agh.edu.ui.utils.FontType.BODY_1;

import java.time.YearMonth;
import java.util.function.Consumer;

public class YearSelection extends BaseSelection<YearMonth> {

    private final Time time = Time.getInstance();

    public YearSelection(YearMonth startValue, Consumer<YearMonth> action) {
        super(startValue, createNewLabel(), action);
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
        return getValue().plusYears(1).isBefore(time.getYearMonth().plusYears(2));
    }
    @Override
    protected boolean isPreviousButtonCheck() {
        return (getValue().minusYears(1).isAfter(time.getYearMonth().minusYears(2))
                && !getValue().minusYears(1).isBefore(YearMonth.from(time.startingTime)));
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
