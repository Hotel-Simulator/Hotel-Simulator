package pl.agh.edu.actor.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;

public class LinkLabel extends LanguageLabel{

    public LinkLabel(String languagePath, BitmapFont font, Runnable action) {
        super(languagePath, font);
        setColor(Colors.SECONDARY_100);
        setUnderscore();
        addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        action.run();
                        return true;
                    }

                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                        setColor(Colors.SECONDARY_300);
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        setColor(Colors.SECONDARY_100);
                    }
                }
        );



    }


}
