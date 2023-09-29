package pl.agh.edu.actor.component.rating;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.config.GraphicConfig;

public class Rating extends Table {

	private final Table table = new Table();
	private final Stack stack = new Stack();
	private static final int maxRating = 5;
	private final Star[] stars = IntStream.range(0, maxRating)
			.mapToObj(index -> new Star(index, this))
			.toArray(Star[]::new);
	private int currentRating = 0;
	private final Function<Integer, Void> function;

	public Rating(Function<Integer, Void> function) {
		this.function = function;
		stack.add(new Image(HotelSkin.getInstance().getPatch("rating-background")));
		stack.add(table);
		Arrays.stream(stars).sequential().forEach(table::add);
		this.add(stack).width(RatingStyle.getWidth()).height(RatingStyle.getHeight()).center();
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
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 60f;
				case MEDIUM -> 70f;
				case LARGE -> 80f;
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 250f;
				case MEDIUM -> 270f;
				case LARGE -> 300f;
			};
		}
	}
}
