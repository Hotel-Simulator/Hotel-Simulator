package pl.agh.edu.ui.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Pools;

import pl.agh.edu.config.GraphicConfig;

public class CustomScrollPane extends ScrollPane {

	public CustomScrollPane(Actor actor, Skin skin, String styleName) {
		super(actor, skin, styleName);
	}

	@Override
	public boolean clipBegin(float x, float y, float width, float height) {
		if (width <= 0 || height <= 0)
			return false;
		Stage stage = this.getStage();
		if (stage == null)
			return false;
		Rectangle tableBounds = new Rectangle();
		if (GraphicConfig.isBlurShaderEnabled() && GraphicConfig.isFullscreen()) {
			tableBounds.x = this.getX();
			tableBounds.y = this.getY();
			tableBounds.width = this.getWidth();
			tableBounds.height = this.getHeight();
			if (ScissorStack.pushScissors(tableBounds))
				return true;
			Pools.free(tableBounds);
		} else {
			tableBounds.x = x;
			tableBounds.y = y;
			tableBounds.width = width;
			tableBounds.height = height;
			Rectangle scissorBounds = Pools.obtain(Rectangle.class);
			stage.calculateScissors(tableBounds, scissorBounds);
			if (ScissorStack.pushScissors(scissorBounds))
				return true;
			Pools.free(scissorBounds);
		}
		return false;
	}

}
