package pl.agh.edu.ui.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.navbar.NavbarButtonType;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;

public class FrameStack extends WrapperContainer<Stack> {

	private final Stack stack = new Stack();

	public FrameStack() {
		super();
		this.stack.setFillParent(true);
		this.setActor(stack);
		this.stack.add(NavbarButtonType.BOARD.getFrameCreator());
		this.resize();
		this.setResolutionChangeHandler(this::resize);
	}

	public boolean isActionPossible() {
		return stack.getChildren().size < 2;
	}

	public void changeFrame(BaseFrame newFrame) {
		BaseFrame baseFrame = (BaseFrame) stack.getChildren().peek();
		stack.addActor(newFrame);
		newFrame.runHorizontalTrainInAnimation();
		baseFrame.runHorizontalTrainOutAnimation();
		newFrame.debug();
	}

	public void resize() {
		this.size(FrameStackStyle.getFrameWidth(), FrameStackStyle.getFrameHeight());
	}

	private static class FrameStackStyle {
		public static float getFrameHeight() {
			return (float) GraphicConfig.getResolution().HEIGHT / 9 * 6;
		}

		public static float getFrameWidth() {
			return (float) GraphicConfig.getResolution().WIDTH / 8 * 6;
		}
	}
}
