package pl.agh.edu.ui.utils.wrapper;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.utils.LanguageString;

public abstract class WrapperDoubleTable extends WrapperTable {
	public final Table rightTable = new Table();
	public final Table leftTable = new Table();

	public WrapperDoubleTable(LanguageString languageString) {
		super(languageString);
		this.init();
	}

	public WrapperDoubleTable() {
		super();
		this.init();
	}

	private void init() {
		innerTable.add(leftTable).grow().uniform().minHeight(0f).pad(0f);
		innerTable.add(rightTable).grow().uniform().minHeight(0f).pad(0f);
	}
}
