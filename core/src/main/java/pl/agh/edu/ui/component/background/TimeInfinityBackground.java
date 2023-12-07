package pl.agh.edu.ui.component.background;

import pl.agh.edu.ui.utils.EngineProvider;

public class TimeInfinityBackground extends InfinityBackground implements EngineProvider {

	public TimeInfinityBackground(String fileName) {
		super(fileName);
	}

	@Override
	protected float getMovingDistance() {
		return 0.5f * getEngine().time.getAcceleration();
	}
}
