package pl.agh.edu.ui.utils.wrapper;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.ui.GameSkin;

public class ButtonTable extends WrapperContainer<Stack> {
	private final Skin skin = GameSkin.getInstance();
	public final Stack innerStack = new Stack();
	public final Table innerTable = new Table(skin);
	public final Table innerButtonTable = new Table();
	public final Button innerButton = new Button(skin, "transparent");

	public ButtonTable() {
		super();
		innerButtonTable.align(Align.bottomLeft);
		this.setActor(innerStack);
		innerButtonTable.add(innerButton).grow();
		innerStack.add(innerTable);
		innerStack.add(innerButtonTable);
	}

}
