package pl.agh.edu.ui.component.tab;

import static pl.agh.edu.ui.component.tab.TabSelector.TabSelectorStyle.getFont;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.BODY1;
import static pl.agh.edu.ui.utils.SkinFont.BODY2;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.badlogic.gdx.utils.Align;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class TabSelector extends WrapperTable {

	public TabSelector(LanguageString languagePathLeft, LanguageString languagePathRight, Runnable actionLeft, Runnable actionRight) {
		super();
		LanguageLabel rightLinkLabel = new LanguageLabel(languagePathRight, getFont());
		LanguageLabel leftLinkLabel = new LanguageLabel(languagePathLeft, getFont());
		rightLinkLabel.setBaseColor(GRAY);
		leftLinkLabel.setBaseColor(GRAY);
		rightLinkLabel.setDisabledColor(SkinColor.WARNING);
		leftLinkLabel.setDisabledColor(SkinColor.WARNING);

		Runnable newActionLeft = () -> {
			actionLeft.run();
			leftLinkLabel.setDisabled(true);
			rightLinkLabel.setDisabled(false);
		};
		Runnable newActionRight = () -> {
			actionRight.run();
			leftLinkLabel.setDisabled(false);
			rightLinkLabel.setDisabled(true);
		};
		leftLinkLabel.makeItLink(newActionLeft);
		rightLinkLabel.makeItLink(newActionRight);
		innerTable.add(leftLinkLabel).width(200f);
		leftLinkLabel.setAlignment(Align.right,Align.right);
		NinePatch separator = skin.getPatch("tabs-separator-line");
		separator.setColor(GRAY.getColor(_500));
		innerTable.add(new Image(separator)).width(1f).pad(10f).padTop(20f).padBottom(20f).padLeft(30f).padRight(30f);
		innerTable.add(rightLinkLabel).width(200f);
		rightLinkLabel.setAlignment(Align.left,Align.left);
		newActionLeft.run();
		setResolutionChangeHandler(() -> {
			leftLinkLabel.setFont(getFont());
			rightLinkLabel.setFont(getFont());
		});
	}

  static class TabSelectorStyle {
		static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> BODY2.getWhiteVariantName();
				case MEDIUM, LARGE -> BODY1.getWhiteVariantName();
			};
		}
	}

}
