package pl.agh.edu.model.client;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.model.Opinion;

public class Client {
	private final int age;
	private final Sex sex;
	private final HotelVisitPurpose hotelVisitPurpose;
	private Opinion opinion;

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

	public Opinion getOpinion() {
		return opinion;
	}

	public void setOpinion(Opinion opinion) {
		this.opinion = opinion;
	}

}
