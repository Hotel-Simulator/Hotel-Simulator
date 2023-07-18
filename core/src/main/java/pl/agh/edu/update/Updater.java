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
    private final List<DailyUpdatable> dailyUpdatableList;
    private final List<MonthlyUpdatable> monthlyUpdatableList;
    private final List<YearlyUpdatable> yearlyUpdatableList;

    private Updater() {
        dailyUpdatableList = new ArrayList<>();
        monthlyUpdatableList = new ArrayList<>();
        yearlyUpdatableList = new ArrayList<>();
        perShiftUpdatableList = new ArrayList<>();
    }
    public void registerPerShiftUpdatable(PerShiftUpdatable perShiftUpdatable){
        perShiftUpdatableList.add(perShiftUpdatable);}
    public void registerDailyUpdatable(DailyUpdatable dailyUpdatable){
        dailyUpdatableList.add(dailyUpdatable);
    }
    public void registerMonthlyUpdatable(MonthlyUpdatable monthlyUpdatable){monthlyUpdatableList.add(monthlyUpdatable);}
    public void registerYearlyUpdatable(YearlyUpdatable yearlyUpdatable){
        yearlyUpdatableList.add(yearlyUpdatable);
    }

    public void perShiftUpdate(){
        perShiftUpdatableList.forEach(PerShiftUpdatable::perShiftUpdate);}
    public void dailyUpdate(){
        dailyUpdatableList.forEach(DailyUpdatable::dailyUpdate);
    }
    public void monthlyUpdate(){
        monthlyUpdatableList.forEach(MonthlyUpdatable::monthlyUpdate);
    }
    public void yearlyUpdate(){
        yearlyUpdatableList.forEach(YearlyUpdatable::yearlyUpdate);
    }


}
