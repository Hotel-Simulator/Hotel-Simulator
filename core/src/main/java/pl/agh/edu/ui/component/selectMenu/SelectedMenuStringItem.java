package pl.agh.edu.ui.component.selectMenu;

public class SelectedMenuStringItem extends SelectMenuItem {
	public SelectedMenuStringItem(String text) {
		super(text, () -> text);
	}
}
