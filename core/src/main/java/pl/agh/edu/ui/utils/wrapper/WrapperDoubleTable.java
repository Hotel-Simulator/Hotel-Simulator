package pl.agh.edu.ui.utils.wrapper;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.ray3k.tenpatch.TenPatchDrawable;

import pl.agh.edu.utils.LanguageString;

public abstract class WrapperDoubleTable extends WrapperContainer<Table> {
	public final Table rightTable = new Table();
	public final Table leftTable = new Table();
	private final Table innerTable = new Table();

	public WrapperDoubleTable(LanguageString languageString) {
		super(languageString);
		this.init();
	}

	public WrapperDoubleTable() {
		super();
		this.init();
	}

	private void init() {
		innerTable.setFillParent(true);
		this.setActor(innerTable);
		innerTable.add(leftTable).grow().uniform().minHeight(0f).pad(0f);
		innerTable.add(rightTable).grow().uniform().minHeight(0f).pad(0f);
		innerTable.pad(0);
	}

	public void setBackground(String backgroundPatch) {
		innerTable.setBackground(new NinePatchDrawable(skin.getPatch(backgroundPatch)));
	}

	public void set10PatchBackground(String backgroundPatch) {
		innerTable.setBackground(new TenPatchDrawable(skin.get(backgroundPatch, TenPatchDrawable.class)));
	}
}
