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
	private final Table table = new Table();

	private final SelectMenu selectResolutionMenu = createSelectMenuForResolution();

	public OptionFrame() {
		super();
		Skin skin = HotelSkin.getInstance();
		NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("frame-glass-background"));
		add(new Image(background, Scaling.stretch, Align.center));

		table.add(selectResolutionMenu).fill();

		this.add(table);
		this.add(selectResolutionMenu);
	}

	private SelectMenu createSelectMenuForResolution(){

		Array<SelectMenuItem> selectMenuItems = new Array<>();
		for (Resolution resolution : Resolution.values()) {
			SelectMenuResolutionItem selectMenuItem = resolution.toSelectMenuResolutionItem();
			selectMenuItems.add(selectMenuItem);
		}

		Function<? super SelectMenuItem, Void> function = selectedOption -> {
			if (selectedOption instanceof SelectMenuResolutionItem resolutionItem) {
				if(resolutionItem.resolution != GameConfig.RESOLUTION){
					GameConfig.changeResolution(resolutionItem.resolution);
				}
			}
			return null;
		};

		return new SelectMenu(
				"Resolution",
				selectMenuItems,
				function);
	}

	@Override
	public void layout() {
		System.out.println("OptionFrame.layout()");
		super.layout();
		this.setBounds(
				(float) GameConfig.RESOLUTION.WIDTH / 4,
				(float) GameConfig.RESOLUTION.HEIGHT / 4,
				(float) GameConfig.RESOLUTION.WIDTH / 2,
				(float) GameConfig.RESOLUTION.HEIGHT / 2);
		this.selectResolutionMenu.layout();
		this.table.layout();
	}

}
