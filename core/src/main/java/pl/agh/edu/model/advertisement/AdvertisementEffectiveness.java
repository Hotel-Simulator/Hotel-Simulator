package pl.agh.edu.model.advertisement;
import pl.agh.edu.json.data_loader.JSONAdvertisementDataLoader;
public enum AdvertisementEffectiveness {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    ;

    private  static final double advertisementMultiplier = JSONAdvertisementDataLoader.multiplier;


    public static AdvertisementEffectiveness getStars(double modifier){
        if(modifier/ advertisementMultiplier >= 100)return AdvertisementEffectiveness.FIVE;
        return AdvertisementEffectiveness.values()[(int)(modifier/advertisementMultiplier / 20)];

    }

    public static void main(String[] args) {
        System.out.println(getStars(0.09));

    }
}
