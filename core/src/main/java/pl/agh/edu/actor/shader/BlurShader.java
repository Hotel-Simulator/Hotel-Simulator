package pl.agh.edu.actor.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import pl.agh.edu.actor.utils.WrapperContainer;
import pl.agh.edu.config.GraphicConfig;

public class BlurShader extends WrapperContainer<Image> {
    private final float blurScale = 1.00f;
    private float deltaBlur = 0f;
    private int pingPongCount = 4;
    private final float MAX_BLUR = 4;
    private float deltaFactor = 0.25f;
    private StateOfTransition stateOfTransition = StateOfTransition.CLOSED;
    private FrameBuffer fbo;
    private FrameBuffer blurTargetA;
    private FrameBuffer blurTargetB;
    private final Stage stage;
    private final SpriteBatch spriteBatch;
    private final ShaderProgram defaultShader;
    private final ShaderProgram blurShader = new ShaderProgram(Gdx.files.internal("shaders/blur.vert"), Gdx.files.internal("shaders/blur.frag"));

    public BlurShader(Stage stage, SpriteBatch spriteBatch) {
        this.stage = stage;
        this.spriteBatch = spriteBatch;
        this.defaultShader = spriteBatch.getShader();
        buildFBO();
        this.fill();
        this.setResolutionChangeHandler(this::resize);
    }

    private void buildFBO() {
        if (fbo != null) fbo.dispose();
        if (blurTargetA != null) blurTargetA.dispose();
        if (blurTargetB != null) blurTargetB.dispose();

        fbo = new GLFrameBuffer.FrameBufferBuilder(GraphicConfig.getResolution().WIDTH, GraphicConfig.getResolution().HEIGHT)
                .addBasicColorTextureAttachment(Pixmap.Format.RGBA8888)
                .addDepthRenderBuffer(GL30.GL_DEPTH_COMPONENT24)
                .build();

        blurTargetA = new FrameBuffer(Pixmap.Format.RGBA8888, (int) (GraphicConfig.getResolution().WIDTH * blurScale), (int) (GraphicConfig.getResolution().HEIGHT * blurScale), false);
        blurTargetB = new FrameBuffer(Pixmap.Format.RGBA8888, (int) (GraphicConfig.getResolution().WIDTH * blurScale), (int) (GraphicConfig.getResolution().HEIGHT * blurScale), false);
    }

    public void resize() {
        buildFBO();
        render();
    }
    public void render(){
        this.setActor(getBluredImage());
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
    private Texture blurTexture(Texture fboTex) {
        if ((deltaBlur < 1f && deltaFactor > 0f) || (deltaBlur > 0f && deltaFactor < 0f)) {
            deltaBlur += Gdx.graphics.getDeltaTime() * deltaFactor;
            if(deltaBlur <= 0f) stateOfTransition = StateOfTransition.CLOSED;
            if(deltaBlur >= 1f) stateOfTransition = StateOfTransition.OPEN;
            deltaBlur = Math.max(Math.min(deltaBlur, 1f), 0f);
        }

        spriteBatch.setShader(blurShader);

        for (int i = 0; i < pingPongCount; i++) {
            blurTargetA.begin();
            spriteBatch.begin();
            blurShader.setUniformf("dir", .5f, 0);
            blurShader.setUniformf("radius", deltaBlur * MAX_BLUR);
            blurShader.setUniformf("resolution", Gdx.graphics.getWidth());
            spriteBatch.draw(i == 0 ? fboTex : blurTargetB.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
            spriteBatch.end();
            blurTargetA.end();

            blurTargetB.begin();
            spriteBatch.begin();
            blurShader.setUniformf("dir", 0, .5f);
            blurShader.setUniformf("radius", deltaBlur * MAX_BLUR);
            blurShader.setUniformf("resolution", Gdx.graphics.getHeight());
            spriteBatch.draw(blurTargetA.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
            spriteBatch.end();
            blurTargetB.end();
        }

        spriteBatch.setShader(defaultShader);

        return blurTargetB.getColorBufferTexture();
    }
    public void dispose() {
        fbo.dispose();
        blurTargetA.dispose();
        blurTargetB.dispose();
        this.setActor(null);
    }
    private Image getBluredImage() {
        fbo.begin();
        stage.draw();
        stage.act();
        fbo.end();
        return new ReversedImage(blurTexture(fbo.getColorBufferTexture()));
    }

    private static class ReversedImage extends Image {

        private Drawable drawable;
        public ReversedImage(Texture texture) {
            super(texture);
            this.drawable = new TextureRegionDrawable(new TextureRegion(texture));
            this.setScaleX(-1.0f);
            this.setRotation(180);
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



