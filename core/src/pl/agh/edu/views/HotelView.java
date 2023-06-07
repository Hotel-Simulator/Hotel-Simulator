package pl.agh.edu.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class HotelView extends View{

    public HotelView(Table root, Skin skin){
        super(skin,root);
        TextButton bankButton = new TextButton("Bank/account", skin);
        this.add(bankButton).center();
        bankButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
               root.removeActor(self);
               root.add(new BankView(root,skin));
			}
		});

        TextButton hireEmployeeButton = new TextButton("Hire Employees",skin);
        this.add(hireEmployeeButton).center();

        hireEmployeeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                root.removeActor(self);
                root.add(new HireEmployeesView(root,skin));
            }
        });
    }
}
