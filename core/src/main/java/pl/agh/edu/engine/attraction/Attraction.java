package pl.agh.edu.engine.attraction;

import static pl.agh.edu.engine.attraction.AttractionState.BEING_BUILD;

import java.math.BigDecimal;

import pl.agh.edu.data.loader.JSONAttractionDataLoader;

public class Attraction {
	public final AttractionType type;
	private AttractionSize size;
	private AttractionState state = BEING_BUILD;

	public Attraction(AttractionType type, AttractionSize size) {
		this.type = type;
		this.size = size;
	}

	public int getDailyCapacity() {
		return JSONAttractionDataLoader.dailyCapacity.get(size);
	}

	public BigDecimal getDailyExpenses() {
		return JSONAttractionDataLoader.dailyExpenses.get(size);
	}

	public void setSize(AttractionSize size) {
		this.size = size;
	}

	public AttractionSize getSize() {
		return size;
	}

	public AttractionState getState() {
		return state;
	}

	public void setState(AttractionState state) {
		this.state = state;
	}
}
