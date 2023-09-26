package pl.agh.edu.actor.component.rating;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.HotelSkin;

public class Rating extends Table {
	private static final int maxRating = 5;
	private final Star[] stars = IntStream.range(0, maxRating)
			.mapToObj(index -> new Star(index, this))
			.toArray(Star[]::new);
	private int currentRating = 0;
	private final Function<Integer, Void> function;

	public Rating(Function<Integer, Void> function) {
		this.function = function;
		this.setBackground(new NinePatchDrawable(HotelSkin.getInstance().getPatch("rating-background")));
		Arrays.stream(stars).sequential().forEach(this::add);
		this.setSize(RatingStyle.getWidth(), RatingStyle.getHeight());
		this.center();
	}

	public void setRating(int rating) {
		function.apply(rating);
		currentRating = rating;
		Arrays.stream(stars)
				.sequential()
				.forEach(star -> star.updateState(rating));
	}

	public void setOverRating(int rating) {
		Arrays.stream(stars)
				.sequential()
				.forEach(star -> star.updateOverState(rating));
	}

	public void setDefaultRating() {
		setRating(currentRating);
	}

	public int getRating() {
		return currentRating;
	}

	private static class RatingStyle {

		public static float getPadding() {
			return 20f;
		}

		public static float getHeight() {
			return Star.getSize() + getPadding();
		}

		public static float getWidth() {
			return maxRating * Star.getSize() + getPadding();
		}
	}
}
