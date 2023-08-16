package pl.agh.edu.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.json.data_loader.JSONRoomDataLoader;
import pl.agh.edu.logo.RandomLogoCreator;
import pl.agh.edu.model.client.ClientGroup;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.EmployeeStatus;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.room_builder.Builder;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class Hotel {
	private String hotelName;
	private Long hotelId;
	private ArrayList<Opinion> opinions = new ArrayList<>();
	private ArrayList<Employee> employees = new ArrayList<>();
	private HashMap<RoomRank, ArrayList<Room>> roomsByRank = new HashMap<>();
	private HashMap<Integer, ArrayList<Room>> roomsByCapacity = new HashMap<>();
	private ArrayList<Builder> builders = new ArrayList<>();
	private ArrayList<Room> rooms = new ArrayList<>();
	private LocalTime checkInTime;
	private LocalTime checkOutTime;
	private Integer attractiveness = null;
	private Double competitiveness;
	private RandomLogoCreator logo;
	private final TimeCommandExecutor timeCommandExecutor;
	private final Time time;

	private static final int noticePeriodInMonths = JSONEmployeeDataLoader.noticePeriodInMonths;

	public Hotel(LocalTime checkInTime, LocalTime checkOutTime) {
		this.rooms = new ArrayList<>();
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.time = Time.getInstance();

		Map<String, Long> attractivenessConstants = JSONHotelDataLoader.attractivenessConstants;
		this.attractiveness = (int) (attractivenessConstants.get("local_market") + attractivenessConstants.get("local_attractions"));

		for (RoomRank rank : RoomRank.values()) {
			roomsByRank.put(rank, new ArrayList<Room>());
		}

		for (int i = 1; i < JSONRoomDataLoader.maxSize; i++) {
			roomsByCapacity.put(i, new ArrayList<Room>());
		}

		for (Room room : rooms) {
			roomsByRank.get(room.getRank()).add(room);
			roomsByCapacity.get(room.getCapacity()).add(room);
		}

		builders.add(new Builder());
	}

	public Hotel() {
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.time = Time.getInstance();
		this.initializeStartingHotelData();
	}

	public void initializeStartingHotelData() {
		Map<String, Long> attractivenessConstants = JSONHotelDataLoader.attractivenessConstants;
		this.attractiveness = (int) (attractivenessConstants.get("local_market") + attractivenessConstants.get("local_attractions"));

		Map<String, Integer> hotelStartingValues = JSONHotelDataLoader.initialData;
		Map<String, LocalTime> hotelCheckInOutTimes = JSONHotelDataLoader.checkInAndOutTime;

		Stream.of(RoomRank.values()).forEach(e -> this.roomsByRank.put(e, new ArrayList<>()));
		IntStream.range(0, JSONRoomDataLoader.maxSize).forEach(e -> this.roomsByCapacity.put(e, new ArrayList<>()));

		IntStream.range(0, hotelStartingValues.get("builder")).forEach(e -> {
			builders.add(new Builder());
		});

		IntStream.range(0, hotelStartingValues.get("room_size_1")).forEach(
				e -> {
					Room newRoom = new Room(RoomRank.ONE, 1);
					this.rooms.add(newRoom);
					this.roomsByRank.get(RoomRank.ONE).add(newRoom);
					this.roomsByCapacity.get(1).add(newRoom);
				});

		IntStream.range(0, hotelStartingValues.get("room_size_2")).forEach(
				e -> {
					Room newRoom = new Room(RoomRank.ONE, 2);
					this.rooms.add(newRoom);
					this.roomsByRank.get(RoomRank.ONE).add(newRoom);
					this.roomsByCapacity.get(2).add(newRoom);
				});

		IntStream.range(0, hotelStartingValues.get("room_size_3")).forEach(
				e -> {
					Room newRoom = new Room(RoomRank.ONE, 3);
					this.rooms.add(newRoom);
					this.roomsByRank.get(RoomRank.ONE).add(newRoom);
					this.roomsByCapacity.get(3).add(newRoom);
				});

		IntStream.range(0, hotelStartingValues.get("room_size_4")).forEach(
				e -> {
					Room newRoom = new Room(RoomRank.ONE, 4);
					this.rooms.add(newRoom);
					this.roomsByRank.get(RoomRank.ONE).add(newRoom);
					this.roomsByCapacity.get(4).add(newRoom);
				});

		this.checkInTime = hotelCheckInOutTimes.get("check_in");
		this.checkOutTime = hotelCheckInOutTimes.get("check_out");

	}

	public ArrayList<Employee> getEmployees() {
		return employees;
	}

	public void hireEmployee(Employee employee) {
		this.employees.add(employee);
		timeCommandExecutor.addCommand(
				new TimeCommand(() -> employee.setStatus(EmployeeStatus.HIRED_WORKING),
						LocalDateTime.of(time.getTime()
								.toLocalDate()
								.minusDays(time.getTime().getDayOfMonth() - 1)
								.plusMonths(1),
								LocalTime.MIDNIGHT)));
	}

	public void fireEmployee(Employee employee) {
		employee.setStatus(EmployeeStatus.FIRED_WORKING);
		timeCommandExecutor.addCommand(
				new TimeCommand(() -> this.removeEmployee(employee),
						LocalDateTime.of(LocalDate.of(time.getTime().getYear(), time.getTime().getMonth(), 1).plusMonths(noticePeriodInMonths + 1), LocalTime.MIDNIGHT)
								.minusSeconds(1)));
	}

	public void removeEmployee(Employee employee) {
		employees.remove(employee);
	}

	public List<Employee> getWorkingEmployeesByProfession(Profession profession) {
		return employees.stream()
				.filter(employee -> employee.getStatus() != EmployeeStatus.HIRED_NOT_WORKING)
				.filter(employee -> employee.getProfession() == profession)
				.collect(Collectors.toList());
	}

	public void monthlyUpdate() {
		employees.forEach(Employee::update);
	}

	public void setEmployees(ArrayList<Employee> employees) {
		this.employees = employees;
	}

	public HashMap<RoomRank, ArrayList<Room>> getRoomsByRank() {
		return roomsByRank;
	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}

	public void setRoomsByRank(HashMap<RoomRank, ArrayList<Room>> roomsByRank) {
		this.roomsByRank = roomsByRank;
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

	public Integer getAttractiveness() {
		return attractiveness;
	}

	public Double getCompetitiveness() {
		return competitiveness;
	}

	public void setCompetitiveness(Double competitiveness) {
		this.competitiveness = competitiveness;
	}

	public HashMap<Integer, ArrayList<Room>> getRoomsByCapacity() {
		return roomsByCapacity;
	}

	public void setRoomsByCapacity(HashMap<Integer, ArrayList<Room>> roomsByCapacity) {
		this.roomsByCapacity = roomsByCapacity;
	}

	public void setPrices(ArrayList<Room> roomsToSet, BigDecimal newPrice) {
		for (Room room : roomsToSet) {
			room.setRentPrice(newPrice);
		}
	}

	public void addBuilder() {
		builders.add(new Builder());
	}

	public void updateCompetitveness() {
		BigDecimal avgRoomStandard = BigDecimal.valueOf(0);
		BigDecimal avgWorkerHappiness = BigDecimal.valueOf(0);
		Double avgOpinionValue = 0.;

		for (Room room : rooms) {
			avgRoomStandard.add(room.getStandard());
		}

		for (Employee employee : employees) {
			avgWorkerHappiness.add(BigDecimal.valueOf(employee.getSatisfaction()));
		}

		for (Opinion opinion : opinions) {
			avgOpinionValue += opinion.getValue();
		}

		avgRoomStandard.divide(BigDecimal.valueOf(rooms.size()));
		avgWorkerHappiness.divide(BigDecimal.valueOf(employees.size()));
		avgOpinionValue /= opinions.size();

		competitiveness = (avgOpinionValue + avgRoomStandard.doubleValue() + avgWorkerHappiness.doubleValue()) / 3;
	}

	public void upgradeRoom(Room room, int numUpgrades) {
		RoomRank prev = room.getRank();

		for (Builder builder : builders) {
			if (!builder.isOccupied()) {
				builder.upgradeRoom(room, numUpgrades);
				int intervalMinutes = 5;

				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						if (builder.finishUpgrade())
							timer.cancel();
					}
				};
				timer.schedule(task, 0, intervalMinutes * 60 * 1000);
				break;
			}
		}

		roomsByRank.get(prev).remove(room);
		roomsByRank.get(room.getRank()).add(room);
	}

	public Room findRoomForClientGroup(ClientGroup group) {
		for (Room room : roomsByRank.get(group.getDesiredRoomRank())) {
			if (room.getState().equals(RoomState.EMPTY) && room.getRentPrice().compareTo(group.getDesiredPricePerNight()) < 1) {
				return room;
			}
		}
		return null;
	}

	public int getEmployeesNumber() {
		return this.employees.size();
	}

	// public void checkOutGuest(ClientGroup group){
	// group.generateOpinion();
	// Opinion opinion = group.getOpinion();
	//
	// this.opinions.add(opinion);
	// }
}
