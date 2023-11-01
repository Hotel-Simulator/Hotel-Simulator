package pl.agh.edu.ui.component.tooltip;

import static pl.agh.edu.ui.utils.SkinFont.BODY_2;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class DescriptionTooltip extends Tooltip<WrapperTable> {

	private static final TooltipManager manager = new TooltipManager();

	public DescriptionTooltip(LanguageString title, LanguageString description) {
		super(new DescriptionTooltipTable(title, description), createTooltipManager());
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
		this.getContainer().width(DescriptionTooltipStyle.getWidth());
	}

	private static class DescriptionTooltipTable extends WrapperTable {

		private final LanguageLabel titleLabel;
		private final LanguageLabel descriptionLabel;

		public DescriptionTooltipTable(LanguageString title, LanguageString description) {
			super();
			this.setBackground("modal-glass-background");
			innerTable.pad(20f);

			titleLabel = new LanguageLabel(title, H4.getName());
			titleLabel.setWrap(true);
			innerTable.add(titleLabel).growX().row();

			descriptionLabel = new LanguageLabel(description, BODY_2.getName());
			descriptionLabel.setWrap(true);
			innerTable.add(descriptionLabel).growX().row();
			innerTable.top();

		}
	}

	private static class DescriptionTooltipStyle {
		private static float getWidth() {
			System.out.println(GraphicConfig.getResolution().SIZE);
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 300f;
				case MEDIUM -> 500f;
				case LARGE -> 800f;
			};
		}
	}
}
