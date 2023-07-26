package update;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.update.*;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateTest {
    @Test
    public void perShiftUpdatableTest(){
        //given
        Updater updater = Updater.getInstance();
        var updatable = new PerShiftUpdatable() {
            int counter = 0;
            @Override
            public void perShiftUpdate() {counter++;}
        };
        updater.registerPerShiftUpdatable(updatable);

        //when
        updater.update(Updater.UpdateFrequency.SHIFT);
        updater.update(Updater.UpdateFrequency.DAY);
        updater.update(Updater.UpdateFrequency.MONTH);
        updater.update(Updater.UpdateFrequency.YEAR);
        //then
        assertEquals(4,updatable.counter);

    }
    @Test
    public void dailyUpdatableTest(){
        //given
        Updater updater = Updater.getInstance();
        var updatable = new DailyUpdatable() {
            int counter = 0;
            @Override
            public void dailyUpdate() {
                counter++;
            }
        };
        updater.registerDailyUpdatable(updatable);

        //when
        updater.update(Updater.UpdateFrequency.SHIFT);
        updater.update(Updater.UpdateFrequency.DAY);
        updater.update(Updater.UpdateFrequency.MONTH);
        updater.update(Updater.UpdateFrequency.YEAR);
        //then
        assertEquals(3,updatable.counter);

    }

    @Test
    public void monthlyUpdatableTest(){
        //given
        Updater updater = Updater.getInstance();
        var updatable = new MonthlyUpdatable() {
            int counter = 0;
            @Override
            public void monthlyUpdate() {
                counter++;
            }
        };
        updater.registerMonthlyUpdatable(updatable);

        //when
        updater.update(Updater.UpdateFrequency.SHIFT);
        updater.update(Updater.UpdateFrequency.DAY);
        updater.update(Updater.UpdateFrequency.MONTH);
        updater.update(Updater.UpdateFrequency.YEAR);
        //then
        assertEquals(2,updatable.counter);

    }

    @Test
    public void yearlyUpdatableTest(){
        //given
        Updater updater = Updater.getInstance();
        var updatable = new YearlyUpdatable() {
            int counter = 0;
            @Override
            public void yearlyUpdate() {
                counter++;
            }
        };
        updater.registerYearlyUpdatable(updatable);

        //when
        updater.update(Updater.UpdateFrequency.SHIFT);
        updater.update(Updater.UpdateFrequency.DAY);
        updater.update(Updater.UpdateFrequency.MONTH);
        updater.update(Updater.UpdateFrequency.YEAR);
        //then
        assertEquals(1,updatable.counter);

    }


}
