package pl.agh.edu.ui.component.table;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.compression.lzma.Base;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.component.CustomScrollPane;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.top;
import static com.badlogic.gdx.utils.Align.topLeft;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.audio.SoundAudio.POP;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._300;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._900;
import static pl.agh.edu.ui.utils.SkinFont.BODY1;
import static pl.agh.edu.ui.utils.SkinFont.BODY2;
import static pl.agh.edu.ui.utils.SkinFont.BODY3;
import static pl.agh.edu.ui.utils.SkinFont.H2;
import static pl.agh.edu.ui.utils.SkinFont.H3;
import static pl.agh.edu.ui.utils.SkinFont.H4;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE1;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE2;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE3;
import static pl.agh.edu.utils.ListUtils.zipLists;

public class CustomTable<DataType> extends WrapperTable {
	protected Table contentRows = new Table();
	protected ScrollPane scrollPane = new CustomScrollPane(contentRows,skin, "transparent");
	private final List<Integer> sizeList;
	private final List<Function<DataType,Actor>> mapperList;
	private CustomTable(List<Integer> sizeList, List<Function<DataType,Actor>> mapperList, List<LanguageString> headerNameList) {
		super();
		this.sizeList = sizeList;
		this.mapperList = mapperList;
		HeaderRow headerRow = new HeaderRow(headerNameList);
		innerTable.add(headerRow).growX().height(BaseTableStyle.getHeaderHeight());

		contentRows.top();
		innerTable.align(topLeft);

		Drawable knobDrawable = skin.getDrawable("scroll-pane-knob");
		Image knobImage = new Image(knobDrawable);
		knobImage.setVisible(false);
		innerTable.add(knobImage).row();

		this.align(top);
		innerTable.add(scrollPane).colspan(2).grow();
		this.setUpScrollPane();

		this.setResolutionChangeHandler(this::resize);
	}

	private void setUpScrollPane(){
		scrollPane.setWidth(scrollPane.getWidth() + scrollPane.getScrollWidth());
		scrollPane.setForceScroll(false, true);
		scrollPane.setSmoothScrolling(true);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setOverscroll(false, false);
		scrollPane.setupFadeScrollBars(1f, 1f);
	}
	private void addRow(BaseRow baseRow, DataType dataType){
		zipLists(mapperList,sizeList).forEach(entry -> baseRow.insert(entry.first().apply(dataType), entry.second()));
		contentRows.add(baseRow).growX()
				.height(BaseTableStyle.getRowHeight())
				.padBottom(BaseTableStyle.getRowSpacing())
				.padTop(contentRows.getChildren().size==1 ? BaseTableStyle.getHeaderSpacing() : 0)
				.row();
	}
	public void addRow(DataType dataType, Runnable action, Boolean special){
		addRow(new BaseRow(special,action),dataType);
	}
	public void addRow(DataType dataType, Runnable action){
		addRow(dataType,action,false);
	}
	public void addRow(DataType dataType, Boolean special){
		addRow(new BaseRow(special),dataType);
	}
	public void addRow(DataType dataType){
		addRow(dataType,false);
	}
	public void addRowWithRemove(DataType dataType, Runnable action, Boolean special){
		addRow(new BaseRow(special,action,true),dataType);
	}
	public void addRowWithRemove(DataType dataType, Runnable action){
		addRowWithRemove(dataType,action,false);
	}
	public void resize(){
		contentRows.getCells().first().padTop(BaseTableStyle.getHeaderSpacing());
		contentRows.getCells().forEach( cell -> {
			cell.padBottom(BaseTableStyle.getRowSpacing());
			cell.height(BaseTableStyle.getRowHeight());
		});
		scrollPane.setScrollingDisabled(false,contentRows.getHeight()<=scrollPane.getHeight());
	}
	private abstract static class TableRow extends WrapperTable{
		private final String separatorName;
		private final String backgroundName;
		private List<Container<Image>> separatorList = new ArrayList<>();
		private TableRow(Boolean special) {
			if(special){
				this.separatorName = "custom-table-separator-special";
				this.backgroundName = "custom-table-special";
			}
			else{
				this.separatorName = "custom-table-separator";
				this.backgroundName = "custom-table";
			}
			this.setBackground(backgroundName+"-up");
		}
		protected void setUpListener(Runnable action){
			this.setTouchable(Touchable.enabled);
			this.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					CLICK.playSound();
					action.run();
				}

				@Override
				public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
					setBackground(backgroundName + "-over");
					separatorList.forEach(container -> container.setActor(new Image(skin.getPatch(separatorName+"-over"))));
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					setBackground(backgroundName + "-up");
					separatorList.forEach(container -> container.setActor(new Image(skin.getPatch(separatorName+"-up"))));
				}
			});
		}
		public void insert(Actor actor, int size) {
			if(!innerTable.getChildren().isEmpty()) innerTable.add(createSeparatorImage(separatorName+"-up")).growY();
			innerTable.add(actor).colspan(size).center().grow();
		}
		private Container<Image> createSeparatorImage(String separatorName){
			Container<Image> container = new Container<>(new Image(skin.getPatch(separatorName)));
			container.fill();
			container.pad(BaseTableStyle.getSeparateLinePadding());
			separatorList.add(container);
			return container;
		}
	}

	private class HeaderRow extends TableRow {
		public HeaderRow(List<LanguageString> columnNames) {
			super(false);
			zipLists(columnNames,sizeList).forEach(entry -> insert(createHeaderLabel(entry.first()), entry.second()));
		}
		private static LanguageLabel createHeaderLabel(LanguageString languageString) {
			LanguageLabel label = new LanguageLabel(languageString, BaseTableStyle.getHeaderFont());
			label.setUpResolutionChangeHandler(() -> label.setFont(BaseTableStyle.getHeaderFont()));
			label.setAlignment(center, center);
			return label;
		}
	}
	private class BaseRow extends TableRow {

		private BaseRow(Boolean special){
			super(special);
		}
		private BaseRow(Boolean special,Runnable action){
			super(special);
			this.setUpListener(action);
		}

		private BaseRow(Boolean special,Runnable action,Boolean removeAfterAction){
			super(special);
			if(removeAfterAction){
				this.setUpListener(() -> {
					action.run();
					this.remove();
				});
			}
			else{
				this.setUpListener(action);
			}
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
		LanguageLabel label = new LanguageLabel(languageString, BaseTableStyle.getFont());
		label.setUpResolutionChangeHandler(() -> label.setFont(BaseTableStyle.getFont()));
		label.setAlignment(center, center);
		return label;
	}

	public static CustomLabel createCustomLabel(String text){
		CustomLabel label = new CustomLabel(BaseTableStyle.getFont());
		label.setText(text);
		label.setAlignment(center, center);
		label.setUpResolutionChangeHandler(() -> label.setFont(BaseTableStyle.getFont()));
		return label;
	}

	private static class BaseTableStyle {

		public static float getHeaderHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 50f;
				case MEDIUM -> 60f;
				case LARGE -> 70f;
			};
		}

		public static float getHeaderSpacing() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 10f;
				case MEDIUM -> 15f;
				case LARGE -> 20f;
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
				case SMALL -> 60f;
				case MEDIUM -> 80f;
				case LARGE -> 100f;
			};
		}
		private static String getHeaderFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SUBTITLE3.getName();
				case MEDIUM -> SUBTITLE2.getName();
				case LARGE -> SUBTITLE1.getName();
			};
		}
		public static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> BODY3.getName();
				case MEDIUM -> BODY2.getName();
				case LARGE -> BODY1.getName();
			};
		}

		public static float getSeparateLinePadding(){
			return 10f;
		}
	}
}
