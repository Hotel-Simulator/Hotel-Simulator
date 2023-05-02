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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.w3c.dom.Text;
import pl.agh.edu.model.Employee;
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

	private List<Employee> employeeList = new ArrayList<>();

	Skin skin;
	private Table root;




	@Override
	public void create() {
		employeeList.add(new Employee("A","B",30,1));
		employeeList.add(new Employee("Cadsfa","Bdafasfsa",30,1));
		employeeList.add(new Employee("Aleksander","Brzęczeszczykiewicz",30,1));

		skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		final Table table = new Table();
		ScrollPane scrollPane = new ScrollPane(table, skin);
		scrollPane.setFadeScrollBars(false);
		scrollPane.setFlickScroll(false);

		root.add(scrollPane);


		scrollPane.validate();


		table.defaults().space(35).fillX();

//		table.debug();
		for (final Employee employee : employeeList) {


			Pixmap drawable = new Pixmap(Gdx.files.internal("head2.jpeg"));
			Pixmap scaled = new Pixmap(40,40,drawable.getFormat());
			scaled.drawPixmap(drawable,
					0,0,drawable.getWidth(),drawable.getHeight(),
					0,0,scaled.getWidth(),scaled.getHeight());
			Image image = new Image(new Texture(scaled));
			table.add(image).left();

			table.add(new Label(employee.getFirstName() + " "+ employee.getLastName(),skin)).colspan(6).fill().space(20);


			table.add(new Label("Skills: "+employee.getSkills(),skin));
			Button button = new Button(skin);
			button.add(new Label("hire",skin)).space(30);
			table.add(button).right();


			button.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {


					ConfirmWindow window = new ConfirmWindow(actor,employee,"Confirm hiring",skin);
					window.setSize(400, 300);
					window.setModal(true);
					window.setVisible(true);
					window.setMovable(true);
					window.setPosition(Gdx.graphics.getWidth()/2 - window.getWidth()/2, Gdx.graphics.getHeight()/2 - window.getHeight()/2);

					stage.addActor(window);





				}
			});

			table.row();
		}

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
