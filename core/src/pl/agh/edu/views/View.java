package pl.agh.edu.views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class View extends Table {
    protected View self;
    protected Skin skin;
    public View(Skin skin,Table root){
        super();
        this.self = this;
        if(!(this instanceof HotelView)){
            TextButton home = new TextButton("home",skin);
            home.addListener(new ClickListener(){
                @Override
                public void clicked (InputEvent event, float x, float y) {
                    self.remove();
                    root.add(new HotelView(root,skin));
                }
            });
            this.add(home).expandX().left().expandY().top();

        }

        this.skin = skin;
    }
}
