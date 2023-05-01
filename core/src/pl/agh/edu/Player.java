package pl.agh.edu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public Vector2 position;
    public Sprite sprite;

    public float speed = 500;
    public Player(Texture img, Color color){
        sprite = new Sprite(img);
        sprite.setColor(color);
        position = new Vector2(Gdx.graphics.getWidth()/2,sprite.getScaleY()+sprite.getHeight()/2);

    }

    public void Update(float deltaTime){
        if(Gdx.input.isKeyPressed(Keys.A)) position.x -=deltaTime*speed;
        if(Gdx.input.isKeyPressed(Keys.D)) position.x +=deltaTime*speed;
    }

    public void Draw(SpriteBatch batch){
        Update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(position.x,position.y);
        batch.disableBlending();
        sprite.draw(batch);
        batch.enableBlending();
    }
}
