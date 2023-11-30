package pl.agh.edu.engine.hotel;

import java.time.LocalTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONHotelDataLoader;
import pl.agh.edu.serialization.KryoConfig;

public class Hotel {
	private final String hotelName;
	private Long hotelId;
	private LocalTime checkInTime;
	private LocalTime checkOutTime;

	public static void kryoRegister() {
		KryoConfig.kryo.register(Hotel.class, new Serializer<Hotel>() {
			@Override
			public void write(Kryo kryo, Output output, Hotel object) {
				kryo.writeObject(output, object.hotelName);
				kryo.writeObject(output, object.checkInTime);
				kryo.writeObject(output, object.checkOutTime);

			}

			@Override
			public Hotel read(Kryo kryo, Input input, Class<? extends Hotel> type) {
				return new Hotel(
						kryo.readObject(input, String.class),
						kryo.readObject(input, LocalTime.class),
						kryo.readObject(input, LocalTime.class));
			}
		});
	}

	public Hotel(String hotelName) {
		this.hotelName = hotelName;
		checkInTime = JSONHotelDataLoader.checkInAndOutTime.get("check_in");
		checkOutTime = JSONHotelDataLoader.checkInAndOutTime.get("check_out");
	}

	private Hotel(String hotelName, LocalTime checkInTime, LocalTime checkOutTime) {
		this.hotelName = hotelName;
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
