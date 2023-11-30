package pl.agh.edu.ui.component.modal.employee;

import static com.badlogic.gdx.utils.Align.center;
import static java.math.BigDecimal.ZERO;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.resolution.Size.MEDIUM;
import static pl.agh.edu.ui.utils.SkinToken.EASE;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.employee.EmployeeSalaryHandler;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.hired.HiredEmployeeHandler;
import pl.agh.edu.ui.component.button.LabeledButton;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.label.ValueTag;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.modal.utils.BaseModal;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.slider.MoneySliderComponent;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.utils.CustomBigDecimal;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

public class ManageEmployeeModal extends BaseModal {
	private LanguageLabel titleLabel;
	private CustomLabel nameLabel;
	private final Table rightTable = new Table();
	private final Table leftTable = new Table();
	private BigDecimal selectedBonus = BigDecimal.valueOf(300L);
	private final LabeledButton bonusButton = new LabeledButton(MEDIUM, getBonusButtonLanguageString());
	private final HiredEmployee hiredEmployee;
	private final HiredEmployeeHandler hiredEmployeeHandler;
	private final EmployeeSalaryHandler employeeSalaryHandler;

	private final Runnable refreshAction;
	private final LanguageLabel dialogLabel;
	private MoneySliderComponent bonusSlider;

	public ManageEmployeeModal(
			HiredEmployee hiredEmployee,
			HiredEmployeeHandler hiredEmployeeHandler,
			EmployeeSalaryHandler employeeSalaryHandler,
			Runnable refreshAction) {
		super();

		this.hiredEmployee = hiredEmployee;
		this.hiredEmployeeHandler = hiredEmployeeHandler;
		this.employeeSalaryHandler = employeeSalaryHandler;
		this.refreshAction = refreshAction;

		this.bonusSlider = createBonusSlider(hiredEmployee);
		this.dialogLabel = createDialogLabel(hiredEmployee);

		initModal();
		this.setResolutionChangeHandler(this::resize);
		this.onResolutionChange();
	}

	private void initModal() {

		innerTable.row();
		innerTable.add(leftTable).grow().uniform();
		innerTable.add(rightTable).grow().uniform();
		innerTable.row();

		leftTable.setBackground(new NinePatchDrawable(skin.getPatch("hal-divider-left-background")));
		leftTable.add(createPhoto()).growX().row();
		leftTable.add(createName(hiredEmployee)).growX().bottom().row();
		leftTable.add(dialogLabel).growX().top().row();
		leftTable.add(createButtonTable(hiredEmployee, hiredEmployeeHandler, () -> {
			refreshAction.run();
			recreateEmployeeInformation();
		}, employeeSalaryHandler)).grow().bottom().row();

		setUpEmployeeInformation();
	}

	private void setUpEmployeeInformation() {
		rightTable.add(createTitleLabel()).grow().expandY().bottom().row();
		rightTable.add(createAgeTag(hiredEmployee)).growX().expandY().bottom().row();
		rightTable.add(createSalaryTag(hiredEmployee)).growX().expandY().bottom().row();
		rightTable.add(createPositionTag(hiredEmployee)).growX().expandY().bottom().row();
		rightTable.add(createLevelTag(hiredEmployee)).growX().expandY().bottom().row();
		rightTable.add(createShiftTag(hiredEmployee)).growX().expandY().bottom().row();
		rightTable.add(createContractTag(hiredEmployee)).growX().expandY().bottom().row();
		rightTable.add(createSatisfactionTag(hiredEmployee)).growX().expandY().bottom().row();
		rightTable.add(bonusSlider).growX().expandY().row();
	}

	private void recreateEmployeeInformation() {
		rightTable.clearChildren();
		this.bonusSlider = createBonusSlider(hiredEmployee);
		setUpEmployeeInformation();
	}

	private Actor createTitleLabel() {
		LanguageLabel languageLabel = new LanguageLabel(new LanguageString("employee.manage.title"), ManageEmployeeModalStyle.getTitleFont());
		languageLabel.setAlignment(center, center);
		titleLabel = languageLabel;
		return languageLabel;
	}

	private Actor createName(HiredEmployee hiredEmployee) {
		nameLabel = new CustomLabel(ManageEmployeeModalStyle.getTitleFont());
		nameLabel.setText(hiredEmployee.firstName + " " + hiredEmployee.lastName);
		nameLabel.setAlignment(center, center);
		return nameLabel;
	}

