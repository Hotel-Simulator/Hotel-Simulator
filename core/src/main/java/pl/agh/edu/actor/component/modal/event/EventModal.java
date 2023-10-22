package pl.agh.edu.actor.component.modal.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.component.button.LabeledButton;
import pl.agh.edu.actor.utils.label.ReplaceLanguageLabel;
import pl.agh.edu.actor.utils.resolution.Size;
import pl.agh.edu.actor.utils.wrapper.WrapperTable;
import pl.agh.edu.audio.SoundAudio;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.model.event.EventData;

import static pl.agh.edu.actor.utils.FontType.BODY_1;
import static pl.agh.edu.actor.utils.FontType.BODY_2;
import static pl.agh.edu.actor.utils.FontType.H3;
import static pl.agh.edu.actor.utils.FontType.H4;

public class EventModal extends WrapperTable {
	private final Container<Image> imageContainer = new Container<>();
	private final ReplaceLanguageLabel titleLabel;
	private final ReplaceLanguageLabel descriptionLabel;
	public EventModal(EventData eventData, Runnable closeHandler) {
		this.setBackground("modal-glass-background");

		innerTable.pad(EventModalStyle.getPadding());

		titleLabel = new ReplaceLanguageLabel(
				eventData.title().property,
				H4.getName(),
				eventData.title().stringsWithReplacements
		);
		titleLabel.setAlignment(Align.center,Align.center);

		Table mainTable = new Table();

		Image image = new Image(GameSkin.getInstance().getDrawable(eventData.imagePath()));
		image.setFillParent(true);
		imageContainer.setActor(image);

		descriptionLabel = new ReplaceLanguageLabel(
				eventData.description().property,
				BODY_2.getName(),
				eventData.description().stringsWithReplacements
		);
		descriptionLabel.setWrap(true);
		descriptionLabel.setAlignment(Align.center,Align.center);

		mainTable.add(imageContainer).pad(EventModalStyle.getPadding());
		mainTable.add(descriptionLabel).grow();

		LabeledButton okButton = new LabeledButton(Size.MEDIUM, "event.label.ok");

		innerTable.add(titleLabel).center().growX().row();
		innerTable.add(mainTable).center().grow().row();
		innerTable.add(okButton).center().growX().row();
		okButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundAudio.BUTTON_1.play();
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
		if(this.getParent()!=null){
			innerTable.setBounds(
					this.getParent().getX(),
					this.getParent().getY(),
					this.getWidth(),
					this.getHeight()
			);
			this.setResetAnimationPosition(
					this.getParent().getX()+(GraphicConfig.getResolution().WIDTH-this.getWidth())/2,
					this.getParent().getY()+(GraphicConfig.getResolution().HEIGHT-this.getHeight())/2
			);
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

		public static String getDescriptionFont(){
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL, MEDIUM -> BODY_2.getName();
                case LARGE -> BODY_1.getName();
			};
		}

		public static String getTitleFont(){
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> H4.getName();
				case MEDIUM,LARGE -> H3.getName();
			};
		}
	}
}
