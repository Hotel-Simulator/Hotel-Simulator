package pl.agh.edu.actor.pane;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import pl.agh.edu.GameConfig;
import pl.agh.edu.actor.HotelSkin;

public abstract class BasePanel extends Stack {
    private final float WIDTH = GameConfig.WIDTH/4*3;
    private final float HEIGHT = GameConfig.HEIGHT/4*3;

    public BasePanel() {
        super();
        this.debugAll();
        Skin skin = HotelSkin.getInstance();
        NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("panel-glass-background"));
        add(new Image(background, Scaling.stretch, Align.center));
    }
    @Override
    public void layout() {
        super.layout();
        this.setSize(WIDTH, HEIGHT);
    }

}
