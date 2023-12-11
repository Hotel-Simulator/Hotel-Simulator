package pl.agh.edu.ui.resolution;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Objects;

public class ResolutionManager {
	private static final List<ResolutionChangeListener> listeners = new ArrayList<>();

	public static void notifyListeners() {
		clearListeners();
		listeners.forEach(elem -> {
			Actor actor = elem.onResolutionChange();
			if (actor == null)
				listeners.remove(elem);
		});
	}
	private static void clearListeners() {
		listeners.removeIf(Objects::isNull);
	}

	public static void addListener(ResolutionChangeListener listener) {
		listeners.add(listener);
	}
}
