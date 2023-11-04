package pl.agh.edu.ui.component.table;

import static com.badlogic.gdx.utils.Align.left;
import static com.badlogic.gdx.utils.Align.topLeft;
import static pl.agh.edu.ui.utils.SkinFont.BODY_1;
import static pl.agh.edu.ui.utils.SkinFont.BODY_2;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.CustomScrollPane;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public abstract class BaseTable extends WrapperTable {
	protected Table contentRows = new Table();
	protected ScrollPane scrollPane = new CustomScrollPane(contentRows, skin, "transparent");

	public BaseTable(List<LanguageString> columnNames) {
		super();
		innerTable.align(left);

		BaseRow headerRow = new HeaderRow(columnNames);
		headerRow.align(topLeft);
		innerTable.add(headerRow).growX()
				.height(BaseTableStyle.getRowHeight())
				.spaceBottom(BaseTableStyle.getRowSpacing());

		Drawable knobDrawable = skin.getDrawable("scroll-pane-knob");
		Image knobImage = new Image(knobDrawable);
		knobImage.setVisible(false);
		innerTable.add(knobImage).row();

		scrollPane.setFadeScrollBars(false);
		scrollPane.setWidth(scrollPane.getWidth() + scrollPane.getScrollWidth());
		innerTable.add(scrollPane).growX().colspan(2);
		scrollPane.setForceScroll(false, true);
	}

	protected void addRow(BaseRow row) {
		row.align(topLeft);
		contentRows.add(row).spaceBottom(BaseTableStyle.getRowSpacing()).growX().row();
	}

	protected void deleteRow(BaseRow row) {
		Cell<BaseRow> cell = contentRows.getCell(row);
		cell.space(0);
		contentRows.getCell(row).clearActor();
	}

	private static class HeaderRow extends BaseRow {
		public HeaderRow(List<LanguageString> columnNames) {
			super();
			insertActorsToRow(columnNames.stream().map(s -> new LanguageLabel(s, BaseTableStyle.getHeaderFont())).collect(Collectors.toList()));
		}
	}

	protected abstract static class BaseRow extends WrapperTable {
		BaseRow() {
			super();
			this.setBackground("table-row-background");
		}

		public void insertActorsToRow(List<Actor> actors) {
			actors.forEach(actor -> {
				Container<Actor> container = new Container<>(actor);
				this.innerTable
						.add(container).growX().uniform().center()
						.height(BaseTableStyle.getRowHeight())
						.padLeft(BaseTableStyle.getCellPadding())
						.padRight(BaseTableStyle.getCellPadding());
				if (actor != actors.get(actors.size() - 1))
					this.innerTable
							.add(new Image(skin.getPatch("table-separator-line")))
							.width(BaseTableStyle.getSeparatorWidth()).growY().center();
			});
		}

	}

	public static class BaseTableStyle {
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
				case SMALL -> 50f;
				case MEDIUM -> 60f;
				case LARGE -> 70f;
			};
		}

		public static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> BODY_1.getName();
				case MEDIUM -> BODY_2.getName();
				case LARGE -> H4.getName();
			};
		}

		public static String getHeaderFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> BODY_2.getName();
				case MEDIUM -> BODY_1.getName();
				case LARGE -> H4.getName();
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
