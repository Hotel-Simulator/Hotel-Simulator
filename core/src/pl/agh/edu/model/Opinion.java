package pl.agh.edu.model;

public class Opinion {

    private final Double value; // 0 - 1

    public Opinion(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

}
