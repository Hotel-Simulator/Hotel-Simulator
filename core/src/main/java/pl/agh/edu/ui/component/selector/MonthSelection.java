package pl.agh.edu.ui.component.selector;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.label.LanguageLabel;
import static pl.agh.edu.ui.utils.FontType.BODY_1;

import java.time.YearMonth;
import java.util.function.Consumer;

public class MonthSelection extends BaseSelection<YearMonth> {

    private final Time time = Time.getInstance();

    public MonthSelection(YearMonth startValue, Consumer<YearMonth> action) {
        super(startValue, createNewLabel(), action);
    }

    public void updateState(YearMonth yearMonth){
        setValue(yearMonth);
        this.updateLabel();
        this.checkButtons();
    }

    private static LanguageLabel createNewLabel(){
        return new LanguageLabel("calendarComponent.month.january", BODY_1.getName());
    }
    @Override
    protected boolean isNextButtonCheck() {
        return getValue().plusMonths(1).isBefore(time.getYearMonth().plusYears(2));
    }
    @Override
    protected boolean isPreviousButtonCheck() {
        return (getValue().minusMonths(1).isAfter(time.getYearMonth().minusYears(2)) && getValue().isAfter(YearMonth.from(time.startingTime)));
    }
    @Override
    protected void nextButtonHandler() {
        setValue(getValue().plusMonths(1));
    }

    @Override
    protected void previousButtonHandler() {
        setValue(getValue().minusMonths(1));
    }
    @Override
    protected void updateLabel(Label label) {
        if(label instanceof LanguageLabel) {
            ((LanguageLabel) label).updateLanguagePath("calendarComponent.month." + getValue().getMonth().toString().toLowerCase());
        }
    }
}
