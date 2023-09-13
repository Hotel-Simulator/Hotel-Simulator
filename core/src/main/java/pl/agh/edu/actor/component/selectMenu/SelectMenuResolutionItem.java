package pl.agh.edu.actor.component.selectMenu;

import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.enums.Resolution;

public class SelectMenuResolutionItem extends SelectMenuItem {
	public final Resolution resolution;

	public SelectMenuResolutionItem(String text, Resolution resolution) {
		super(text);
		this.resolution = resolution;
	}
}
