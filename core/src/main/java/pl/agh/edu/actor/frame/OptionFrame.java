package pl.agh.edu.actor.frame;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.GameConfig;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.selectMenu.SelectMenu;
import pl.agh.edu.actor.component.selectMenu.SelectMenuItem;
import pl.agh.edu.actor.component.selectMenu.SelectMenuResolutionItem;
import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.enums.Resolution;

public class OptionFrame extends Stack {
	private final float WIDTH = (float) GameConfig.RESOLUTION.getWidth() / 9 * 4;
	private final float HEIGHT = (float) GameConfig.RESOLUTION.getHeight() / 9 * 4;

	private final Table table = new Table();

	public OptionFrame() {
		super();
		Skin skin = HotelSkin.getInstance();
		NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("frame-glass-background"));
		add(new Image(background, Scaling.stretch, Align.center));

		this.add(table);

		Array<SelectMenuItem> selectMenuItems = new Array<>();
		for (Resolution resolution : Resolution.values()) {
			SelectMenuResolutionItem selectMenuItem = resolution.toSelectMenuResolutionItem(Size.SMALL);
			selectMenuItems.add(selectMenuItem);
		}

		Function<SelectMenuItem, Void> function = selectedOption -> {
			if (selectedOption instanceof SelectMenuResolutionItem resolutionItem) {
				GameConfig.changeResolution(resolutionItem.resolution);
			}
			return null;
		};

		SelectMenu selectMenu = new SelectMenu(
				Size.SMALL,
				"Resolution",
				selectMenuItems,
				function);

		table.add(selectMenu);
	}

	@Override
	public void layout() {
		super.layout();
		this.setBounds((float) GameConfig.RESOLUTION.getWidth() / 18 * 5, (float) GameConfig.RESOLUTION.getHeight() / 18 * 5, WIDTH, HEIGHT);
	}

}
