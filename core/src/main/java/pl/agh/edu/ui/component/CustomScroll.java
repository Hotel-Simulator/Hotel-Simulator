package pl.agh.edu.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;

public class CustomScroll extends ScrollPane{


    public CustomScroll(Actor actor, Skin skin,String styleName) {
        super(actor, skin, styleName);
        this.debugAll();
        this.setCullingArea(new Rectangle(this.getX(),this.getY(),this.getWidth(),this.getHeight()));
    }
    @Override
    public boolean clipBegin (float x, float y, float width, float height) {
        if (width <= 0 || height <= 0) return true;
        Stage stage = this.getStage();
        if (stage == null) return true;
        Rectangle tableBounds = new Rectangle();
        tableBounds.width = this.getWidth();
        tableBounds.height = this.getHeight();
        tableBounds.x = this.getX();
        tableBounds.y = this.getY();

        Rectangle displayBound = new Rectangle();
        displayBound.width = GraphicConfig.getResolution().WIDTH;
        displayBound.height = GraphicConfig.getResolution().HEIGHT;
        displayBound.x = 0;
        displayBound.y = 0;

        Rectangle temp = new Rectangle();
        temp.x = 0;
        temp.y = 0;
        temp.width = width;
        temp.height = height;

//        stage.act();
//        stage.getBatch().flush();
//        stage.getBatch().end();
        stage.calculateScissors(tableBounds,temp);
        return ScissorStack.pushScissors(temp);
    }

}
