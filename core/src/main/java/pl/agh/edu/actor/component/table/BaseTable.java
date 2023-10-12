package pl.agh.edu.actor.component.table;

import java.util.List;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.rating.Star;
import pl.agh.edu.actor.utils.LanguageLabel;
import pl.agh.edu.actor.utils.WrapperTable;

public abstract class BaseTable extends WrapperTable {
	private final int noColumns;
	private final float separatorWidth;
	private final float cellPadding = 10f;
	protected final float rowSpacing = 20f;

	public BaseTable(List<String> columnNames) {
		super();

		this.noColumns = columnNames.size();
		this.separatorWidth = 2f;

		align(Align.bottomLeft);

		BaseRow header = createRow(columnNames.stream().map(s -> new LanguageLabel(s, "body2")).toArray(Actor[]::new));
		header.setBackground("table-header-background");
		header.align(Align.bottomLeft);
		innerTable.add(header).space(rowSpacing).growX();
	}

	public final BaseRow createRow(Actor... actors) {
		BaseRow baseRow = new BaseRow();
		IntStream.range(0, noColumns).forEach(i -> {
			Actor actor = actors[i];
			Container<Actor> container = new Container<>(actor);
			container.pad(1f);
			baseRow.innerTable.add(container).growX().uniform().height(Star.getSize() + 20f).center().padLeft(cellPadding).padRight(cellPadding);
			if (i != noColumns - 1)
				baseRow.innerTable.add(new Image(HotelSkin.getInstance().getPatch("table-separator-line"))).width(separatorWidth).growY().center();
		});

		baseRow.setBackground("table-row-background");
		innerTable.row();
		return baseRow;
	}

	public static class BaseRow extends WrapperTable {
		BaseRow() {
			super();
		}
	}

}
