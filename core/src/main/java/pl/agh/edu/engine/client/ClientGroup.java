package pl.agh.edu.engine.client;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.opinion.Opinion;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.serialization.KryoConfig;

public class ClientGroup {
	private final List<Client> members;
	private final HotelVisitPurpose hotelVisitPurpose;
	private final BigDecimal desiredPricePerNight;
	private final RoomRank desiredRoomRank;
	private final Duration maxWaitingTime;
	private final int numberOfNights;
	public final Opinion opinion;

	public static void kryoRegister() {
		KryoConfig.kryo.register(ClientGroup.class, new Serializer<ClientGroup>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGroup object) {
				kryo.writeObject(output, object.members, KryoConfig.listSerializer(Client.class));
				kryo.writeObject(output, object.hotelVisitPurpose);
				kryo.writeObject(output, object.desiredPricePerNight);
				kryo.writeObject(output, object.desiredRoomRank);
				kryo.writeObject(output, object.maxWaitingTime);
				kryo.writeObject(output, object.numberOfNights);
				kryo.writeObject(output, object.opinion);
			}

			@Override
			public ClientGroup read(Kryo kryo, Input input, Class<? extends ClientGroup> type) {
				return new ClientGroup(
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Client.class)),
						kryo.readObject(input, HotelVisitPurpose.class),
						kryo.readObject(input, BigDecimal.class),
						kryo.readObject(input, RoomRank.class),
						kryo.readObject(input, Duration.class),
						kryo.readObject(input, Integer.class),
						kryo.readObject(input, Opinion.class));
			}
		});
	}

	private ClientGroup(Builder builder) {
		this.hotelVisitPurpose = builder.hotelVisitPurpose;
		this.members = builder.members;
		this.desiredPricePerNight = builder.desiredPricePerNight;
		this.desiredRoomRank = builder.desiredRoomRank;
		this.maxWaitingTime = builder.maxWaitingTime;
		this.numberOfNights = builder.numberOfNights;
		opinion = new Opinion(this);
	}

	private ClientGroup(List<Client> members,
			HotelVisitPurpose hotelVisitPurpose,
			BigDecimal desiredPricePerNight,
			RoomRank desiredRoomRank,
			Duration maxWaitingTime,
			int numberOfNights,
			Opinion opinion) {
		this.members = members;
		this.hotelVisitPurpose = hotelVisitPurpose;
		this.desiredPricePerNight = desiredPricePerNight;
		this.desiredRoomRank = desiredRoomRank;
		this.maxWaitingTime = maxWaitingTime;
		this.numberOfNights = numberOfNights;
		this.opinion = opinion;
	}

	public RoomRank getDesiredRoomRank() {
		return desiredRoomRank;
	}

	public int getNumberOfNights() {
		return numberOfNights;
	}

	public List<Client> getMembers() {
		return members;
	}

	@Override
	public String toString() {
		return "ClientGroup{" +

				"hotelVisitPurpose=" + hotelVisitPurpose +
				", desiredPricePerNight=" + desiredPricePerNight +
				", desiredRoomRank=" + desiredRoomRank +
				", numberOfMembers=" + members.size() +
				'}';
	}

	public Duration getMaxWaitingTime() {
		return maxWaitingTime;
	}

	public BigDecimal getDesiredPricePerNight() {
		return desiredPricePerNight;
	}

	public int getSize() {
		return members.size();
	}

	public HotelVisitPurpose getHotelVisitPurpose() {
		return hotelVisitPurpose;
	}

	public static class Builder {
		private HotelVisitPurpose hotelVisitPurpose;
		private List<Client> members;
		private BigDecimal desiredPricePerNight;
		private RoomRank desiredRoomRank;
		private Duration maxWaitingTime;
		private int numberOfNights;

		public Builder hotelVisitPurpose(HotelVisitPurpose hotelVisitPurpose) {
			this.hotelVisitPurpose = hotelVisitPurpose;
			return this;
		}

		public Builder members(List<Client> members) {
			this.members = members;
			return this;
		}

		public Builder desiredPricePerNight(BigDecimal desiredPricePerNight) {
			this.desiredPricePerNight = desiredPricePerNight;
			return this;
		}

		public Builder desiredRoomRank(RoomRank desiredRoomRank) {
			this.desiredRoomRank = desiredRoomRank;
			return this;
		}

		public Builder maxWaitingTime(Duration maxWaitingTime) {
			this.maxWaitingTime = maxWaitingTime;
			return this;
		}

		public Builder numberOfNights(int numberOfNights) {
			this.numberOfNights = numberOfNights;
			return this;
		}

		public ClientGroup build() {
			return new ClientGroup(this);
		}
	}
}
