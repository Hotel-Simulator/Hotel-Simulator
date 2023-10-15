package pl.agh.edu.actor.component.table;

import com.badlogic.gdx.scenes.scene2d.Actor;

import pl.agh.edu.actor.utils.WrapperTable;

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
		public BaseTableStyle() {
		}
	}

}


