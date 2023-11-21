package pl.agh.edu.ui.component.table;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.textField.HotelNameTextField;
import pl.agh.edu.ui.component.textField.TimeTextField;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

import java.util.OptionalDouble;

public class HotelTable extends WrapperTable {
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
    private final Table leftTable = new Table();
    private final Table rightTable = new Table();
    private final GameSkin skin = GameSkin.getInstance();
    private final Drawable background = new NinePatchDrawable(skin.getPatch("modal-glass-background"));
    public Rating rating;

    public HotelTable() {
        createRating();
        createFrameTable();
        setResolutionChangeHandler(this::createFrameTable);
    }

    private void createFrameTable() {
        innerTable.top();
        innerTable.clearChildren();
        leftTable.clear();
        rightTable.clear();
        createLeftTable();
        createRightTable();
        innerTable.pad(10f);
        innerTable.add(leftTable).width(HotelTableStyles.getLeftTableWidth()).height(HotelTableStyles.getTablesHeight()).padRight(30f);
        innerTable.add(rightTable).width(HotelTableStyles.getRightTableWidth()).height(HotelTableStyles.getTablesHeight());
    }

    public void createLeftTable(){
        leftTable.setBackground(background);
        leftTable.pad(HotelTableStyles.getLeftTablePad());

        Image scenarioImage = new Image(skin.getDrawable("resort-icon"));
        TextField hotelName = new HotelNameTextField(skin, HotelTableStyles.getTextFieldStyle());

        leftTable.add(scenarioImage).grow().row();
        leftTable.add(hotelName).padTop(30f).growX();

    }

    public void createRightTable(){
        rightTable.add(createScenarioRow()).growX().expandY().top().row();
        rightTable.add(createCheckInRow()).growX().expandY().row();
        rightTable.add(createCheckOutRow()).growX().expandY().row();
        rightTable.add(createWorkersOpinionTable()).growX().expandY().bottom();
    }

    private Table createScenarioRow(){
        Table scenario = new Table();
        scenario.setBackground(background);
        scenario.pad(HotelTableStyles.VERTICAL_PAD, HotelTableStyles.HORIZONTAL_PAD, HotelTableStyles.VERTICAL_PAD, HotelTableStyles.HORIZONTAL_PAD);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.scenario.label"),HotelTableStyles.getLabelsStyle());
        String hotelType = game.engine.hotelScenariosManager.hotelType.toString();
        Label value = new Label(hotelType, skin, HotelTableStyles.getLabelsStyle());

        scenario.add(title).expandX();
        scenario.add(value).expandX();
        return scenario;
    }

    private Table createCheckInRow(){
        Table checkIn = new Table();
        checkIn.setBackground(background);
        checkIn.pad(0f);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.checkIn.label"),HotelTableStyles.getLabelsStyle());
        title.setWrap(true);
        title.setAlignment(Align.center);
        TextField time = new TimeTextField("in", skin, HotelTableStyles.getTextFieldStyle());
        time.setAlignment(Align.center);

        checkIn.add(title).width(HotelTableStyles.getChecksTitleWidth()).expandX();
        checkIn.add(time).width(HotelTableStyles.getTimeWidth()).right().padRight(40f).expandX();
        return checkIn;
    }

    private Table createCheckOutRow(){
        Table checkOut = new Table();
        checkOut.setBackground(background);
        checkOut.pad(0f);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.checkOut.label"),HotelTableStyles.getLabelsStyle());
        title.setWrap(true);
        title.setAlignment(Align.center);
        TextField time = new TimeTextField("out", skin, HotelTableStyles.getTextFieldStyle());
        time.setAlignment(Align.center);

        checkOut.add(title).width(HotelTableStyles.getChecksTitleWidth()).expandX();
        checkOut.add(time).width(HotelTableStyles.getTimeWidth()).right().padRight(40f).expandX();
        return checkOut;
    }

    private Table createWorkersOpinionTable(){
        Table workersOpinion = new Table();

        workersOpinion.add(createWorkersTable()).growX().uniform().fill();
        workersOpinion.add(createOpinionTable()).growX().uniform().expandY();
        return workersOpinion;
    }

    private Table createWorkersTable() {
        Table workersTable = new Table();

        workersTable.setBackground(background);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.workers.label"),HotelTableStyles.getLabelsStyle());
        Image photo = new Image(skin.getDrawable("default"));
        String employees = String.valueOf(game.engine.hotelHandler.employeeHandler.getEmployees().size());
        Label value = new Label(employees, skin, HotelTableStyles.getLabelsStyle());

        workersTable.add(title).colspan(2).row();
        workersTable.add(photo);
        workersTable.add(value);
        return workersTable;
    }

    private Table createOpinionTable() {
        Table opinion = new Table();

        opinion.align(Align.center);
        opinion.setBackground(background);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.opinions.label"),HotelTableStyles.getLabelsStyle());

        opinion.add(title).row();
        opinion.add(rating).grow();
        return opinion;
    }

    private void createRating() {
        OptionalDouble ratingValue = OpinionHandler.getAvgRating();
        if(ratingValue.isPresent())
            this.rating = new Rating((int) ratingValue.getAsDouble());
        else{
            this.rating = new Rating(0);
        }
    }

    private static class HotelTableStyles{
        public static float HORIZONTAL_PAD = 0f;
        public static float VERTICAL_PAD = 0f;

        public static float getLeftTableWidth(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 350f;
                case MEDIUM -> 500f;
                case LARGE -> 700f;
            };
        }

        public static float getTablesHeight(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 350f;
                case MEDIUM -> 550f;
                case LARGE -> 700f;
            };
        }

        public static float getRightTableWidth(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 500f;
                case MEDIUM -> 600f;
                case LARGE -> 700f;
            };
        }

        public static float getChecksTitleWidth(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 200f;
                case MEDIUM -> 300f;
                case LARGE -> 400f;
            };
        }
        public static float getTimeWidth(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 150f;
                case MEDIUM -> 200f;
                case LARGE -> 250f;
            };
        }

        public static float getLeftTablePad(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 20f;
                case MEDIUM -> 50f;
                case LARGE -> 90f;
            };
        }

        public static String getTextFieldStyle(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> "hotel_frame_small";
                case MEDIUM -> "hotel_frame_medium";
                case LARGE -> "hotel_frame_large";
            };
        }

        public static String getLabelsStyle(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> SkinFont.SUBTITLE1.getName();
                case MEDIUM -> SkinFont.H4.getName();
                case LARGE -> SkinFont.H3.getName();
            };
        }
    }
}
