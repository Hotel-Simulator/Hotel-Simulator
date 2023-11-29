package pl.agh.edu.ui.component.modal.employee;

import static com.badlogic.gdx.utils.Align.center;
import static java.math.BigDecimal.ZERO;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.resolution.Size.MEDIUM;

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
	private final LabeledButton giveBonusButton = new LabeledButton(MEDIUM, getBonusButtonLanguageString());
	private final HiredEmployee hiredEmployee;

	public ManageEmployeeModal(
			HiredEmployee hiredEmployee,
			HiredEmployeeHandler hiredEmployeeHandler,
			EmployeeSalaryHandler employeeSalaryHandler,
			Runnable refreshAction) {
		super();

		this.hiredEmployee = hiredEmployee;

		initModal(hiredEmployee, hiredEmployeeHandler, employeeSalaryHandler, refreshAction);
		this.setResolutionChangeHandler(this::resize);
		this.onResolutionChange();
	}

	private void initModal(
			HiredEmployee hiredEmployee,
			HiredEmployeeHandler hiredEmployeeHandler,
			EmployeeSalaryHandler employeeSalaryHandler,
			Runnable refreshAction) {

		innerTable.row();
		innerTable.add(leftTable).grow().uniform();
		innerTable.add(rightTable).grow().uniform();
		innerTable.row();

		leftTable.setBackground(new NinePatchDrawable(skin.getPatch("hal-divider-left-background")));
		leftTable.add(createPhoto()).growX().expandY().row();
		leftTable.add(createName(hiredEmployee)).growX().row();
		leftTable.add(createBonusSlider(hiredEmployee)).uniform().growX().expandY().bottom().row();
		leftTable.add(createButtonTable(hiredEmployee, hiredEmployeeHandler, () -> {
			refreshAction.run();
			recreateEmployeeInformation();
		}, employeeSalaryHandler)).growX().expandY().bottom().row();

		setUpEmployeeInformation();
	}

	private void setUpEmployeeInformation() {
		rightTable.add(createTitleLabel()).uniform().grow().expandY().bottom().row();
		rightTable.add(createAgeTag(hiredEmployee)).uniform().growX().expandY().bottom().row();
		rightTable.add(createSalaryTag(hiredEmployee)).uniform().growX().expandY().bottom().row();
		rightTable.add(createPositionTag(hiredEmployee)).uniform().growX().expandY().bottom().row();
		rightTable.add(createLevelTag(hiredEmployee)).uniform().growX().expandY().bottom().row();
		rightTable.add(createShiftTag(hiredEmployee)).uniform().growX().expandY().bottom().row();
		rightTable.add(createContractTag(hiredEmployee)).uniform().growX().expandY().bottom().row();
		rightTable.add(createSatisfactionTag(hiredEmployee)).uniform().growX().expandY().bottom().row();
		rightTable.add(createBonusTag(hiredEmployee)).uniform().growX().expandY().bottom().row();
	}

	private void recreateEmployeeInformation() {
		rightTable.clearChildren();
		setUpEmployeeInformation();
	}

	private Actor createTitleLabel() {
		LanguageLabel languageLabel = new LanguageLabel(new LanguageString("employee.manage.title"), ManageEmployeeModalStyle.getTitleFont());
		languageLabel.setAlignment(center, center);
		titleLabel = languageLabel;
		return languageLabel;
	}

	private Actor createPhoto() {
		Image image = new Image(skin.getDrawable("default"));
		Container<Image> container = new Container<>(image);
		container.size(ManageEmployeeModalStyle.getPhotoSize());
		return container;
	}

	private Actor createName(HiredEmployee hiredEmployee) {
		nameLabel = new CustomLabel(ManageEmployeeModalStyle.getTitleFont());
		nameLabel.setText(hiredEmployee.firstName + " " + hiredEmployee.lastName);
		nameLabel.setAlignment(center, center);
		return nameLabel;
	}

	private Actor createButtonTable(HiredEmployee hiredEmployee, HiredEmployeeHandler hiredHiredEmployeeHandler, Runnable postAction, EmployeeSalaryHandler employeeSalaryHandler) {
		Table buttonTable = new Table();
		LabeledButton backButton = new LabeledButton(MEDIUM, new LanguageString("employee.hire.button.back"));
		buttonTable.add(backButton).growX().expandY().uniform();
		buttonTable.add(giveBonusButton).growX().expandY().uniform();
		LabeledButton hireButton = new LabeledButton(MEDIUM, new LanguageString("employee.hire.button.hire"));
		buttonTable.add(hireButton).growX().expandY().uniform();

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				CLICK.playSound();
				ModalManager.getInstance().closeModal();
			}
		});

		giveBonusButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				CLICK.playSound();
				employeeSalaryHandler.giveBonus(hiredEmployee, selectedBonus);
				postAction.run();
				recreateEmployeeInformation();

			}
		});

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

		return buttonTable;
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

	private Actor createBonusTag(HiredEmployee hiredEmployee) {
		return new ValueTag(new LanguageString("employee.stats.bonuses"), new CustomBigDecimal(hiredEmployee.getTotalBonus()).toString());
	}

	private Actor createSatisfactionTag(HiredEmployee hiredEmployee) {
		return new ValueTag(new LanguageString("employee.stats.satisfaction"), hiredEmployee.getSatisfaction().multiply(BigDecimal.valueOf(100)) + "%");
	}

	private LanguageString getBonusButtonLanguageString() {
		return new LanguageString("employee.manage.button.bonus", List.of(Pair.of("bonus", new CustomBigDecimal(selectedBonus).toString())));
	}

	private Actor createBonusSlider(HiredEmployee hiredEmployee) {
		Function<BigDecimal, Void> function = selectedOption -> {
			selectedBonus = selectedOption;
			giveBonusButton.updateLanguageString(getBonusButtonLanguageString());
			return null;
		};

		MoneySliderComponent slider = new MoneySliderComponent(
				new LanguageString("employee.stats.bonus"),
				ZERO,
				hiredEmployee.getWage(),
				function,
				selectedBonus);

		return slider;
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

		private static String getWhiteFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SkinFont.SUBTITLE3.getWhiteVariantName();
				case MEDIUM, LARGE -> SkinFont.SUBTITLE2.getWhiteVariantName();
			};
		}

		private static String getTitleFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SkinFont.H4.getName();
				case MEDIUM, LARGE -> SkinFont.H3.getName();
			};
		}

		private static float getBottomTablePadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 10f;
				case MEDIUM -> 20f;
				case LARGE -> 30f;
			};
		}

		private static float getModalPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}

		private static float getDialogPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 50f;
				case LARGE -> 80f;
			};
		}

		private static float getBottomPadding() {
			return 20f;
		}

		private static float getRadiusPadding() {
			return 20f;
		}

		private static float getPhotoSize() {
			return 250f;
		}
	}
}
