package pl.agh.edu.actor.slider;

public enum SliderSize {
    SMALL(500f,40f,20f,20f),
    MEDIUM(600f,50f,30f,20f),
    LARGE(800f,60f,40f,20f),
    ;


    private float SLIDER_WIDTH;
    private float SLIDER_HEIGHT;
    private float HORIZONTAL_OUTER_PADDING;
    private float HORIZONTAL_INNER_PADDING;

    SliderSize(float slider_width, float slider_height, float horizontal_outer_padding, float horizontal_inner_padding){
        SLIDER_WIDTH = slider_width;
        SLIDER_HEIGHT = slider_height;
        HORIZONTAL_OUTER_PADDING = horizontal_outer_padding;
        HORIZONTAL_INNER_PADDING = horizontal_inner_padding;
    }

//    static{
//        for(SliderSize sliderSize : values())
//            switch (sliderSize) {
//                case SMALL -> {
//                    sliderSize.SLIDER_WIDTH = 500f;
//                    sliderSize.SLIDER_HEIGHT = 40f;
//                    sliderSize.HORIZONTAL_OUTER_PADDING = 20f;
//                    sliderSize.HORIZONTAL_INNER_PADDING = 20f;
//                }
//                case MEDIUM -> {
//                    sliderSize.SLIDER_WIDTH = 600f;
//                    sliderSize.SLIDER_HEIGHT = 50f;
//                    sliderSize.HORIZONTAL_OUTER_PADDING = 30f;
//                    sliderSize.HORIZONTAL_INNER_PADDING = 20f;
//                }
//                case LARGE -> {
//                    sliderSize.SLIDER_WIDTH = 800f;
//                    sliderSize.SLIDER_HEIGHT = 60f;
//                    sliderSize.HORIZONTAL_OUTER_PADDING = 40f;
//                    sliderSize.HORIZONTAL_INNER_PADDING = 20f;
//                }
//        }
//    }

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
