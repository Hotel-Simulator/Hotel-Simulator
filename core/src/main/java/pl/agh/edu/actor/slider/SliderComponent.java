package pl.agh.edu.actor.slider;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;

import java.math.BigDecimal;

public class SliderComponent extends Table {
	private final float SLIDER_WIDTH;
	private final float SLIDER_HEIGHT;
	private final float HORIZONTAL_OUTER_PADDING;
	private final float HORIZONTAL_INNER_PADDING;
	protected Label valueLabel;
	protected BigSlider slider;
	protected Skin skin;
	protected String suffix;
	private float min;
	private float max;
	private float step;
	Label realLabel;

	public SliderComponent(String name, String suffix, BigDecimal min, BigDecimal max, BigDecimal step, SliderSize sliderSize) {
		SLIDER_WIDTH = sliderSize.getSLIDER_WIDTH();
		SLIDER_HEIGHT = sliderSize.getSLIDER_HEIGHT();
		HORIZONTAL_OUTER_PADDING = sliderSize.getHORIZONTAL_OUTER_PADDING();
		HORIZONTAL_INNER_PADDING = sliderSize.getHORIZONTAL_INNER_PADDING();
		Stack componentStack = new Stack();
		add(componentStack).height(SLIDER_HEIGHT);
		skin = HotelSkin.getInstance();
		NinePatch background = skin.getPatch("slider-component-background");
		componentStack.add(new Image(background));
		this.suffix = suffix;
		componentStack.add(new SliderRowTable(name, min, max, step));

	}

//	public SliderComponent(String name, String suffix, Prefix min, Prefix max, BigDecimal step, SliderSize sliderSize) {
//
//	}

	private class SliderRowTable extends Table {

		public SliderRowTable(String name, BigDecimal min, BigDecimal max, BigDecimal step) {
			Label nameLabel = new Label(name + ":", skin, "subtitle1_label");
			nameLabel.setAlignment(Align.left, Align.center);
			add(nameLabel).width(SLIDER_WIDTH / 4 - HORIZONTAL_OUTER_PADDING).padLeft(HORIZONTAL_OUTER_PADDING);

			valueLabel = new Label("", skin, "subtitle1_label");
			valueLabel.setAlignment(Align.right);
			valueLabel.setText(String.format("%.0f", max) + suffix);
			add(valueLabel).width(SLIDER_WIDTH / 4);

			Drawable lineDrawable = skin.getDrawable("separator-line");
			lineDrawable.setMinHeight(SLIDER_HEIGHT);
			lineDrawable.setMinWidth(2);
			Image separator = new Image(lineDrawable);
			add(separator).bottom().padLeft(HORIZONTAL_INNER_PADDING).padRight(HORIZONTAL_INNER_PADDING).setActorX(SLIDER_WIDTH/2);
			System.out.println((max.subtract(min)).floatValue());
			System.out.println(1f/(max.subtract(min)).floatValue());
			slider = new BigSlider(min, max, step, false, skin);
			slider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setField();
					BigSlider sl = (BigSlider) actor;
					realLabel.setText(String.format( "%f",sl.getValue()));
				}
			});
			add(slider).width(SLIDER_WIDTH / 2 - HORIZONTAL_OUTER_PADDING).padRight(HORIZONTAL_OUTER_PADDING);
			realLabel = new Label("",skin);
			row();
			add(realLabel);

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


	public BigDecimal getValue() {
		return slider.getValue();
	}

//dymamic step
// multiplier for bigdecimal

	//mniejsza gałka

}
