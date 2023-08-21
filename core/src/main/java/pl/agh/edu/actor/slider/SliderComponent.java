package pl.agh.edu.actor.slider;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;

public class SliderComponent extends Table {
	private final float SLIDER_WIDTH;
	private final float SLIDER_HEIGHT;
	private final float HORIZONTAL_OUTER_PADDING;
	private final float HORIZONTAL_INNER_PADDING;
	protected Label valueLabel;
	protected Slider slider;
	protected Skin skin;
	protected String suffix;
	private float min;
	private float max;
	private float step;

	public SliderComponent(String name, String suffix, float min, float max, float step, SliderSize sliderSize) {
		SLIDER_WIDTH = sliderSize.getSLIDER_WIDTH();
		SLIDER_HEIGHT = sliderSize.getSLIDER_HEIGHT();
		HORIZONTAL_OUTER_PADDING = sliderSize.getHORIZONTAL_OUTER_PADDING();
		HORIZONTAL_INNER_PADDING = sliderSize.getHORIZONTAL_OUTER_PADDING();
		Stack componentStack = new Stack();
		add(componentStack).height(SLIDER_HEIGHT);
		skin = HotelSkin.getInstance();
		NinePatch background = skin.getPatch("slider-component-background");
		componentStack.add(new Image(background));
		this.suffix = suffix;
		componentStack.add(new SliderRowTable(name, min, max, step));

	}

	private class SliderRowTable extends Table {

		public SliderRowTable(String name, float min, float max, float step) {
			Label nameLabel = new Label(name + ":", skin, "subtitle1_label");
			add(nameLabel).minWidth(SLIDER_WIDTH / 4).padLeft(HORIZONTAL_OUTER_PADDING);

			valueLabel = new Label("", skin, "subtitle1_label");
			valueLabel.setAlignment(Align.right);
			valueLabel.setText(String.format("%.0f", max) + suffix);
			add(valueLabel).minWidth(SLIDER_WIDTH / 4);

			Drawable lineDrawable = skin.getDrawable("separator-line");
			lineDrawable.setMinHeight(SLIDER_HEIGHT);
			lineDrawable.setMinWidth(2);
			Image separator = new Image(lineDrawable);
			add(separator).bottom().padLeft(HORIZONTAL_INNER_PADDING).padRight(HORIZONTAL_INNER_PADDING);

			slider = new Slider(min, max, step, false, skin);
			slider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setField();
				}
			});
			add(slider).width(SLIDER_WIDTH / 3).padRight(HORIZONTAL_OUTER_PADDING);

			setField();
		}


	}
	protected void setField() {
		String displayedValue;

		if (Math.floor(step)==step){
			displayedValue = String.format("%.0f", slider.getValue());
		}
		else{
			displayedValue = String.format("%.1f", slider.getValue());
		}
		valueLabel.setText(displayedValue + suffix);
	}


	public Float getValue() {
		return slider.getValue();
	}

}
