package pl.agh.edu.model;

import java.time.LocalTime;
import java.util.*;

import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.logo.RandomLogoCreator;

public class Hotel {
	private String hotelName;
	private Long hotelId;
	private LocalTime checkInTime = JSONHotelDataLoader.checkInAndOutTime.get("check_in");
	private LocalTime checkOutTime = JSONHotelDataLoader.checkInAndOutTime.get("check_out");
	private final int attractiveness = (int) (JSONHotelDataLoader.attractivenessConstants.get("local_market") +
			JSONHotelDataLoader.attractivenessConstants.get("local_attractions"));

	private RandomLogoCreator logo;

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

	public Integer getAttractiveness() {
		return attractiveness;
	}

}
