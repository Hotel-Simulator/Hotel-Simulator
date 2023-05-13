package pl.agh.edu.model;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.Sex;

public class Client {
    private final int age;
    private final Sex sex;
    private final HotelVisitPurpose hotelVisitPurpose;

    public Client(int age, Sex sex, HotelVisitPurpose hotelVisitPurpose) {
        this.age = age;
        this.sex = sex;
        this.hotelVisitPurpose = hotelVisitPurpose;
    }

    public int getAge() {
        return age;
    }

    public Sex getSex() {
        return sex;
    }

    public HotelVisitPurpose getHotelVisitPurpose() {
        return hotelVisitPurpose;
    }
}
