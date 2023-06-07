package pl.agh.edu.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import pl.agh.edu.generator.employee_generator.EmployeeGenerator;
import pl.agh.edu.model.Employee;
import pl.agh.edu.windows.ConfirmWindow;

import java.util.List;

public class HireEmployeesView extends View{

    private List<Employee> employeeList;

    public HireEmployeesView(Table root, Skin skin){
        super(skin,root);
        Table table = new Table();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        table.defaults().space(20).fillX();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(false);
        employeeList = EmployeeGenerator.getEmployees(20);
        this.add(scrollPane);

        for (final Employee employee : employeeList) {


            Pixmap drawable = new Pixmap(Gdx.files.internal("head2.jpeg"));
            Pixmap scaled = new Pixmap(40,40,drawable.getFormat());
            scaled.drawPixmap(drawable,
                    0,0,drawable.getWidth(),drawable.getHeight(),
                    0,0,scaled.getWidth(),scaled.getHeight());
            Image image = new Image(new Texture(scaled));
            table.add(image).left();

            table.add(new Label(employee.getFirstName() + " "+ employee.getLastName(),skin)).colspan(3).fill().space(1);


            table.add(new Label("Skills: "+employee.getSkills(),skin));
            table.add(new Label("Role: "+employee.getRole().name(),skin));
            Button button = new Button(skin);
            button.add(new Label("hire",skin)).space(1);
            table.add(button).right();


            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {


                    ConfirmWindow window = new ConfirmWindow(actor,employee,"Confirm hiring",skin);
                    window.setSize(400, 300);
                    window.setModal(true);
                    window.setVisible(true);
                    window.setMovable(true);
                    window.setPosition(Gdx.graphics.getWidth()/2 - window.getWidth()/2, Gdx.graphics.getHeight()/2 - window.getHeight()/2);

                    getStage().addActor(window);





                }
            });

            table.row();
        }
    }
}
