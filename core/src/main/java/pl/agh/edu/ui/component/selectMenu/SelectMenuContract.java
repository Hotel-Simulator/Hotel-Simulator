package pl.agh.edu.ui.component.selectMenu;

import com.badlogic.gdx.utils.Array;

import pl.agh.edu.engine.employee.contract.TypeOfContract;

public class SelectMenuContract extends SelectMenuItem {
	public TypeOfContract typeOfContract;

	public SelectMenuContract(TypeOfContract typeOfContract) {
		super(typeOfContract.name(), typeOfContract::toString);
		this.typeOfContract = typeOfContract;
	}

	public static Array<SelectMenuItem> getArray() {
		Array<SelectMenuItem> itemArray = new Array<>();

		for (TypeOfContract typeOfContract : TypeOfContract.values()) {
			itemArray.add(new SelectMenuContract(typeOfContract));
		}

		return itemArray;
	}
}
