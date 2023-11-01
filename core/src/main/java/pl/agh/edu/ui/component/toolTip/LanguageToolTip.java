package pl.agh.edu.ui.component.toolTip;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

import java.util.List;

public class LanguageToolTip extends TextTooltip implements ResolutionChangeListener, LanguageChangeListener {

    private final String languagePath;

    public final List<Pair<String, String>> stringsWithReplacements;

    public LanguageToolTip(LanguageString languageString) {
        super(LanguageManager.get(languageString), createManager(), GameSkin.getInstance());
        this.languagePath = languageString.property;
        this.stringsWithReplacements = languageString.stringsWithReplacements;
        init();
    }

    public LanguageToolTip(String languagePath){
        super(LanguageManager.get(languagePath), createManager(), GameSkin.getInstance());
        this.languagePath = languagePath;
        this.stringsWithReplacements = List.of();
        init();
    }

    private void init(){
        updateStyle();
        LanguageManager.addListener(this);
        ResolutionManager.addListener(this);
    }
    public static TooltipManager createManager(){
        TooltipManager manager = new TooltipManager();
        manager.initialTime = 0.5f;
        manager.resetTime = 0.5f;
        manager.subsequentTime = 0.5f;
        manager.maxWidth = 5f;
        return manager;
    }

    public void updateStyle(){
        TextTooltipStyle style = new TextTooltipStyle(GameSkin.getInstance().get("default", TextTooltip.TextTooltipStyle.class));
        switch (GraphicConfig.getResolution().SIZE) {
            case SMALL -> style.wrapWidth= 300f;
            case MEDIUM -> style.wrapWidth= 300f;
            case LARGE -> style.wrapWidth= 300f;
        }
        this.setStyle(style);
    }

    public void createNewLabel(){
        this.newLabel(
                LanguageManager.get(languagePath,stringsWithReplacements),
                GameSkin.getInstance().get("body2", Label.LabelStyle.class)
        );
    }

    @Override
    public void onLanguageChange() {
        createNewLabel();
    }

    @Override
    public void onResolutionChange() {
        updateStyle();
    }
}
