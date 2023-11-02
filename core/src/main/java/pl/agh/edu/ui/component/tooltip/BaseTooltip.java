package pl.agh.edu.ui.component.tooltip;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.utils.Null;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
public abstract class BaseTooltip extends Tooltip<WrapperTable> {

	private static final TooltipManager manager = new TooltipManager();

	protected BaseTooltip(BaseTooltipTable wrapperTable) {
		super(wrapperTable, createTooltipManager());
		this.getContainer().fill();
	}

	private static TooltipManager createTooltipManager() {
		manager.initialTime = 0.5f;
		manager.resetTime = 0.5f;
		manager.subsequentTime = 0.5f;
		return manager;
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
		super.enter(event, x, y, pointer, fromActor);
		this.getContainer().width(BaseTooltipStyle.getWidth());
	}

	protected abstract static class BaseTooltipTable extends WrapperTable {

		public BaseTooltipTable() {
			super();
			this.setBackground("modal-glass-background");
			innerTable.pad(20f);
			innerTable.top();
		}
	}

	private static class BaseTooltipStyle {
		private static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 300f;
				case MEDIUM -> 500f;
				case LARGE -> 800f;
			};
		}
	}
}
