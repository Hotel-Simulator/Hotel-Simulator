package pl.agh.edu.actor.utils.wrapper;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.ray3k.tenpatch.TenPatchDrawable;

import pl.agh.edu.actor.GameSkin;

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

	private void init() {
		this.setActor(innerTable);
		innerTable.setFillParent(true);
		innerTable.pad(0);
	}

	public void setBackground(String backgroundPatch) {
		innerTable.setBackground(new NinePatchDrawable(GameSkin.getInstance().getPatch(backgroundPatch)));
	}

	public void set10PatchBackground(String backgroundPatch) {
		innerTable.setBackground(new TenPatchDrawable(GameSkin.getInstance().get(backgroundPatch, TenPatchDrawable.class)));
	}
}
