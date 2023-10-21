package pl.agh.edu.actor.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Null;

public class LinkLabel extends LanguageLabel{

    public LinkLabel(String languagePath, BitmapFont font, Runnable linkAction) {
        super(languagePath, font);
        setColor(SkinColor.SECONDARY_100);
        setUnderscore();
        addListener(
                new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        linkAction.run();
                    }

                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                        setColor(SkinColor.SECONDARY_300);
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        setColor(SkinColor.SECONDARY_100);
                    }
                }
        );



    }


}
