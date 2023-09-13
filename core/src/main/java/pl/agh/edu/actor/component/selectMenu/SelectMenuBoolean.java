package pl.agh.edu.actor.component.selectMenu;


import pl.agh.edu.actor.utils.Size;

public class SelectMenuBoolean extends SelectMenuItem{
    private final boolean value;

    public SelectMenuBoolean(boolean value, Size size) {
        super(value ? "Yes" : "No");
        this.value = value;
    }
}
