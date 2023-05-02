package pl.agh.edu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import pl.agh.edu.model.Employee;

import java.util.List;

public class HireEmployeesWindow extends CustomWindow {
    HireEmployeesWindow(String title, List<Employee> employees, Skin skin){
        super("",skin);
    }
}
