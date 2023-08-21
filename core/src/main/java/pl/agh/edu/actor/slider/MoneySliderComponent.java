package pl.agh.edu.actor.slider;

public class MoneySliderComponent extends SliderComponent{
    public MoneySliderComponent(String name, float min, float max, float step, SliderSize sliderSize) {
        super(name, "$", min, max, step, sliderSize);

    }

    @Override
    protected void setField() {
        Float value = getValue();
        String displayedValue;
        if (value < 1000) {
            displayedValue = String.format("%.0f", value);
        } else if (value < 1000000) {
            displayedValue = String.format("%.1fk", value / 1000);
        } else {
            displayedValue = String.format("%.1fm", value / 1000000);
        }
        valueLabel.setText(String.format("%.0f", displayedValue) + suffix);
    }
}
