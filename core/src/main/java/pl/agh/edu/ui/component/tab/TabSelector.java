package pl.agh.edu.ui.component.tab;

import static pl.agh.edu.ui.component.tab.TabSelector.TabSelectorStyle.getFont;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.BODY_1;
import static pl.agh.edu.ui.utils.SkinFont.BODY_2;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class TabSelector extends WrapperTable {
	Skin skin = GameSkin.getInstance();

	public TabSelector(String languagePathLeft, String languagePathRight, Runnable actionLeft, Runnable actionRight) {
		super();
		LanguageLabel rightLinkLabel = new LanguageLabel(new LanguageString(languagePathRight), getFont());
		LanguageLabel leftLinkLabel = new LanguageLabel(new LanguageString(languagePathLeft), getFont());

		Runnable newActionLeft = () -> {
			actionLeft.run();
			leftLinkLabel.setDisabled(true);
			rightLinkLabel.setDisabled(false);
		};
		Runnable newActionRight = () -> {
			actionRight.run();
			rightLinkLabel.setDisabled(true);
			leftLinkLabel.setDisabled(false);
		};
		leftLinkLabel.makeItLink(newActionLeft);
		rightLinkLabel.makeItLink(newActionRight);
		innerTable.add(leftLinkLabel).pad(10);
		NinePatch separator = skin.getPatch("tabs-separator-line");
		separator.setColor(GRAY.getColor(_500));
		innerTable.add(new Image(separator)).width(2).pad(10).padTop(20).padBottom(20).padLeft(10).padRight(10);
		innerTable.add(rightLinkLabel).pad(10);
		newActionLeft.run();
	}

	static class TabSelectorStyle {
		static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> BODY_2.getWhiteVariantName();
				case MEDIUM -> BODY_1.getWhiteVariantName();
				case LARGE -> H4.getWhiteVariantName();
			};
		}
	}

}
