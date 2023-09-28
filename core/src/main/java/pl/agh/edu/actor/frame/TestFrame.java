package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.component.rating.Rating;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		Rating rating = new Rating(1, (Integer i) -> {
			System.out.println("Rating: " + i);
		});

		Rating rating2 = new Rating(1);
		root.add(rating);
		root.row();
		root.add(rating2);
		this.add(root);
	}
}
