package pl.agh.edu.engine.client;

import pl.agh.edu.engine.hotel.HotelVisitPurpose;

public record Client(String name, int age, Gender sex, HotelVisitPurpose hotelVisitPurpose){}
