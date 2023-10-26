package pl.agh.edu.engine.client;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.opinion.Opinion;
import pl.agh.edu.engine.room.RoomRank;

public class ClientGroup {
	private final List<Client> members;
	private final HotelVisitPurpose hotelVisitPurpose;
	private final BigDecimal desiredPricePerNight;
	private final RoomRank desiredRoomRank;
	private final Duration maxWaitingTime;
	private final int numberOfNights;
	public final Opinion opinion;

	private ClientGroup(Builder builder) {
		this.hotelVisitPurpose = builder.hotelVisitPurpose;
		this.members = builder.members;
		this.desiredPricePerNight = builder.desiredPricePerNight;
		this.desiredRoomRank = builder.desiredRoomRank;
		this.maxWaitingTime = builder.maxWaitingTime;
		this.numberOfNights = builder.numberOfNights;
		opinion = new Opinion(this);
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
