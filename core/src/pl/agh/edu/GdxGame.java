package pl.agh.edu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pl.agh.edu.command.CommandExecutor;
import pl.agh.edu.model.Bank;
import pl.agh.edu.time.Time;
import pl.agh.edu.views.BankView;
import pl.agh.edu.views.HotelView;
import pl.agh.edu.windows.BankWindow;
import pl.agh.edu.windows.HireEmployeesWindow;

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

	private Time time;
    private CommandExecutor commandExecutor;



	@Override
	public void create() {
		time = Time.getInstance();
		commandExecutor=CommandExecutor.getInstance();

		skin = new Skin(Gdx.files.internal("metalui/metal-ui.json")); // some random zip downloaded

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);
		HotelView bankView = new HotelView(root,skin);
		root.add(bankView);

//		final HireEmployeesWindow table = new HireEmployeesWindow(stage, "hire emplyees", skin); //custom window that extends CustomWindow that extends Window
//
//		TextButton hireEmployeesButton = new TextButton("hire employees", skin);
//		hireEmployeesButton.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				table.setSize(600, 400);
//				table.setModal(true);
//				table.setVisible(true);
//				table.setMovable(true);
//				table.setPosition(Gdx.graphics.getWidth() / 2 - table.getWidth() / 2, Gdx.graphics.getHeight() / 2 - table.getHeight() / 2);
//
//				stage.addActor(table);
//			}
//		});
//		root.add(hireEmployeesButton).center();
//		root.row();
//
//		Bank bank = new Bank();
//		final BankWindow bankWindow = new BankWindow(stage, bank,"Bank/account", skin); //custom window that extends CustomWindow that extends Window
//
//
//		TextButton bankButton = new TextButton("Bank/account", skin);
//		bankButton.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				bankWindow.setSize(600, 400);
//				bankWindow.setModal(true);
//				bankWindow.setVisible(true);
//				bankWindow.setMovable(true);
//				bankWindow.setPosition(Gdx.graphics.getWidth() / 2 - bankWindow.getWidth() / 2, Gdx.graphics.getHeight() / 2 - bankWindow.getHeight() / 2);
//
//				stage.addActor(bankWindow);
//			}
//		});
//		root.add(bankButton).center();
//		root.row();


//		scrollPane.validate();
	}

	@Override
	public void render() {
		time.update(Gdx.graphics.getDeltaTime());
		commandExecutor.executeCommands();

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