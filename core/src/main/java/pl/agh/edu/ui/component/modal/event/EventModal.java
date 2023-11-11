package pl.agh.edu.ui.component.modal.event;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.resolution.Size.MEDIUM;
import static pl.agh.edu.ui.utils.SkinFont.BODY1;
import static pl.agh.edu.ui.utils.SkinFont.BODY2;
import static pl.agh.edu.ui.utils.SkinFont.H3;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.event.EventModalData;
import pl.agh.edu.ui.component.button.LabeledButton;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class EventModal extends WrapperTable {
	private final Container<Image> imageContainer = new Container<>();
	private final LanguageLabel titleLabel;
	private final LanguageLabel descriptionLabel;

	public EventModal(EventModalData eventModalData, Runnable closeHandler) {
		this.setBackground("modal-glass-background");

		innerTable.pad(EventModalStyle.getPadding());

		titleLabel = new LanguageLabel(eventModalData.title(), H4.getName());
		titleLabel.setAlignment(center, center);

		Table mainTable = new Table();

		Image image = new Image(skin.getDrawable(eventModalData.imagePath()));
		image.setFillParent(true);
		imageContainer.setActor(image);

		descriptionLabel = new LanguageLabel(eventModalData.description(), BODY2.getName());
		descriptionLabel.setWrap(true);
		descriptionLabel.setAlignment(center, left);

		mainTable.add(imageContainer).pad(EventModalStyle.getPadding());
		mainTable.add(descriptionLabel).grow();

		LabeledButton okButton = new LabeledButton(MEDIUM, new LanguageString("event.label.ok"));

		innerTable.add(titleLabel).center().growX().row();
		innerTable.add(mainTable).center().grow().row();
		innerTable.add(okButton).center().growX().row();
		okButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				CLICK.playSound();
				closeHandler.run();
			}
		});
		this.resize();
		this.setResolutionChangeHandler(this::resize);
		this.onLanguageChange();
	}

	public void resize() {
		this.descriptionLabel.setFont(EventModalStyle.getDescriptionFont());
		this.titleLabel.setFont(EventModalStyle.getTitleFont());
		imageContainer.size(EventModalStyle.getIconSize(), EventModalStyle.getIconSize());
		this.resetAnimationPosition();
		this.validate();
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

	private static class EventModalStyle {
		public static float getIconSize() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 100f;
				case MEDIUM -> 200f;
				case LARGE -> 400f;
			};
		}

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}

		public static String getDescriptionFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL, MEDIUM -> BODY2.getName();
				case LARGE -> BODY1.getName();
			};
		}

		public static String getTitleFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> H4.getName();
				case MEDIUM, LARGE -> H3.getName();
			};
		}
	}
}
