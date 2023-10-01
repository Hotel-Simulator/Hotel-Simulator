package pl.agh.edu.actor.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class BlurShader {
    private final FrameBuffer blurTargetA;
    private final FrameBuffer blurTargetB;
    private final ShaderProgram blurShader;

    public BlurShader(int FBO_SIZE) {
        blurTargetA = new FrameBuffer(Pixmap.Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
        blurTargetB = new FrameBuffer(Pixmap.Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);

        String vertShader = Gdx.files.internal("shaders/blur.vert").readString();
        String fragShader = Gdx.files.internal("shaders/blur.frag").readString();
        blurShader = new ShaderProgram(vertShader, fragShader);

        if (!blurShader.isCompiled()) {
            Gdx.app.error("Shader Error", blurShader.getLog());
        }
    }

    public void renderBlur(FrameBuffer sourceBuffer, float screenWidth, float screenHeight, float blurRadius) {
        blurTargetA.begin();
        blurShader.begin();
        blurShader.setUniformf("dir", 1f, 0f);
        blurShader.setUniformf("radius", blurRadius);
        blurShader.setUniformf("resolution", screenWidth);
        sourceBuffer.getColorBufferTexture().bind(0);

        blurShader.end();
        blurTargetA.end();

        blurTargetB.begin();
        blurTargetB.end();
    }

    public void dispose() {
        blurTargetA.dispose();
        blurTargetB.dispose();
        blurShader.dispose();
    }
}
