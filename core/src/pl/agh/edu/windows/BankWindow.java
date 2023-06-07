package pl.agh.edu.windows;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.agh.edu.model.bank.Bank;

public class BankWindow extends CustomWindow{
    public BankWindow(Stage stage, Bank bank, String text, Skin skin){
        super(stage,text,skin);
        this.add(new Label("Account balance: "+bank.printBalance(),skin));
        this.row();
    }
}
