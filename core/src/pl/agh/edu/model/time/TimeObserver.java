package pl.agh.edu.model.time;

public interface TimeObserver {
    void onUpdate(int years, int months, int days, int hours, int minutes);

}
