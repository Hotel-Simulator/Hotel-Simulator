package pl.agh.edu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

public class GdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	Player player;

	private Stage stage;
	private Table table;



	@Override
	public void create () {
//		batch = new SpriteBatch();
//		img = new Texture("player.png");
//		player = new Player(img, Color.RED);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

	}

	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
//		ScreenUtils.clear(1, 0, 0, 1);
//		batch.begin();
////		batch.draw(img, 0, 0);
//		player.Draw(batch);
//		System.out.println(batch.renderCalls);
//		batch.end();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}


	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
