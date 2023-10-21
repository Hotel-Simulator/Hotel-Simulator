package pl.agh.edu.actor.component.modal.options;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.actor.shader.BlurShader;
import pl.agh.edu.actor.utils.wrapper.WrapperContainer;
import pl.agh.edu.model.time.Time;

public class OptionsWrapper extends WrapperContainer<OptionModal> {
	private boolean isOptionsOpen = false;
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
		this.debugAll();
	}

	private void openOptions() {
		if (isOptionsOpen)
			return;
		Time.getInstance().stop();
		inputMultiplexer.setProcessors(optionsStage);
		isOptionsOpen = true;
		this.setActor(optionModal);
		blurShader.startBlur();
		optionModal.runVerticalFadeInAnimation();
	}

	private void closeOptions() {
		if (!isOptionsOpen)
			return;
		inputMultiplexer.setProcessors(mainStage);
		isOptionsOpen = false;
		blurShader.stopBlur();
		optionModal.runVerticalFadeOutAnimation();
	}

	public Runnable getOptionHandler() {
		return () -> {
			if (isOptionsOpen)
				closeOptions();
			else
				openOptions();
		};
	}

	public void render() {
		optionsStage.act();
		optionsStage.draw();
	}
}
