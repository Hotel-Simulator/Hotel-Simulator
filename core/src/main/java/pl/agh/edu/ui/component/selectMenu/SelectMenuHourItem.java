package pl.agh.edu.ui.component.selectMenu;

import java.time.LocalTime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import pl.agh.edu.GdxGame;
import pl.agh.edu.engine.time.Time;

public class SelectMenuHourItem extends SelectMenuItem {
	public final LocalTime hour;
	public static final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
	public static final LocalTime MAX_CHECK_IN_TIME = game.engine.hotelHandler.hotel.getMaxCheckInTime();
	public static final LocalTime MIN_CHECK_IN_TIME = game.engine.hotelHandler.hotel.getMinCheckInTime();
	public static final LocalTime MAX_CHECK_OUT_TIME = game.engine.hotelHandler.hotel.getMaxCheckOutTime();
	public static final LocalTime MIN_CHECK_OUT_TIME = game.engine.hotelHandler.hotel.getMinCheckOutTime();

	public SelectMenuHourItem(LocalTime hour) {
		super(hour.toString(), hour::toString);
		this.hour = hour;
	}

	public static Array<SelectMenuItem> getArrayForCheckIn() {
		Array<SelectMenuItem> itemArray = new Array<>();

		LocalTime checkInTime = MIN_CHECK_IN_TIME;

		while (!MAX_CHECK_IN_TIME.isBefore(checkInTime)) {
			itemArray.add(new SelectMenuHourItem(checkInTime));
			checkInTime = checkInTime.plusMinutes(Time.timeUnitInMinutes);
		}

		return itemArray;
	}

	public static Array<SelectMenuItem> getArrayForCheckOut() {
		Array<SelectMenuItem> itemArray = new Array<>();
		LocalTime checkOutTime = MIN_CHECK_OUT_TIME;

		while (!MAX_CHECK_OUT_TIME.isBefore(checkOutTime)) {
			itemArray.add(new SelectMenuHourItem(checkOutTime));
			checkOutTime = checkOutTime.plusMinutes(Time.timeUnitInMinutes);
		}

		return itemArray;
	}
}
