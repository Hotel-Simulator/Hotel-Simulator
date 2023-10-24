package pl.agh.edu.ui.component.modal;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.shader.BlurShader;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public abstract class BaseModalWrapper extends WrapperContainer<WrapperTable> {
	private final InputMultiplexer inputMultiplexer;
	private final BlurShader blurShader;
	private final Stage mainStage;
	private final Stage modalStage;

	public BaseModalWrapper(
			InputMultiplexer inputMultiplexer,
			BlurShader blurShader,
			Stage mainStage,
			Stage modalStage) {
		super();
		this.inputMultiplexer = inputMultiplexer;
		this.blurShader = blurShader;
		this.mainStage = mainStage;
		this.modalStage = modalStage;
		this.setFillParent(true);
	}

	public boolean isModalOpen() {
		return this.getActor() != null;
	}

	private int countContainersWithActors() {
		int count = 0;
		for (Actor actor : modalStage.getActors()) {
			if (actor instanceof Container container && container.getActor() != null) {
				count++;
			}
		}
		return count;
	}

	protected boolean isStageActive() {
		return countContainersWithActors() > 0;
	}

	protected boolean isStageReadyToClose() {
		return countContainersWithActors() <= 1;
	}

	protected void activatedStage() {
		Time.getInstance().stop();
		inputMultiplexer.setProcessors(modalStage);
		blurShader.startBlur();
	}

	protected void deactivatedStage() {
		inputMultiplexer.setProcessors(mainStage);
		blurShader.stopBlur();
	}

	public abstract void openModal();

	public abstract void closeModal();
}
