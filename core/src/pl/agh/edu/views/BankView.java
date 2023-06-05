package pl.agh.edu.views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pl.agh.edu.model.bank.Bank;
import pl.agh.edu.model.bank.Loan;

import java.math.BigDecimal;

public class BankView extends View{

    private Label loanValue;
    private Label loanPeriod;
    private Table loans = new Table();


    public BankView(Table root, Skin skin){
        super(skin,root);
        loanValue = new Label("0",skin);
        loanPeriod = new Label("0",skin);


        Bank bank = Bank.getInstance();
        this.add(new Label("Account balance: "+bank.printBalance(),skin)).space(10);
        this.row();
        Slider loanValueSlider = new Slider(0, 100, 1, false, skin);
        loanValueSlider.setValue(0);
        this.add(new Label("Loan value",skin)).space(10);
        this.add(loanValueSlider).space(10);
        this.add(loanValue).space(10);
        this.row();

        Slider loanPeriodSlider = new Slider(0, 100, 1, false, skin);
        loanPeriodSlider.setValue(0);
        this.add(new Label("Loan period in months",skin)).space(10);
        this.add(loanPeriodSlider).space(10);
        this.add(loanPeriod).space(10);
        loanValueSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Slider loanValueSlider = (Slider) actor;
                loanValue.setText(String.valueOf(loanValueSlider.getValue()));

            }
        });
        loanPeriodSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Slider loanPeriodSlider = (Slider) actor;
                loanPeriod.setText(String.valueOf(loanPeriodSlider.getValue()));
            }
        });
        this.row();
        TextButton obtainLoan = new TextButton("obtain loan",skin);
        this.add(obtainLoan).center();
        obtainLoan.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    Loan loan = bank.obtainLoan(new BigDecimal(loanValue.getText().toString()), (int) Math.round(Double.valueOf(loanPeriod.getText().toString())));
                    Table loanRow = new Table();
                    addLoanToRow(loanRow, loan);
                    loans.row();
                    loans.add(loanRow);
                }
                catch(ArithmeticException e){
                    System.out.println("don't divide by zero dummy");
                }

            }
        });
        loans.columnDefaults(4);
        loans.add(new Label("Paid / To be paid",skin)).space(10);
        loans.add(new Label("Monthly payments",skin)).space(10);
        loans.add(new Label("Months left",skin)).space(10);
        loans.add(new Label("Pay",skin)).space(10);

        this.row();
        this.add(loans);
    }

    private void addLoanToRow(Table loanRow,Loan loan){
        loanRow.add(new Label(String.valueOf(loan.getPaidValue())+" / "+String.valueOf(loan.getLoanValue()),skin)).space(10);
        loanRow.add(new Label(String.valueOf(loan.getMonthlyPayments()),skin)).space(10);
        loanRow.add(new Label(String.valueOf(loan.getMonthsLeft()),skin)).space(10);
        TextButton payAll = new TextButton("Pay all",skin );
        loanRow.add(payAll).space(10);
    }
}
