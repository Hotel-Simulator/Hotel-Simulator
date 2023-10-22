package pl.agh.edu.actor.component.modal.options;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import pl.agh.edu.actor.shader.BlurShader;
import pl.agh.edu.actor.utils.wrapper.WrapperContainer;
import pl.agh.edu.model.time.Time;

public class OptionsWrapper extends WrapperContainer<OptionModal> {
	private final InputMultiplexer inputMultiplexer;
	private final BlurShader blurShader;
	private final Stage mainStage;
	private final Stage optionsStage;
	private final OptionModal optionModal = new OptionModal(this::closeOptions);

	public OptionsWrapper(
			InputMultiplexer inputMultiplexer,
			BlurShader blurShader,
			Stage mainStage,
			Stage optionsStage) {
		super();
		this.inputMultiplexer = inputMultiplexer;
		this.blurShader = blurShader;
		this.mainStage = mainStage;
		this.optionsStage = optionsStage;
		this.setFillParent(true);
	}

	public boolean isOptionsOpen() {
		return this.getActor() != null;
	}
	public int countContainersWithActors() {
		int count = 0;
		for (Actor actor : optionsStage.getActors()) {
			if (actor instanceof Container container && container.getActor() != null) {
				count++;
			}
		}
		return count;
	}

	private boolean isStageActive() {
		return countContainersWithActors() > 0;
	}
	private boolean isStageReadyToClose() {
		return countContainersWithActors() <= 1;
	}

	private void openOptions() {
		if (isOptionsOpen()) return;
		if(!isStageActive()){
			Time.getInstance().stop();
			inputMultiplexer.setProcessors(optionsStage);
			blurShader.startBlur();
		}
		this.setActor(optionModal);
		optionModal.runVerticalFadeInAnimation();
	}

	private void closeOptions() {
		if (!isOptionsOpen()) return;
		if(isStageReadyToClose()){
			inputMultiplexer.setProcessors(mainStage);
			blurShader.stopBlur();
		}
		optionModal.runVerticalFadeOutAnimation();
	}

	public Runnable getOptionHandler() {
		return () -> {
			if (isOptionsOpen())
				closeOptions();
			else
				openOptions();
		};
	}
}
