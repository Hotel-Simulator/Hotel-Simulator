package pl.agh.edu.actor.component.table;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

import pl.agh.edu.actor.utils.Font;
import pl.agh.edu.actor.utils.WrapperTable;
import pl.agh.edu.config.GraphicConfig;

public abstract class BaseTable extends WrapperTable {

	public BaseTable() {
		super();
	}

	public abstract class BaseRow extends WrapperTable {
		BaseRow() {
			super();
		}

		public abstract void insertActorsToRow(Actor... actors);
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

		public static BitmapFont getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> Font.BODY1;
				case MEDIUM -> Font.BODY2;
				case LARGE -> Font.H4;
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


