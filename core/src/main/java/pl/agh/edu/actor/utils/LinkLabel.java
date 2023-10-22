package pl.agh.edu.actor.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;


import static pl.agh.edu.actor.utils.SkinColor.ColorLevel._300;
import static pl.agh.edu.actor.utils.SkinColor.SECONDARY;
import static pl.agh.edu.actor.utils.SkinColor.ColorLevel._500;

public class LinkLabel extends LanguageLabel{

    public LinkLabel(String languagePath, String font, Runnable linkAction) {
        super(languagePath, font);
        setColor(SECONDARY.getColor(_500));
        setUnderscoreColor(SECONDARY.getColor(_500));
        addListener(
                new InputListener() {
//                    @Override
//                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                        linkAction.run();
//                    }

                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                        setColor(SECONDARY.getColor(_300));
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        setColor(SECONDARY.getColor(_500));
                    }
                }
        );
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                linkAction.run();
            }
        });



    }


}
