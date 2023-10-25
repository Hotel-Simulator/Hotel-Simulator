package pl.agh.edu.actor.component.table;

import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.compression.lzma.Base;
import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.FontType;
import pl.agh.edu.actor.utils.LanguageLabel;
import pl.agh.edu.actor.utils.wrapper.WrapperTable;
import pl.agh.edu.config.GraphicConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.agh.edu.actor.component.table.BaseTable.BaseTableStyle.getCellPadding;
import static pl.agh.edu.actor.component.table.BaseTable.BaseTableStyle.getRowSpacing;
import static pl.agh.edu.actor.component.table.CreditTable.CreditTableStyle.getSeparatorWidth;
import static pl.agh.edu.actor.component.table.CreditTable.CreditTableStyle.getTableFont;

public abstract class BaseTable extends WrapperTable {
	protected int noColumns;
	protected Table contentRows = new Table();
	protected ScrollPane scrollPane = new ScrollPane(contentRows, GameSkin.getInstance(), "transparent");

	public BaseTable() {
		super();

	}

	protected void addRow(BaseRow row, Table targetTable){
		row.align(Align.topLeft);
		targetTable.add(row).spaceBottom(getRowSpacing()).growX().align(Align.topLeft);
		targetTable.row();
	}
	protected void deleteRow(BaseRow row) {
		Cell<BaseRow> cell = contentRows.getCell(row);
		cell.space(0);
		contentRows.getCell(row).clearActor();
	}
	protected abstract class BaseRow extends WrapperTable {


		BaseRow(List<String> columnNames) {
			super();
			noColumns = columnNames.size();
			insertActorsToRow(columnNames.stream().map(s -> new LanguageLabel(s, getTableFont().getName())).collect(Collectors.toList()));
			this.setBackground("table-header-background");
		}
		BaseRow(){
			super();
		}

		public void insertActorsToRow(List<Actor> actors) {
			IntStream.range(0, noColumns).forEach(i -> {
				Actor actor = actors.get(i);
				Container<Actor> container = new Container<>(actor);
				this.innerTable.add(container).growX().uniform().center().padLeft(getCellPadding()).padRight(getCellPadding());
				if (i != noColumns - 1)
					this.innerTable.add(new Image(GameSkin.getInstance().getPatch("table-separator-line"))).width(getSeparatorWidth()).growY().center();
			});
		}




	}

	public abstract static class BaseTableStyle {
		public static float getCellPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 15f;
				case MEDIUM -> 20f;
				case LARGE -> 30f;
			};
		}

		public static float getRowSpacing() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 10f;
				case MEDIUM -> 15f;
				case LARGE -> 20f;
			};
		}

		public static float getRowHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}

		public static FontType getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> FontType.BODY_1;
				case MEDIUM -> FontType.BODY_2;
				case LARGE -> FontType.H4;
			};
		}

		public static float getSeparatorWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 1f;
				case MEDIUM -> 2f;
				case LARGE -> 3f;
			};
		}
	}

}
