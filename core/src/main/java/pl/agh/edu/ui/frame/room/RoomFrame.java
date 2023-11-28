package pl.agh.edu.ui.frame.room;

import static pl.agh.edu.ui.component.table.CustomTable.createCustomLabel;

import java.util.Arrays;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class RoomFrame extends BaseFrame {
	public RoomFrame() {
		super(new LanguageString("navbar.button.rooms"));
		CustomTable<Room> roomTable = new CustomTable.CustomTableBuilder<Room>()
				.addColumn(new LanguageString("roomTable.column.icon"), this::createRoomIcon, 2)
				.addColumn(new LanguageString("roomTable.column.size"), this::createSize, 2)
				.addColumn(new LanguageString("roomTable.column.rank"), this::createRank, 2)
				.addColumn(new LanguageString("roomTable.column.price"), this::createPrice, 2)
				.addColumn(new LanguageString("roomTable.column.in.possession"), this::createNumberInPossession, 2)
				.build();

		Arrays.stream(RoomSize.values())
				.flatMap(size -> Arrays.stream(RoomRank.values())
						.map(rank -> new Room(rank, size)))
				.forEach(room -> roomTable.addRow(room, System.out::println, true));
		mainTable.add(roomTable).grow();
	}

	private Actor createRoomIcon(Room room) {
		Image image = new Image(skin.getDrawable("default"));
		Container<Image> container = new Container<>(image);
		container.size(50f);
		return container;
	}

	private Actor createSize(Room room) {
		return createCustomLabel(room.size.name());
	}

	private Actor createRank(Room room) {
		return createCustomLabel(room.getRank().name());
	}

	private Actor createPrice(Room room) {
		return createCustomLabel(engine.hotelHandler.roomManager.roomPricePerNight.getPrice(room) + "$");
	}

	private Actor createNumberInPossession(Room room) {
		return createCustomLabel(String.valueOf(engine.hotelHandler.roomManager.getRoomNumberByRankSize(room.getRank(), room.getSize())));
	}
}
