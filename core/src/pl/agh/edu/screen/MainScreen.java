package pl.agh.edu.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import pl.agh.edu.GdxGame;
import pl.agh.edu.screen.application_listener.TopNavbarListener;

public class MainScreen implements Screen {

    private final GdxGame game;
    private final Stage stage;

    private TopNavbarListener navbarApplicationListener;

    public MainScreen(GdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
    }
    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.GRAVE) {
                    game.changeScreenToConsole();
                }
                return super.keyDown(keycode);
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
        Skin skin = new Skin(Gdx.files.internal("skin/skin.json"));

        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage, skin, Gdx.files.internal("view/test.json"));
        navbarApplicationListener = new TopNavbarListener(stage);
        navbarApplicationListener.create();

    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
        navbarApplicationListener.render();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}