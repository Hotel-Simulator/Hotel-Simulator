package pl.agh.edu.ui.component.modal.employee;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.PossibleEmployeeHandler;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.OfferResponse;
import pl.agh.edu.engine.employee.contract.TypeOfContract;
import pl.agh.edu.ui.component.button.LabeledButton;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.label.ValueTag;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.modal.utils.BaseModal;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.selectMenu.SelectMenu;
import pl.agh.edu.ui.component.selectMenu.SelectMenuContract;
import pl.agh.edu.ui.component.selectMenu.SelectMenuItem;
import pl.agh.edu.ui.component.selectMenu.SelectMenuShift;
import pl.agh.edu.ui.component.slider.MoneySliderComponent;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

import static com.badlogic.gdx.utils.Align.center;
import static java.math.BigDecimal.ZERO;
import static pl.agh.edu.engine.employee.contract.OfferResponse.NEGATIVE;
import static pl.agh.edu.engine.employee.contract.OfferResponse.POSITIVE;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.resolution.Size.MEDIUM;
import static pl.agh.edu.ui.utils.SkinColor.SUCCESS;
import static pl.agh.edu.ui.utils.SkinColor.WARNING;
import static pl.agh.edu.ui.utils.SkinToken.EASE;

public class ManageEmployeeModal  extends BaseModal {
    private LanguageLabel titleLabel;
    private CustomLabel nameLabel;
    private final Table rightTable = new Table();
    private final Table leftTable = new Table();

    public ManageEmployeeModal(
            Employee employee,
            EmployeeHandler employeeHandler,
            Runnable refreshAction) {
        super();

        innerTable.row();
        innerTable.add(leftTable).grow().uniform();
        innerTable.add(rightTable).grow().uniform();
        innerTable.row();

        leftTable.setBackground(new NinePatchDrawable(skin.getPatch("hal-divider-left-background")));
        leftTable.add(createPhoto()).growX().expandY().row();
        leftTable.add(createName(employee)).growX().row();
        leftTable.add(createButtonTable(employee, employeeHandler, refreshAction)).growX().expandY().bottom().row();

        rightTable.add(createTitleLabel()).uniform().grow().expandY().bottom().row();
        rightTable.add(createAgeTag(employee)).uniform().growX().expandY().bottom().row();
        rightTable.add(createSalaryTag(employee)).uniform().growX().expandY().bottom().row();
        rightTable.add(createPositionTag(employee)).uniform().growX().expandY().bottom().row();
        rightTable.add(createLevelTag(employee)).uniform().growX().expandY().bottom().row();
        rightTable.add(createShiftTag(employee)).uniform().growX().expandY().bottom().row();
        rightTable.add(createContractTag(employee)).uniform().growX().expandY().bottom().row();

        this.setResolutionChangeHandler(this::resize);
        this.onResolutionChange();
    }

    private Actor createTitleLabel() {
        LanguageLabel languageLabel = new LanguageLabel(new LanguageString("employee.hire.title"), ManageEmployeeModalStyle.getTitleFont());
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

    private Actor createName(Employee employee) {
        nameLabel = new CustomLabel(ManageEmployeeModalStyle.getTitleFont());
        nameLabel.setText(employee.firstName + " " + employee.lastName);
        nameLabel.setAlignment(center, center);
        return nameLabel;
    }
    private Actor createButtonTable(Employee employee, EmployeeHandler possibleEmployeeHandler, Runnable refreshAction) {
        Table buttonTable = new Table();
        LabeledButton backButton = new LabeledButton(MEDIUM, new LanguageString("employee.hire.button.back"));
        buttonTable.add(backButton).growX().expandY().uniform();
        LabeledButton hireButton = new LabeledButton(MEDIUM, new LanguageString("employee.hire.button.hire"));
        buttonTable.add(hireButton).growX().expandY().uniform();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CLICK.playSound();
                ModalManager.getInstance().closeModal();
            }
        });

//        hireButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (hireButton.isDisabled())
//                    return;
//                CLICK.playSound();
//                handleOfferContract(possibleEmployee, possibleEmployeeHandler, hireButton);
//                refreshAction.run();
//            }
//        });

        return buttonTable;
    }
    private Actor createAgeTag(Employee employee) {
        return new ValueTag(new LanguageString("employee.stats.age"), Integer.toString(employee.age));
    }
    private Actor createPositionTag(Employee employee) {
        return new ValueTag(new LanguageString("employee.stats.position"), employee.profession.languageString);
    }

    private Actor createLevelTag(Employee employee) {
        Container<Rating> container = new Container<>(new Rating(employee.skills.multiply(BigDecimal.valueOf(5L)).intValue()));
        container.padLeft(ManageEmployeeModalStyle.getRadiusPadding());
        return new ValueTag(new LanguageString("employee.stats.level"), container);
    }

    private Actor createShiftTag(Employee employee) {
        return new ValueTag(new LanguageString("employee.stats.shift"), new LanguageString("employee.shift." + employee.preferences.desiredShift.name().toLowerCase()));
    }

    private Actor createContractTag(Employee employee) {
        return new ValueTag(new LanguageString("employee.stats.contract"), new LanguageString("employee.shift." + employee.preferences.desiredShift.name().toLowerCase()));
    }

    private Actor createSalaryTag(Employee employee) {
        return new ValueTag(new LanguageString("employee.stats.salary"), employee.preferences.desiredWage.toString());
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
