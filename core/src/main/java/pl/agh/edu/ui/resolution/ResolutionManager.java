package pl.agh.edu.ui.resolution;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ResolutionManager {
	private static final List<ResolutionChangeListener> listeners = new ArrayList<>();

	public static void notifyListeners() {
		listeners.forEach(elem -> {
			Actor actor = elem.onResolutionChange();
			if (actor == null)
				listeners.remove(elem);
		});
	}

	public static void addListener(ResolutionChangeListener listener) {
		listeners.add(listener);
	}
}
