package pl.agh.edu.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import pl.agh.edu.model.Bank;

public class HotelView extends View{

    public HotelView(Table root, Skin skin, Bank bank){
        super();
        TextButton bankButton = new TextButton("Bank/account", skin);
        this.add(bankButton).center();
        bankButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
               root.removeActor(self);
               root.add(new BankView(root,skin,bank));
			}
		});
    }
}
