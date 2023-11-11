package pl.agh.edu.ui.component.rating;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class Rating extends WrapperTable {
	private static final int maxRating = 5;
	private final Star[] stars = IntStream.range(0, maxRating)
			.mapToObj(index -> new Star(index, this))
			.toArray(Star[]::new);
	private final Consumer<Integer> function;
	private int currentRating;
	private boolean disabled = false;

	public Rating(Integer currentRating, Consumer<Integer> function) {
		super();
		this.currentRating = currentRating;
		this.function = function;

		this.setBackground("rating-background");
		Arrays.stream(stars).sequential().forEach(innerTable::add);

		innerTable.setFillParent(true);

		this.setResolutionChangeHandler(this::changeResolutionHandler);
		this.setRating(currentRating);
		changeResolutionHandler();
	}

	public Rating(Consumer<Integer> function) {
		this(0, function);
	}

	public Rating(Integer currentRating) {
		this(currentRating, (Integer i) -> {});
		this.setDisabled(true);
	}

	public boolean isDisabled() {
		return disabled;
	}

	private void setDisabled(boolean disabled) {
		this.disabled = disabled;
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

	public void setRating(int rating) {
		function.accept(rating);
		currentRating = rating;
		Arrays.stream(stars)
				.sequential()
				.forEach(star -> star.updateState(rating));
	}

	private void changeResolutionHandler() {
		this.size(RatingStyle.getWidth(), RatingStyle.getHeight());
	}

	private static class RatingStyle {

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f;
				case MEDIUM -> 10f;
				case LARGE -> 15f;
			};
		}

		public static float getHeight() {
			return Star.getSize() + 2 * getPadding();
		}

		public static float getWidth() {
			return maxRating * Star.getSize() + 2 * getPadding();
		}
	}
}
