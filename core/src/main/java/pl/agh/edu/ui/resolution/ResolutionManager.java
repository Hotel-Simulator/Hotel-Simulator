package pl.agh.edu.ui.resolution;

import java.util.ArrayList;
import java.util.List;

public class ResolutionManager {
	private static final List<ResolutionChangeListener> listeners = new ArrayList<>();

	public static void notifyListeners() {
		listeners.forEach(ResolutionChangeListener::onResolutionChange);
	}

	public static void addListener(ResolutionChangeListener listener) {
		listeners.add(listener);
	}
}
