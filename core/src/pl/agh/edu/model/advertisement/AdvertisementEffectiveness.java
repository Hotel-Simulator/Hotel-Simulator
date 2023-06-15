package pl.agh.edu.model.advertisement;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.JSONExtractor;

import java.io.IOException;

public enum AdvertisementEffectiveness {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    ;
    private  static final double advertisementMultiplier;

    static {
        try {
            advertisementMultiplier = JSONExtractor.getAdvertisementMultiplier();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static AdvertisementEffectiveness getStars(double modifier){
        if(modifier/ advertisementMultiplier >= 100)return AdvertisementEffectiveness.FIVE;
        return AdvertisementEffectiveness.values()[(int)(modifier/advertisementMultiplier / 20)];

    }

    public static void main(String[] args) {
        System.out.println(getStars(0.09));
    }
}
