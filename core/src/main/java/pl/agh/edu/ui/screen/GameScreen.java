package pl.agh.edu.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import java.util.function.Consumer;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.background.InfinityBackground;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.shader.BlurShader;

public class GameScreen implements Screen {
    private final Stage mainStage = new Stage(GraphicConfig.getViewport());
    private final Stage middleStage = new Stage(GraphicConfig.getViewport());
    private final Stage topStage = new Stage(GraphicConfig.getViewport());
    private final BlurShader blurShader = new BlurShader(mainStage, middleStage);
    private final Container<Actor> container = new Container<>();
    private final InputMultiplexer inputMultiplexer;
    private final ModalManager modalManager;
    private final Consumer<Float> renderConsumer;

    private final InfinityBackground infinityBackground;
    public GameScreen(String backgroundName,InputMultiplexer inputMultiplexer, Consumer<Float> renderConsumer) {
        setupUI();

        this.inputMultiplexer = inputMultiplexer;
        this.renderConsumer = renderConsumer;
        this.infinityBackground = new InfinityBackground(backgroundName);

        inputMultiplexer.addProcessor(mainStage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        modalManager = ModalManager.initialize(ModalManager.ModalPreferences.of(inputMultiplexer, blurShader, mainStage, topStage));

    }
    private void setupUI() {
        Stack stack = new Stack();

        stack.setFillParent(true);
        stack.addActor(infinityBackground);
        stack.addActor(container);

        mainStage.addActor(stack);
        middleStage.addActor(blurShader);
        topStage.addActor(modalManager);

    }
    public void setActor(Actor actor) {
        container.setActor(actor);
    }
    public void clearActor() {
        container.clearChildren();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        mainStage.act();
        mainStage.draw();
        if (blurShader.isActive()) {
            blurShader.render();
        }
        if (modalManager.isModalOpen()) {
            topStage.act();
            topStage.draw();
        }
        renderConsumer.accept(delta);
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
        middleStage.getViewport().update(width, height, true);
        topStage.getViewport().update(width, height, true);
        blurShader.resize();
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
