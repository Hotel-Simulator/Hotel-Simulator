package pl.agh.edu.actor.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.ray3k.tenpatch.TenPatchDrawable;

import pl.agh.edu.actor.HotelSkin;

public abstract class WrapperTable extends WrapperContainer<Table> {
	public final Table innerTable = new Table();

	public WrapperTable(String languagePath) {
		super(languagePath);
		this.init();
	}

	public WrapperTable() {
		super();
		this.init();
	}

	public void init() {
		this.setActor(innerTable);
		innerTable.setFillParent(true);
		innerTable.pad(0);
	}

	public void setBackground(String backgroundPatch) {
		innerTable.setBackground(new NinePatchDrawable(HotelSkin.getInstance().getPatch(backgroundPatch)));
	}

	public void set10PatchBackground(String backgroundPatch) {
		innerTable.setBackground(new TenPatchDrawable(HotelSkin.getInstance().get(backgroundPatch, TenPatchDrawable.class)));
	}
}
