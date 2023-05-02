package pl.agh.edu;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.w3c.dom.Text;
import pl.agh.edu.model.Employee;
import sun.tools.jconsole.Tab;

import java.awt.*;

import javax.swing.*;

public class GdxGame extends ApplicationAdapter {
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private long lastDropTime;
	private Stage stage;
	private ScrollPane pane;
	private VerticalGroup verticalGroup;


	Skin skin;
	private Table root;




	@Override
	public void create() {

		skin = new Skin(Gdx.files.internal("metalui/metal-ui.json")); // some random zip downloaded

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		final HireEmployeesWindow table = new HireEmployeesWindow(stage,"hire emplyees",skin); //custom window that extends CustomWindow that extends Window



		TextButton hireEmployeesButton = new TextButton("hire employees",skin);
		hireEmployeesButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				table.setSize(600, 400);
				table.setModal(true);
				table.setVisible(true);
				table.setMovable(true);
				table.setPosition(Gdx.graphics.getWidth()/2 - table.getWidth()/2, Gdx.graphics.getHeight()/2 - table.getHeight()/2);

				stage.addActor(table);
			}
		});
		root.add(hireEmployeesButton).center();
		root.row();


//		scrollPane.validate();




	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}


	@Override
	public void dispose() {

		stage.dispose();
		skin.dispose();
	}
}
