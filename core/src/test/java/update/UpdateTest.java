package update;

import org.junit.jupiter.api.Test;
import pl.agh.edu.update.*;
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
        updater.update(Updater.UpdateFrequency.PER_SHIFT);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_OUT_TIME);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_IN_TIME);
        updater.update(Updater.UpdateFrequency.DAILY);
        updater.update(Updater.UpdateFrequency.MONTHLY);
        updater.update(Updater.UpdateFrequency.YEARLY);
        //then
        assertEquals(4,updatable.counter);

    }

    @Test
    public void dailyAtCheckOutTimeUpdatableTest(){
        //given
        Updater updater = Updater.getInstance();
        var updatable = new DailyAtCheckOutTimeUpdatable() {
            int counter = 0;
            @Override
            public void dailyAtCheckOutTimeUpdate() {
                counter++;
            }

        };
        updater.registerDailyAtCheckOutTimeUpdatable(updatable);

        //when
        updater.update(Updater.UpdateFrequency.PER_SHIFT);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_OUT_TIME);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_IN_TIME);
        updater.update(Updater.UpdateFrequency.DAILY);
        updater.update(Updater.UpdateFrequency.MONTHLY);
        updater.update(Updater.UpdateFrequency.YEARLY);
        //then
        assertEquals(1,updatable.counter);

    }
    @Test
    public void dailyAtHotelCheckInTimeUpdatableTest(){
        //given
        Updater updater = Updater.getInstance();
        var updatable = new DailyAtCheckInTimeUpdatable() {
            int counter = 0;
            @Override
            public void dailyAtCheckInTimeUpdate() {
                counter++;
            }

        };
        updater.registerDailyAtCheckInTimeUpdatable(updatable);

        //when
        updater.update(Updater.UpdateFrequency.PER_SHIFT);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_OUT_TIME);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_IN_TIME);
        updater.update(Updater.UpdateFrequency.DAILY);
        updater.update(Updater.UpdateFrequency.MONTHLY);
        updater.update(Updater.UpdateFrequency.YEARLY);
        //then
        assertEquals(1,updatable.counter);

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
        updater.update(Updater.UpdateFrequency.PER_SHIFT);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_OUT_TIME);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_IN_TIME);
        updater.update(Updater.UpdateFrequency.DAILY);
        updater.update(Updater.UpdateFrequency.MONTHLY);
        updater.update(Updater.UpdateFrequency.YEARLY);
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
        updater.update(Updater.UpdateFrequency.PER_SHIFT);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_OUT_TIME);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_IN_TIME);
        updater.update(Updater.UpdateFrequency.DAILY);
        updater.update(Updater.UpdateFrequency.MONTHLY);
        updater.update(Updater.UpdateFrequency.YEARLY);
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
        updater.update(Updater.UpdateFrequency.PER_SHIFT);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_OUT_TIME);
        updater.update(Updater.UpdateFrequency.DAILY_AT_CHECK_IN_TIME);
        updater.update(Updater.UpdateFrequency.DAILY);
        updater.update(Updater.UpdateFrequency.MONTHLY);
        updater.update(Updater.UpdateFrequency.YEARLY);
        //then
        assertEquals(1,updatable.counter);

    }


}
