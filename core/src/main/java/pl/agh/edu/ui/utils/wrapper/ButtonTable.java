package pl.agh.edu.ui.utils.wrapper;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.ui.GameSkin;

public class ButtonTable extends WrapperContainer<Stack>{
  private final Skin skin = GameSkin.getInstance();
  public final Stack innerStack = new Stack();
  public final Table innerTable = new Table(skin);
  public final Table innerButtonTable = new Table();
  public final Button innerButton = new Button(skin, "transparent");
//  public final Table clickableTable = new Table();
  public ButtonTable() {
    super();
    innerButtonTable.debugAll();
    innerButtonTable.align(Align.bottomLeft);
//    innerButton.setVisible(false);
    this.setActor(innerStack);
//    innerButton.setVisible(false);
    innerButtonTable.add(innerButton).grow();
//    innerButton.setWidth(100f);
//    innerButton.setHeight(100f);
//    innerButton.sizeBy(12f);
//    innerButton.setSize(100f,100f);
//    innerButton.setFillParent(true);
    innerStack.add(innerTable);
    innerStack.add(innerButtonTable);
//    innerStack.add(clickableTable);
  }


}
