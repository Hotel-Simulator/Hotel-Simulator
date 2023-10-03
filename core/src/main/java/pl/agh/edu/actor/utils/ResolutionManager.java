package pl.agh.edu.actor.utils;

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
