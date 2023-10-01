package pl.agh.edu.actor.component.selectMenu;

public class SelectedMenuStringItem extends SelectMenuItem {
	public SelectedMenuStringItem(String text) {
		super(text, () -> text);
	}
}
