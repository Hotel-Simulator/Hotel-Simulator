package pl.agh.edu.actor.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import pl.agh.edu.actor.utils.WrapperContainer;
import pl.agh.edu.config.GraphicConfig;

public class BlurShader extends WrapperContainer<Image> {
    private float deltaBlur = 0f;
    private float deltaFactor = 0.25f;
    private StateOfTransition stateOfTransition = StateOfTransition.CLOSED;
    private FrameBuffer fbo;
    private FrameBuffer blurTargetA;
    private FrameBuffer blurTargetB;
    private final ShaderProgram blurShader = new ShaderProgram(Gdx.files.internal("shaders/blur.vert"), Gdx.files.internal("shaders/blur.frag"));
    private final Stage stage;
    private final ReversedImage reversedImage;

    public BlurShader(Stage stage) {
        this.stage = stage;
        buildFBO();
        reversedImage = new ReversedImage(fbo.getColorBufferTexture());
        this.setActor(reversedImage);
        this.setResolutionChangeHandler(this::resize);
    }

    public void buildFBO(){
        int width = GraphicConfig.getResolution().WIDTH;
        int height = GraphicConfig.getResolution().HEIGHT;

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        blurTargetA = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        blurTargetB = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    }

    public void resize() {
        buildFBO();
        render();
    }
    public void render(){
        fbo.begin();
        stage.draw();
        stage.act();
        fbo.end();
        reversedImage.updateDrawable(blurTexture());
    }
    public StateOfTransition getStateOfTransition() {
        return stateOfTransition;
    }
    public void startBlur(){
        stateOfTransition = StateOfTransition.OPENING;
        this.buildFBO();
        deltaFactor = 0.25f;
    }
    public void stopBlur() {
        stateOfTransition = StateOfTransition.CLOSING;
        deltaFactor = -0.90f;
    }
    private Texture blurTexture() {
        if ((deltaBlur < 1f && deltaFactor > 0f) || (deltaBlur > 0f && deltaFactor < 0f)) {
            deltaBlur += Gdx.graphics.getDeltaTime() * deltaFactor;
            if(deltaBlur <= 0f) stateOfTransition = StateOfTransition.CLOSED;
            if(deltaBlur >= 1f) stateOfTransition = StateOfTransition.OPEN;
            deltaBlur = Math.max(Math.min(deltaBlur, 1f), 0f);
        }

//        int width = GraphicConfig.isFullscreen() ? Gdx.graphics.getWidth() : GraphicConfig.getResolution().WIDTH;
//        int height = GraphicConfig.isFullscreen() ? Gdx.graphics.getHeight() : GraphicConfig.getResolution().HEIGHT;
//        int x = !GraphicConfig.isFullscreen() ? 0 : (Gdx.graphics.getWidth() - GraphicConfig.getResolution().WIDTH) / 2;
//        int y = !GraphicConfig.isFullscreen() ? 0 : (Gdx.graphics.getHeight() - GraphicConfig.getResolution().HEIGHT) / 2;

        int width = GraphicConfig.getResolution().WIDTH;
        int height = GraphicConfig.getResolution().HEIGHT;
        int x = 0;
        int y = 0;


        SpriteBatch spriteBatch = (SpriteBatch) stage.getBatch();
        float MAX_BLUR = 4;
        float RADIUS = 0.5f;

        spriteBatch.setShader(blurShader);
        spriteBatch.begin();
        blurTargetA.begin();
        blurShader.setUniformf("dir", RADIUS, 0);
        blurShader.setUniformf("radius", deltaBlur * MAX_BLUR);
        blurShader.setUniformf("resolution", width);
        spriteBatch.draw(fbo.getColorBufferTexture(), x, y, width, height, 0, 0, 1, 1);
        spriteBatch.end();
        blurTargetB.end( 0,0,this.getTargetWidth(),this.getTargetHeight());

        spriteBatch.begin();
        blurTargetB.begin();
        blurShader.setUniformf("dir", 0, RADIUS);
        blurShader.setUniformf("radius", deltaBlur * MAX_BLUR);
        spriteBatch.draw(blurTargetA.getColorBufferTexture(), x, y, width, height, 0, 0, 1, 1);
        spriteBatch.end();
        blurTargetB.end( 0,0,this.getTargetWidth(),this.getTargetHeight());

        spriteBatch.setShader(null);

        return blurTargetB.getColorBufferTexture();
    }

    private int getTargetX(){
        return (int) GraphicConfig.getViewport().getCamera().position.x - GraphicConfig.getResolution().WIDTH/2;
    }

    private int getTargetY(){
        return (int) GraphicConfig.getViewport().getCamera().position.y - GraphicConfig.getResolution().HEIGHT/2;
    }

    private int getTargetWidth(){
        if(GraphicConfig.isFullscreen()) return Gdx.graphics.getDisplayMode().width;
        return (int) GraphicConfig.getViewport().getWorldWidth();
    }

    private int getTargetHeight(){
        if(GraphicConfig.isFullscreen()) return Gdx.graphics.getDisplayMode().height;
        return (int) GraphicConfig.getViewport().getWorldHeight();
    }
    public void dispose() {
        fbo.dispose();
        blurTargetA.dispose();
        blurTargetB.dispose();
        this.setActor(null);
    }
    private static class ReversedImage extends Image {

        private Drawable drawable;
        public ReversedImage(Texture texture) {
            super(texture);
            this.drawable = new TextureRegionDrawable(new TextureRegion(texture));
            this.setScaleX(-1.0f);
            this.setRotation(180);
        }

        public void updateDrawable(Texture texture) {
            this.drawable = new TextureRegionDrawable(new TextureRegion(texture));
        }
        @Override
        public void draw (Batch batch, float parentAlpha) {
            float x = 0;
            float y = GraphicConfig.getResolution().HEIGHT;
            float scaleX = this.getScaleX();
            float scaleY = this.getScaleY();
            float imageWidth = GraphicConfig.getResolution().WIDTH;
            float imageHeight = GraphicConfig.getResolution().HEIGHT;

            ((TransformDrawable)drawable).draw(batch, x , y, getOriginX(), getOriginY(),imageWidth, imageHeight, scaleX, scaleY, 180);
        }
    }
}