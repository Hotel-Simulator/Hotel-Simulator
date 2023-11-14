package pl.agh.edu.engine.hotel;

import java.time.LocalTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONHotelDataLoader;
import pl.agh.edu.serialization.KryoConfig;

public class Hotel {
	private String hotelName;
	private Long hotelId;
	private LocalTime checkInTime;
	private LocalTime checkOutTime;

	static {
		KryoConfig.kryo.register(Hotel.class, new Serializer<Hotel>() {
			@Override
			public void write(Kryo kryo, Output output, Hotel object) {
				kryo.writeObject(output, object.checkInTime);
				kryo.writeObject(output, object.checkOutTime);

			}

			@Override
			public Hotel read(Kryo kryo, Input input, Class<? extends Hotel> type) {
				return new Hotel(
						kryo.readObject(input, LocalTime.class),
						kryo.readObject(input, LocalTime.class));
			}
		});
	}

	public Hotel() {
		checkInTime = JSONHotelDataLoader.checkInAndOutTime.get("check_in");
		checkOutTime = JSONHotelDataLoader.checkInAndOutTime.get("check_out");
	}

	private Hotel(LocalTime checkInTime, LocalTime checkOutTime) {
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}

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

}
