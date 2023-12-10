package pl.agh.edu.ui.frame.bank;

import static com.badlogic.gdx.utils.Align.top;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinColor.PRIMARY;
import static pl.agh.edu.ui.utils.SkinFont.BODY2;
import static pl.agh.edu.ui.utils.SkinFont.BODY3;
import static pl.agh.edu.ui.utils.SkinFont.BUTTON1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.ValueTag;
import pl.agh.edu.ui.component.slider.MoneySliderComponent;
import pl.agh.edu.ui.component.slider.SliderComponent;
import pl.agh.edu.ui.component.tab.TabSelector;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.CustomBigDecimal;
import pl.agh.edu.utils.LanguageString;

import java.math.BigDecimal;

public class CreditFrame extends BaseFrame {

	private static BankAccount bankAccount = ((GdxGame) Gdx.app.getApplicationListener()).engine.hotelHandler.bankAccount;
	private Table contentTable = new Table();
	public CreditFrame() {
		super(new LanguageString("navbar.button.credit"));
		mainTable.add(new TabSelector(new LanguageString("credit.new.title"), new LanguageString("creditTable.yourCredits"), ()->{
			contentTable.clearChildren();
			contentTable.add(new NewCredit()).grow().row();
		}, () ->{
			contentTable.clearChildren();
			contentTable.add(new CreditTable()).grow().row();
		})).growX().row();
		contentTable.align(top);
		mainTable.add(contentTable).grow().row();
	}

	private static class NewCredit extends WrapperTable{

		ValueTag interest;
		MoneySliderComponent valueSlider;
		SliderComponent monthsSlider;
		ValueTag monthly;
		public NewCredit() {
			super();
			innerTable.align(top);
			 interest = new ValueTag(new LanguageString("valueTag.credit.interest"), "0");
			 valueSlider = new MoneySliderComponent(new LanguageString("credit.new.value"), BigDecimal.ONE	, bankAccount.getBalance().multiply(BigDecimal.TEN), n -> {
				updateMonthly();
				return null;
			}, BigDecimal.ONE);
			monthsSlider = new SliderComponent(new LanguageString("credit.new.months"),new LanguageString("credit.new.month"), 1,48,1, n -> {
				updateMonthly();
				System.out.println(monthsSlider.getValue());
				return null;
			});
			monthly = new ValueTag(new LanguageString("credit.new.monthly"), "0");
			innerTable.add(interest).space(NewCreditStyle.getBigPadding()).align(top).row();
			innerTable.add(valueSlider).space(NewCreditStyle.getSmallPadding()).row();
			innerTable.add(monthsSlider).space(NewCreditStyle.getBigPadding()).row();
			innerTable.add(monthly).space(NewCreditStyle.getBigPadding()).row();
			valueSlider.overrideWidth(NewCreditStyle::getSliderWidth);
			monthsSlider.overrideWidth(NewCreditStyle::getSliderWidth);
			interest.overrideWidth(NewCreditStyle::getValueTagWidth);
			monthly.overrideWidth(NewCreditStyle::getValueTagWidth);

		}

		public void updateMonthly(){
			CustomBigDecimal cbd = new CustomBigDecimal(valueSlider.getSliderValue().getValue().multiply(BigDecimal.valueOf(monthsSlider.getValue()).add(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(100))));
			CustomLabel cl = new CustomLabel(BODY2.getName());
			cl.setText(cbd.toString());
			monthly.setValue(cl);
			invalidate();
		}

		private static class NewCreditStyle{
			public static float getSliderWidth() {
				return switch (GraphicConfig.getResolution().SIZE) {
					case SMALL -> 800f;
					case MEDIUM -> 900f;
					case LARGE -> 1100f;
				};
			}
			public static float getValueTagWidth() {
				return switch (GraphicConfig.getResolution().SIZE) {
					case SMALL -> 600f;
					case MEDIUM -> 700f;
					case LARGE -> 800f;
				};
			}
			public static float getSmallPadding(){
				return 10f;
			}
			public static float getBigPadding(){
				return 20f;
			}
		}
	}


	private static class CreditTable extends WrapperTable{

		public CreditTable() {
			super();
			CustomTable<Credit> creditTable = new CustomTable.CustomTableBuilder<Credit>()
							.addColumn(new LanguageString("creditTable.column.date"), this::createDate, 4)
							.addColumn(new LanguageString("creditTable.column.monthly"), this::createMonthly, 4)
							.addColumn(new LanguageString("creditTable.column.payall"), this::createPayAll, 4)
							.build();

			engine.hotelHandler.bankAccountHandler.getCurrentCredits().keySet().forEach(credit -> creditTable.addRowWithRemove(credit, () -> {
				engine.hotelHandler.bankAccountHandler.payEntireCredit(credit);

			}));


			innerTable.add(creditTable).grow();

		}
		private Actor createMonthly(Credit credit) {
			return new CustomLabel(BUTTON1.getWhiteVariantName());
		}

		private Actor createDate(Credit credit) {
			return new CustomLabel(BUTTON1.getWhiteVariantName());
		}

		private Actor createPayAll(Credit credit) {
			CustomLabel cl = new CustomLabel(BUTTON1.getWhiteVariantName());
			cl.makeItLink(() -> {
				System.out.println("test");
			});
			return cl;
		}
	}

}
