package pl.agh.edu.actor.component.table;

import static pl.agh.edu.actor.component.table.BaseTable.BaseTableStyle.*;

import java.util.List;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.utils.LanguageLabel;
import pl.agh.edu.actor.utils.WrapperTable;
import pl.agh.edu.config.GraphicConfig;

public abstract class BaseTable extends WrapperTable {
	private final int noColumns;
	private final float separatorWidth = 2f;

	public BaseTable(List<String> columnNames) {
		super();

		this.noColumns = columnNames.size();
		align(Align.bottomLeft);

		BaseRow header = createRow(columnNames.stream().map(s -> new LanguageLabel(s, getFont())).toArray(Actor[]::new));
		header.setBackground("table-header-background");
		header.align(Align.bottomLeft);
		innerTable.add(header).space(getRowSpacing()).growX();
	}

	public final BaseRow createRow(Actor... actors) {
		BaseRow baseRow = new BaseRow(actors);
		innerTable.row();
		return baseRow;
	}

	public class BaseRow extends WrapperTable {
		BaseRow(Actor... actors) {
			super();
			IntStream.range(0, noColumns).forEach(i -> {
				Actor actor = actors[i];
				Container<Actor> container = new Container<>(actor);
				container.pad(1f);
				this.innerTable.add(container).growX().uniform().height(getRowHeight()).center().padLeft(getCellPadding()).padRight(getCellPadding());
				if (i != noColumns - 1)
					this.innerTable.add(new Image(HotelSkin.getInstance().getPatch("table-separator-line"))).width(separatorWidth).growY().center();
			});
			this.setBackground("table-row-background");
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
				case SMALL -> 15f;
				case MEDIUM -> 20f;
				case LARGE -> 30f;
			};
		}

		public static float getRowHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}

		public static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> "body2";
				case MEDIUM -> "body1";
				case LARGE -> "h4";
			};
		}
	}

}
