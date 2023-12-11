package pl.agh.edu.ui.frame.room;

import static pl.agh.edu.ui.component.table.CustomTable.createCustomLabel;

import java.util.Arrays;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomManager;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class RoomFrame extends BaseFrame {
	public final RoomManager roomManager = getEngine().roomManager;

	public RoomFrame() {
		super(new LanguageString("navbar.button.rooms"));
		CustomTable<Room> roomTable = new CustomTable.CustomTableBuilder<Room>()
				.addColumn(new LanguageString("roomTable.column.photo"), this::createRoomPhoto, 2)
				.addColumn(new LanguageString("roomTable.column.size"), this::createSize, 2)
				.addColumn(new LanguageString("roomTable.column.rank"), this::createRank, 2)
				.addColumn(new LanguageString("roomTable.column.price"), this::createPrice, 2)
				.addColumn(new LanguageString("roomTable.column.inPossession"), this::createNumberInPossession, 2)
				.build();

		Arrays.stream(RoomSize.values())
				.flatMap(size -> Arrays.stream(RoomRank.values())
						.map(rank -> new Room(rank, size)))
				.forEach(room -> roomTable.addRow(room, System.out::println, false));
		mainTable.add(roomTable).grow();
	}

	private Actor createRoomPhoto(Room room) {
		Image image = new Image(getGameSkin().getDrawable("default"));
		Container<Image> container = new Container<>(image);
		container.size(50f);
		return container;
	}

	private Actor createSize(Room room) {
		return CustomTable.createLanguageLabel(new LanguageString("room.size." + room.size.name().toLowerCase()));
	}

	private Actor createRank(Room room) {
		return CustomTable.createLanguageLabel(new LanguageString("room.rank." + room.getRank().name().toLowerCase()));
	}

	private Actor createPrice(Room room) {
		return createCustomLabel(roomManager.roomPricePerNight.getPrice(room) + "$");
	}

	private Actor createNumberInPossession(Room room) {
		return createCustomLabel(String.valueOf(roomManager.getRoomNumberByRankSize(room.getRank(), room.size)));
	}
}
