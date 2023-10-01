package pl.agh.edu.actor.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.ray3k.tenpatch.TenPatchDrawable;

import pl.agh.edu.actor.HotelSkin;

public abstract class WrapperDoubleTable extends WrapperContainer<Table> {
	private final Table innerTable = new Table();
	public final Table rightTable = new Table();
	public final Table leftTable = new Table();

	public WrapperDoubleTable(String languagePath) {
		super(languagePath);
		this.init();
	}

	public WrapperDoubleTable() {
		super();
		this.init();
	}

	private void init() {
		innerTable.setFillParent(true);
		this.setActor(innerTable);
		innerTable.add(leftTable).grow().uniform();
		innerTable.add(rightTable).grow().uniform();
	}

	public void setBackground(String backgroundPatch) {
		innerTable.setBackground(new NinePatchDrawable(HotelSkin.getInstance().getPatch(backgroundPatch)));
	}

	public void set10PatchBackground(String backgroundPatch) {
		innerTable.setBackground(new TenPatchDrawable(HotelSkin.getInstance().get(backgroundPatch, TenPatchDrawable.class)));
	}
}