	private Actor createButtonTable(HiredEmployee hiredEmployee, HiredEmployeeHandler hiredHiredEmployeeHandler, Runnable postAction, EmployeeSalaryHandler employeeSalaryHandler) {
		Table buttonTable = new Table();

		buttonTable.add(bonusButton).grow().uniform().bottom();
		bonusButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (bonusButton.isDisabled())
					return;
				CLICK.playSound();
				employeeSalaryHandler.giveBonus(hiredEmployee, selectedBonus);
				postAction.run();
				recreateEmployeeInformation();

			}
		});

		LabeledButton hireButton = new LabeledButton(MEDIUM, new LanguageString("employee.hire.button.hire"));
		buttonTable.add(hireButton).grow().uniform().bottom();
		hireButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (hireButton.isDisabled())
					return;
				CLICK.playSound();
				ModalManager.getInstance().showHireEmployeeModal(hiredEmployee, hiredHiredEmployeeHandler, postAction);
				postAction.run();
			}
		});

		buttonTable.row();

		LabeledButton backButton = new LabeledButton(MEDIUM, new LanguageString("employee.hire.button.back"));
		buttonTable.add(backButton).grow().uniform().bottom();
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (backButton.isDisabled())
					return;
				CLICK.playSound();
				ModalManager.getInstance().closeModal();
			}
		});

		LabeledButton fireButton = new LabeledButton(MEDIUM, new LanguageString("employee.hire.button.fire"));
		fireButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (fireButton.isDisabled())
					return;
				CLICK.playSound();
				hiredHiredEmployeeHandler.fireEmployee(hiredEmployee);
				postAction.run();
				bonusButton.setDisabled(true);
				fireButton.setDisabled(true);
				bonusSlider.setDisable(true);
				hireButton.setDisabled(true);
				dialogLabel.updateLanguageString(getDialogLanguageString(hiredEmployee));
			}
		});
		buttonTable.add(fireButton).grow().uniform().bottom();
		switch (hiredEmployee.getStatus()) {
			case PENDING -> {
				hireButton.setDisabled(true);
				bonusButton.setDisabled(true);
				bonusSlider.setDisable(true);
				fireButton.setDisabled(true);
			}
			case TERMINATED -> {
				hireButton.setDisabled(true);
				bonusButton.setDisabled(true);
				fireButton.setDisabled(true);
				bonusSlider.setDisable(true);
			}
		}
		return buttonTable;
	}

	public LanguageString getDialogLanguageString(HiredEmployee hiredEmployee) {
		return new LanguageString("employee.status." + hiredEmployee.getStatus().toString().toLowerCase());
	}

	private Actor createPhoto() {
		Image image = new Image(skin.getDrawable("default"));
		Container<Image> container = new Container<>(image);
		container.size(ManageEmployeeModalStyle.getPhotoSize());
		return container;
	}

	private Actor createAgeTag(HiredEmployee hiredEmployee) {
		return new ValueTag(new LanguageString("employee.stats.age"), Integer.toString(hiredEmployee.age));
	}

	private Actor createPositionTag(HiredEmployee hiredEmployee) {
		return new ValueTag(new LanguageString("employee.stats.position"), hiredEmployee.profession.languageString);
	}

	private Actor createLevelTag(HiredEmployee hiredEmployee) {
		Container<Rating> container = new Container<>(new Rating(hiredEmployee.skills.multiply(BigDecimal.valueOf(5L)).intValue()));
		container.padLeft(ManageEmployeeModalStyle.getRadiusPadding());
		return new ValueTag(new LanguageString("employee.stats.level"), container);
	}

	private Actor createShiftTag(HiredEmployee hiredEmployee) {
		return new ValueTag(new LanguageString("employee.stats.shift"), new LanguageString("employee.shift." + hiredEmployee.getShift().name().toLowerCase()));
	}

	private Actor createContractTag(HiredEmployee hiredEmployee) {
		return new ValueTag(new LanguageString("employee.stats.contract"), new LanguageString("employee.contract." + hiredEmployee.getTypeOfContract().name().toLowerCase()));
	}

	private Actor createSalaryTag(HiredEmployee hiredEmployee) {
		return new ValueTag(new LanguageString("employee.stats.salary"), new CustomBigDecimal(hiredEmployee.getWage()).toString());
	}

	private Actor createSatisfactionTag(HiredEmployee hiredEmployee) {
		Container<Rating> container = new Container<>(new Rating(hiredEmployee.getSatisfaction().multiply(BigDecimal.valueOf(5L)).intValue()));
		container.padLeft(ManageEmployeeModalStyle.getRadiusPadding());
		return new ValueTag(new LanguageString("employee.stats.satisfaction"), container);
	}

	private LanguageString getBonusButtonLanguageString() {
		return new LanguageString("employee.manage.button.bonus", List.of(Pair.of("bonus", new CustomBigDecimal(selectedBonus).toString())));
	}

	private MoneySliderComponent createBonusSlider(HiredEmployee hiredEmployee) {
		Function<BigDecimal, Void> function = selectedOption -> {
			selectedBonus = selectedOption;
			bonusButton.updateLanguageString(getBonusButtonLanguageString());
			return null;
		};

		return new MoneySliderComponent(
				new LanguageString("employee.stats.bonus"),
				ZERO,
				hiredEmployee.getWage(),
				function,
				selectedBonus);
	}

	private LanguageLabel createDialogLabel(HiredEmployee hiredEmployee) {
		LanguageLabel languageLabel = new LanguageLabel(
				getDialogLanguageString(hiredEmployee),
				ManageEmployeeModalStyle.getFont(),
				EASE);
		languageLabel.minHeight(0f);
		languageLabel.maxHeight(ManageEmployeeModalStyle.getDialogHeight());
		languageLabel.setBackground("label-glass-background");
		languageLabel.setWrap(true);
		languageLabel.pad(ManageEmployeeModalStyle.getDialogPadding());
		return languageLabel;
	}

	private void resize() {
		nameLabel.setFont(ManageEmployeeModalStyle.getTitleFont());
		titleLabel.setFont(ManageEmployeeModalStyle.getTitleFont());

		rightTable.padBottom(ManageEmployeeModalStyle.getBottomPadding());
		leftTable.padBottom(ManageEmployeeModalStyle.getBottomPadding());
	}

	private static class ManageEmployeeModalStyle {
		private static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SkinFont.SUBTITLE3.getName();
				case MEDIUM, LARGE -> SkinFont.SUBTITLE2.getName();
			};
		}

		private static String getTitleFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SkinFont.H4.getName();
				case MEDIUM, LARGE -> SkinFont.H3.getName();
			};
		}

		private static float getDialogPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 50f;
				case LARGE -> 80f;
			};
		}

		private static float getDialogHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 70f;
				case MEDIUM -> 120f;
				case LARGE -> 180f;
			};
		}

		private static float getBottomPadding() {
			return 20f;
		}

		private static float getRadiusPadding() {
			return 20f;
		}

		private static float getPhotoSize() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 150f;
				case MEDIUM -> 200f;
				case LARGE -> 300f;
			};
		}
	}
}
