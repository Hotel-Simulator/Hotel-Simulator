package pl.agh.edu.engine.client;

import pl.agh.edu.engine.hotel.HotelVisitPurpose;

public record Client(int age, Sex sex, HotelVisitPurpose hotelVisitPurpose){}