package pl.agh.edu.engine.employee.contract;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.utils.LanguageString;

public enum TypeOfContract {
	PERMANENT;

	public static void kryoRegister() {
		KryoConfig.kryo.register(TypeOfContract.class);
	}

	public LanguageString getLanguageString() {
		return new LanguageString("employee.contract." + name().toLowerCase());
	}

	@Override
	public String toString() {
		return LanguageManager.get(getLanguageString());
	}
}
