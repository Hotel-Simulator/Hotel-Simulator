package pl.agh.edu.ui.component.modal.utils;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public abstract class BaseModal extends WrapperTable {

	public BaseModal() {
		super();
		this.setBackground("modal-glass-background");
	}

	@Override
	public void validate() {
		super.validate();
		if (this.getParent() != null) {
			innerTable.setBounds(
					this.getParent().getX(),
					this.getParent().getY(),
					this.getWidth(),
					this.getHeight());
			this.setResetAnimationPosition(
					this.getParent().getX() + (GraphicConfig.getResolution().WIDTH - this.getWidth()) / 2,
					this.getParent().getY() + (GraphicConfig.getResolution().HEIGHT - this.getHeight()) / 2);
		}
	}
}
