package pl.agh.edu.windows;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pl.agh.edu.model.Employee;
import pl.agh.edu.enums.TypeOfContract;


public class ConfirmWindow extends CustomWindow {

    private boolean confirmed = false;
    private Actor actor;
    private Employee employee;

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ConfirmWindow(Actor actor, Employee employee, String text, Skin skin){
        super("",skin);
        this.actor = actor;
        this.employee = employee;

        this.add(new Label("Choose type of Contract",skin));
        this.row();
        for(TypeOfContract toc:TypeOfContract.values()){
            TextButton tb = new TextButton(toc.toString(),skin);
            this.add(tb);
            tb.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setConfirmed(true);
                    System.out.println("Hey, you clicked me!");
                    ((Button) getActor()).setDisabled(true);
                    System.out.println(((Button) getActor()).isDisabled());
                    getActor().setColor(getActor().getColor().r,getActor().getColor().g,getActor().getColor().b,0.3f);
                    getEmployee().setHired(true);
                    setVisible(false);
                }
            });
        }






    }

    public void setConfirmed(boolean val){
        this.confirmed = val;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
