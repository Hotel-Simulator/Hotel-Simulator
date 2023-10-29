package pl.agh.edu.ui.component.tab;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.utils.FontType;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.SkinColor.ColorLevel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

import static pl.agh.edu.ui.utils.SkinColor.GRAY;

public class TabSelector extends WrapperTable {
  Skin skin = GameSkin.getInstance();
  boolean left = true;
  public TabSelector(String laguagePathLeft, String languagePathRight, Runnable actionLeft, Runnable actionRight) {
    super();
    LanguageLabel rightLinkLabel = new LanguageLabel(languagePathRight, FontType.BODY_1.getWhiteVariantName());
    LanguageLabel leftLinkLabel = new LanguageLabel(laguagePathLeft, FontType.BODY_1.getWhiteVariantName());

    Runnable newActionLeft = () -> {
      actionLeft.run();
      leftLinkLabel.setDisabled(true);
      rightLinkLabel.setDisabled(false);
      left = true;
    };
    Runnable newActionRight = () -> {
      actionRight.run();
      rightLinkLabel.setDisabled(true);
      leftLinkLabel.setDisabled(false);
      left = false;
    };
    leftLinkLabel.makeItLink(newActionLeft);
    rightLinkLabel.makeItLink(newActionRight);
    innerTable.add(leftLinkLabel).pad(10);
    NinePatch separator = skin.getPatch("tabs-separator-line");
    separator.setColor(GRAY.getColor(ColorLevel._500));
    innerTable.add(new Image(separator)).width(2).pad(10).padTop(20).padBottom(20).padLeft(0).padRight(0);
    innerTable.add(rightLinkLabel).pad(10);
    newActionLeft.run();
  }
}
