package pl.agh.edu.ui.component.tooltip;

import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.ui.component.label.LanguageLabel;
import static pl.agh.edu.ui.utils.SkinFont.BODY_2;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import java.util.List;

public class EventDescriptionTooltip extends BaseTooltip {
	public EventDescriptionTooltip(List<CalendarEvent> events) {
		super(new EventDescriptionTooltipTable(events));
		this.getContainer().fill();
	}
	private static class EventDescriptionTooltipTable extends BaseTooltipTable {

		public EventDescriptionTooltipTable(List<CalendarEvent> events) {
			super();
			this.setBackground("modal-glass-background");
			innerTable.pad(20f);
			events.forEach(calendarEvent -> {
				LanguageLabel titleLabel = new LanguageLabel(calendarEvent.title(), H4.getName());
				titleLabel.setWrap(true);
				innerTable.add(titleLabel).growX().row();

				LanguageLabel descriptionLabel = new LanguageLabel(calendarEvent.description(), BODY_2.getName());
				descriptionLabel.setWrap(true);
				innerTable.add(descriptionLabel).growX().row();
			});
		}
	}
}
