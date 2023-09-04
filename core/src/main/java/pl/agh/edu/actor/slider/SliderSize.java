package pl.agh.edu.actor.slider;

public enum SliderSize {
	SMALL(500f, 40f, 20f, 20f),
	MEDIUM(600f, 50f, 30f, 20f),
	LARGE(800f, 60f, 40f, 20f),
	;

	private final float SLIDER_WIDTH;
	private final float SLIDER_HEIGHT;
	private final float HORIZONTAL_OUTER_PADDING;
	private final float HORIZONTAL_INNER_PADDING;

	SliderSize(float slider_width, float slider_height, float horizontal_outer_padding, float horizontal_inner_padding) {
		SLIDER_WIDTH = slider_width;
		SLIDER_HEIGHT = slider_height;
		HORIZONTAL_OUTER_PADDING = horizontal_outer_padding;
		HORIZONTAL_INNER_PADDING = horizontal_inner_padding;
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
