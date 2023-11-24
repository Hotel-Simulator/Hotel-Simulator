package pl.agh.edu.engine.hotel;

import java.time.LocalTime;

import pl.agh.edu.data.loader.JSONHotelDataLoader;

public class Hotel {
	private String hotelName = JSONHotelDataLoader.hotelName;
	private Long hotelId;
	private LocalTime checkInTime = JSONHotelDataLoader.checkInAndOutTime.get("check_in");
	private LocalTime checkOutTime = JSONHotelDataLoader.checkInAndOutTime.get("check_out");
	private final LocalTime maxCheckInTime = JSONHotelDataLoader.checkInAndOutTime.get("max_check_in");
	private final LocalTime minCheckInTime = JSONHotelDataLoader.checkInAndOutTime.get("min_check_in");
	private final LocalTime maxCheckOutTime = JSONHotelDataLoader.checkInAndOutTime.get("max_check_out");
	private final LocalTime minCheckOutTime = JSONHotelDataLoader.checkInAndOutTime.get("min_check_out");

	public Hotel() {}

	public LocalTime getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(LocalTime checkInTime) {
		this.checkInTime = checkInTime;
	}

	public LocalTime getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(LocalTime checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public LocalTime getMaxCheckInTime() {
		return maxCheckInTime;
	}

	public LocalTime getMinCheckInTime() {
		return minCheckInTime;
	}

	public LocalTime getMaxCheckOutTime() {
		return maxCheckOutTime;
	}

	public LocalTime getMinCheckOutTime() {
		return minCheckOutTime;
	}
}
