package pl.agh.edu.model.advertisement;

public enum AdvertisementEffectiveness {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    ;
    public double value(){
        return switch (this){
            case ONE -> 0.3;
            case TWO -> 0.6;
            case THREE -> 0.9;
            case FOUR -> 0.12;
            case FIVE -> 0.15;
        };
    }
}
