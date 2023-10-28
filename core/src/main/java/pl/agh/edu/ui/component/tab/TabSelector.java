package pl.agh.edu.ui.component.tab;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.utils.FontType;
import pl.agh.edu.ui.utils.LinkLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class TabSelector extends WrapperTable {
  private final String languagePathLeft;
  private final String languagePathRight;
  Skin skin = GameSkin.getInstance();


  public TabSelector(String laguagePathLeft, String languagePathRight, Runnable actionLeft, Runnable actionRight) {
    super();
    this.languagePathLeft = laguagePathLeft;
    this.languagePathRight = languagePathRight;
    innerTable.add(new LinkLabel(laguagePathLeft, FontType.BODY_1.getWhiteVariantName(), actionLeft)).pad(10);
    innerTable.add(new Image(skin.getPatch("table-separator-line"))).width(2).pad(10).padTop(20).padBottom(20).padLeft(0).padRight(0);
    innerTable.add(new LinkLabel(languagePathRight, FontType.BODY_1.getWhiteVariantName(), actionRight)).pad(10);
  }
}
