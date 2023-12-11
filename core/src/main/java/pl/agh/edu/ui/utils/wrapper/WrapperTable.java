package pl.agh.edu.ui.utils.wrapper;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.ray3k.tenpatch.TenPatchDrawable;

import pl.agh.edu.utils.LanguageString;

public abstract class WrapperTable extends WrapperContainer<Table> {
	public final Table innerTable = new Table();

	public WrapperTable(LanguageString languageString) {
		super(languageString);
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
		innerTable.setBackground(new NinePatchDrawable(getGameSkin().getPatch(backgroundPatch)));
	}

	public void set10PatchBackground(String backgroundPatch) {
		innerTable.setBackground(new TenPatchDrawable(getGameSkin().get(backgroundPatch, TenPatchDrawable.class)));
	}
}
