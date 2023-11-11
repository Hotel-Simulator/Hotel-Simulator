package pl.agh.edu.engine.employee;

import pl.agh.edu.utils.LanguageString;

public enum Profession {
	CLEANER,
	TECHNICIAN,
	RECEPTIONIST;

	public final LanguageString languageString;

	Profession() {
		this.languageString = new LanguageString("employee.profession." + this.name().toLowerCase());
	}
}
