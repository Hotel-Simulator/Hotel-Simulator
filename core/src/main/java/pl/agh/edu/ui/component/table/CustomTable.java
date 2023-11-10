package pl.agh.edu.ui.component.table;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.CustomScrollPane;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.utils.SkinFont.BODY_1;
import static pl.agh.edu.ui.utils.SkinFont.BODY_2;
import static pl.agh.edu.ui.utils.SkinFont.H4;
import static pl.agh.edu.utils.ListUtils.zipLists;

public class CustomTable<DataType> extends WrapperTable {
	protected Table contentRows = new Table();
	protected ScrollPane scrollPane = new CustomScrollPane(contentRows,skin, "default");
	private final List<Integer> sizeList;
	private final List<Function<DataType,Actor>> mapperList;
	private CustomTable(List<Integer> sizeList, List<Function<DataType,Actor>> mapperList, List<LanguageString> headerNameList) {
		super();
		this.sizeList = sizeList;
		this.mapperList = mapperList;
		innerTable.add(new HeaderRow(headerNameList)).growX().row();
		this.setUpScrollPane();
	}

	private void setUpScrollPane(){
		scrollPane.setWidth(scrollPane.getWidth() + scrollPane.getScrollWidth());
		innerTable.add(scrollPane).grow();
		scrollPane.setForceScroll(false, true);
		scrollPane.setSmoothScrolling(true);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setOverscroll(false, false);
		scrollPane.setupFadeScrollBars(1f, 1f);
	}
	public Actor addRow(DataType dataType){
		BaseRow newBaseRow = new BaseRow();
		zipLists(mapperList,sizeList).forEach(entry -> newBaseRow.insert(entry.first().apply(dataType), entry.second()));
		contentRows.add(newBaseRow).growX().row();
		return newBaseRow;
	}
	private abstract class TableRow extends WrapperTable{
		private TableRow() {
			super();
			this.setBackground("table-row-background");
		}
		public void insert(Actor actor, int size) {
			if(!innerTable.getChildren().isEmpty()) innerTable.add(new Image(skin.getPatch("table-separator-line"))).growY().center();
			innerTable.add(actor).colspan(size).center().growX();
		}
	}

	private class HeaderRow extends TableRow {
		public HeaderRow(List<LanguageString> columnNames) {
			super();
			zipLists(columnNames,sizeList).forEach(entry -> insert(createLanguageLabel(entry.first()), entry.second()));

			Drawable knobDrawable = skin.getDrawable("scroll-pane-knob");
			Image knobImage = new Image(knobDrawable);
			knobImage.setVisible(false);
			innerTable.add(knobImage).row();
		}
	}
	private class BaseRow extends TableRow {
		private BaseRow(){
			super();
		}
	}

	public static class CustomTableBuilder<T> {
		private final List<Function<T,Actor>> actorSchematicList = new ArrayList<>();
		private final List<Integer> sizeList = new ArrayList<>();
		private final List<LanguageString> headerNameList = new ArrayList<>();
		public CustomTableBuilder<T> addColumn(LanguageString columnName, Function<T, Actor> actorSchematic, int size) {
			actorSchematicList.add(actorSchematic);
			sizeList.add(size);
			headerNameList.add(columnName);
			return this;
		}

		public CustomTable<T> build() {
            return new CustomTable<>(sizeList, actorSchematicList,headerNameList);
		}
	}

	public static LanguageLabel createLanguageLabel(LanguageString languageString) {
		LanguageLabel label = new LanguageLabel(languageString, BaseTableStyle.getHeaderFont());
		label.setUpResolutionChangeHandler(() -> label.setFont(BaseTableStyle.getHeaderFont()));
		label.setAlignment(center, center);
		return label;
	}

	public static CustomLabel createCustomLabel(String text){
		CustomLabel label = new CustomLabel(BaseTableStyle.getHeaderFont());
		label.setText(text);
		label.setAlignment(center, center);
		label.setUpResolutionChangeHandler(() -> label.setFont(BaseTableStyle.getHeaderFont()));
		return label;
	}

	private static class BaseTableStyle {
		private static String getHeaderFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> BODY_2.getName();
				case MEDIUM -> BODY_1.getName();
				case LARGE -> H4.getName();
			};
		}
	}
}
