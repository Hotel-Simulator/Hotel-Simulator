package pl.agh.edu.ui.component.tooltip;

import static pl.agh.edu.ui.utils.SkinFont.BODY2;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.utils.LanguageString;

public class DescriptionTooltip extends BaseTooltip {
	public DescriptionTooltip(LanguageString title, LanguageString description) {
		super(new DescriptionTooltipTable(title, description));
		this.getContainer().fill();
	}

	private static class DescriptionTooltipTable extends BaseTooltipTable {

		public DescriptionTooltipTable(LanguageString title, LanguageString description) {
			super();

			LanguageLabel titleLabel = new LanguageLabel(title, H4.getName());
			titleLabel.setWrap(true);
			innerTable.add(titleLabel).growX().row();

			LanguageLabel descriptionLabel = new LanguageLabel(description, BODY2.getName());
			descriptionLabel.setWrap(true);
			innerTable.add(descriptionLabel).growX().row();
		}
	}
}
