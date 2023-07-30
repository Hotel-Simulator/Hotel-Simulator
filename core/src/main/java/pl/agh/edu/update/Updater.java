package pl.agh.edu.update;

import java.util.ArrayList;
import java.util.List;

public class Updater{

    private static Updater instance;

    public static Updater getInstance() {
        if(instance == null) instance = new Updater();
        return instance;
    }


    private final List<PerShiftUpdatable> perShiftUpdatableList;
    private final List<DailyAtCheckOutTimeUpdatable> dailyAtCheckOutTimeUpdatableList;
    private final List<DailyAtCheckInTimeUpdatable> dailyAtCheckInTimeUpdatableList;
    private final List<DailyUpdatable> dailyUpdatableList;
    private final List<MonthlyUpdatable> monthlyUpdatableList;
    private final List<YearlyUpdatable> yearlyUpdatableList;


    private Updater() {
        perShiftUpdatableList = new ArrayList<>();
        dailyAtCheckOutTimeUpdatableList = new ArrayList<>();
        dailyAtCheckInTimeUpdatableList = new ArrayList<>();
        dailyUpdatableList = new ArrayList<>();
        monthlyUpdatableList = new ArrayList<>();
        yearlyUpdatableList = new ArrayList<>();
    }
    public void registerPerShiftUpdatable(PerShiftUpdatable perShiftUpdatable){
        perShiftUpdatableList.add(perShiftUpdatable);}
    public void registerDailyAtCheckOutTimeUpdatable(DailyAtCheckOutTimeUpdatable dailyAtCheckOutTimeUpdatable){
        dailyAtCheckOutTimeUpdatableList.add(dailyAtCheckOutTimeUpdatable);
    }
    public void registerDailyAtCheckInTimeUpdatable(DailyAtCheckInTimeUpdatable dailyAtCheckInTimeUpdatable){
        dailyAtCheckInTimeUpdatableList.add(dailyAtCheckInTimeUpdatable);
    }
    public void registerDailyUpdatable(DailyUpdatable dailyUpdatable){
        dailyUpdatableList.add(dailyUpdatable);
    }
    public void registerMonthlyUpdatable(MonthlyUpdatable monthlyUpdatable){monthlyUpdatableList.add(monthlyUpdatable);}
    public void registerYearlyUpdatable(YearlyUpdatable yearlyUpdatable){
        yearlyUpdatableList.add(yearlyUpdatable);
    }

    private void perShiftUpdate(){
        perShiftUpdatableList.forEach(PerShiftUpdatable::perShiftUpdate);}
    private void dailyAtHotelCheckOutTimeUpdate(){
        dailyAtCheckOutTimeUpdatableList.forEach(DailyAtCheckOutTimeUpdatable::dailyAtCheckOutTimeUpdate);
    }
    private void dailyAtHotelCheckInTimeUpdate(){
        dailyAtCheckInTimeUpdatableList.forEach(DailyAtCheckInTimeUpdatable::dailyAtCheckInTimeUpdate);
    }
    private void dailyUpdate(){
        dailyUpdatableList.forEach(DailyUpdatable::dailyUpdate);
    }
    private void monthlyUpdate(){
        monthlyUpdatableList.forEach(MonthlyUpdatable::monthlyUpdate);
    }
    private void yearlyUpdate(){
        yearlyUpdatableList.forEach(YearlyUpdatable::yearlyUpdate);
    }

    public void update(UpdateFrequency updateFrequency){
        switch(updateFrequency){
            case DAILY_AT_CHECK_IN_TIME: {
                dailyAtHotelCheckInTimeUpdate();
                break;
            }
            case DAILY_AT_CHECK_OUT_TIME: {
                dailyAtHotelCheckOutTimeUpdate();
                break;
            }
            case YEARLY: {
                yearlyUpdate();
            }
            case MONTHLY: {
                monthlyUpdate();
            }
            case DAILY: {
                dailyUpdate();
            }
            case PER_SHIFT: {
                perShiftUpdate();
            }
        }
    }

    public enum UpdateFrequency{
        YEARLY,
        MONTHLY,
        DAILY_AT_CHECK_IN_TIME,
        DAILY_AT_CHECK_OUT_TIME,
        DAILY,
        PER_SHIFT,
    }

}
