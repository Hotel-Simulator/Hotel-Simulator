package pl.agh.edu.ui.resolution;

import static pl.agh.edu.ui.resolution.Size.LARGE;
import static pl.agh.edu.ui.resolution.Size.MEDIUM;
import static pl.agh.edu.ui.resolution.Size.SMALL;

import pl.agh.edu.ui.component.selectMenu.SelectMenuResolutionItem;

public enum Resolution {
	_1366x768(1366, 768, SMALL),
	_1440x900(1440, 900, SMALL),
	_1600x900(1600, 900, SMALL),
	_1920x1080(1920, 1080, MEDIUM),
	_2560x1080(2560, 1080, MEDIUM),
	_2560x1440(2560, 1440, MEDIUM),
	_2560x1600(2560, 1600, LARGE),
	_3440x1440(3440, 1440, LARGE),
	_3840x2160(3840, 2160, LARGE);

	public final int WIDTH;
	public final int HEIGHT;
	public final Size SIZE;

	Resolution(int width, int height, Size size) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.SIZE = size;
	}

	public SelectMenuResolutionItem toSelectMenuResolutionItem() {
		return new SelectMenuResolutionItem(this.toString(), this);
	}

	@Override
	public String toString() {
		return WIDTH + "x" + HEIGHT;
	}
}
