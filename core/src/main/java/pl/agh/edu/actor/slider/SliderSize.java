package pl.agh.edu.actor.slider;

public enum SliderSize {
    SMALL,
    MEDIUM,
    LARGE,
    ;


    private float SLIDER_WIDTH;
    private float SLIDER_HEIGHT;
    private float HORIZONTAL_OUTER_PADDING;
    private float HORIZONTAL_INNER_PADDING;

    SliderSize() {
        switch (this) {
            case SMALL -> {
                SLIDER_WIDTH = 100f;
                SLIDER_HEIGHT = 10f;
                HORIZONTAL_OUTER_PADDING = 10f;
                HORIZONTAL_INNER_PADDING = 10f;
            }
            case MEDIUM -> {
                SLIDER_WIDTH = 300f;
                SLIDER_HEIGHT = 30f;
                HORIZONTAL_OUTER_PADDING = 30f;
                HORIZONTAL_INNER_PADDING = 30f;
            }
            case LARGE -> {
                SLIDER_WIDTH = 600f;
                SLIDER_HEIGHT = 60f;
                HORIZONTAL_OUTER_PADDING = 60f;
                HORIZONTAL_INNER_PADDING = 60f;
            }
        }
    }

    public float getSLIDER_WIDTH() {
        return SLIDER_WIDTH;
    }

    public float getSLIDER_HEIGHT() {
        return SLIDER_HEIGHT;
    }

    public float getHORIZONTAL_OUTER_PADDING() {
        return HORIZONTAL_OUTER_PADDING;
    }

    public float getHORIZONTAL_INNER_PADDING() {
        return HORIZONTAL_INNER_PADDING;
    }
}
