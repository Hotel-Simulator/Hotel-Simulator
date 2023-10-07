package pl.agh.edu.actor.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class BlurShader {
    private final float blurScale = 1.00f;
    private float deltaBlur = 0f;
    private int pingPongCount = 4;

    private final float MAX_BLUR = 4;

    private float deltaFactor = 0.25f;
    private FrameBuffer fbo;
    private FrameBuffer blurTargetA;
    private FrameBuffer blurTargetB;

    private final SpriteBatch spriteBatch;
    private final ShaderProgram defaultShader;
    private final ShaderProgram blurShader = new ShaderProgram(Gdx.files.internal("shaders/blur.vert"), Gdx.files.internal("shaders/blur.frag"));

    public BlurShader(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.defaultShader = spriteBatch.getShader();
        buildFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void buildFBO(int width, int height) {
        if (width == 0 || height == 0) return;
        if (fbo != null) fbo.dispose();
        if (blurTargetA != null) blurTargetA.dispose();
        if (blurTargetB != null) blurTargetB.dispose();

        fbo = new GLFrameBuffer.FrameBufferBuilder(width, height)
                .addBasicColorTextureAttachment(Pixmap.Format.RGBA8888)
                .addDepthRenderBuffer(GL30.GL_DEPTH_COMPONENT24)
                .build();

        blurTargetA = new FrameBuffer(Pixmap.Format.RGBA8888, (int) (width * blurScale), (int) (height * blurScale), false);
        blurTargetB = new FrameBuffer(Pixmap.Format.RGBA8888, (int) (width * blurScale), (int) (height * blurScale), false);
    }

    public void resize(int width, int height) {
        buildFBO(width, height);
    }
    public void startBlur(){
        deltaFactor = 0.25f;
    }
    public void stopBlur(){
        deltaFactor = -0.90f;
    }

    public boolean isOpen(){
        return deltaBlur > 0f;
    }
    private Texture blurTexture(Texture fboTex) {
        if ((deltaBlur < 1f && deltaFactor > 0f) || (deltaBlur > 0f && deltaFactor < 0f)) {
            deltaBlur += Gdx.graphics.getDeltaTime() * deltaFactor;
        }

        spriteBatch.setShader(blurShader);

        for (int i = 0; i < pingPongCount; i++) {
            // Horizontal blur pass
            blurTargetA.begin();
            spriteBatch.begin();
            blurShader.setUniformf("dir", .5f, 0);
            blurShader.setUniformf("radius", deltaBlur * MAX_BLUR);
            blurShader.setUniformf("resolution", Gdx.graphics.getWidth());
            spriteBatch.draw(i == 0 ? fboTex : blurTargetB.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
            spriteBatch.end();
            blurTargetA.end();

            // Verticle blur pass
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
    public Texture renderWithBlur(Stage stage){
        fbo.begin();
        stage.draw();
        stage.act();
        fbo.end();

        return blurTexture(fbo.getColorBufferTexture());
    }
}



