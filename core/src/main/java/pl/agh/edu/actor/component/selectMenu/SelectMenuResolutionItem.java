package pl.agh.edu.actor.component.selectMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.utils.Array;

import pl.agh.edu.enums.Resolution;

public class SelectMenuResolutionItem extends SelectMenuItem {
	public final Resolution resolution;

	public SelectMenuResolutionItem(String text, Resolution resolution) {
		super(resolution.toString(), () -> text);
		this.resolution = resolution;
	}

	public static Array<SelectMenuItem> getArray() {
		Array<SelectMenuItem> itemArray = new Array<>();
		Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();

		for (Resolution resolution : Resolution.values()) {
			 if (resolution.HEIGHT <= displayMode.height && resolution.WIDTH <= displayMode.width) {
			itemArray.add(resolution.toSelectMenuResolutionItem());
			 }
		}

		return itemArray;
	}
}
