package pl.agh.edu.engine.attraction;

import java.math.BigDecimal;

import pl.agh.edu.data.loader.JSONAttractionDataLoader;

public class Attraction {
	public final AttractionType type;
	private AttractionSize size;
	private boolean isBeingBuild = true;
	private boolean isUnderSizeChange = false;

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

	public boolean isBeingBuild() {
		return isBeingBuild;
	}

	public void setBeingBuild(boolean beingBuild) {
		isBeingBuild = beingBuild;
	}

	public boolean isUnderSizeChange() {
		return isUnderSizeChange;
	}

	public void setUnderSizeChange(boolean underSizeChange) {
		isUnderSizeChange = underSizeChange;
	}
}
