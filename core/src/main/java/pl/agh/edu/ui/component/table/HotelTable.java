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
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class HotelTable extends WrapperTable {
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
    private final Table leftTable = new Table();
    private final Table rightTable = new Table();
    private final GameSkin skin = GameSkin.getInstance();
    private final Drawable background = new NinePatchDrawable(skin.getPatch("modal-glass-background"));

    public HotelTable() {
        createFrameTable();
        setResolutionChangeHandler(this::createFrameTable);
    }

    private void createFrameTable() {
        innerTable.clearChildren();
        leftTable.clear();
        rightTable.clear();
        createLeftTable();
        createRightTable();
        innerTable.pad(10);
        innerTable.add(leftTable).width(HotelTableStyles.getLeftTableWidth()).height(HotelTableStyles.getTablesHeight()).padRight(30f);
        innerTable.add(rightTable).width(HotelTableStyles.getRightTableWidth()).height(HotelTableStyles.getTablesHeight());
    }


    public void doSth(){

    }

    public void createLeftTable(){
        leftTable.setBackground(background);
        leftTable.pad(50f);

        // from engine scenario
        Image scenarioImage = new Image(skin.getDrawable("resort-icon"));
        TextField hotelName = new HotelNameTextField(skin, "hotel_frame_medium");
        hotelName.setAlignment(Align.center);

        leftTable.add(scenarioImage).grow().row();
        leftTable.add(hotelName).width(450f).padTop(30f).growX();

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
        scenario.pad(20f, 0f, 20f, 0f);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.scenario.label"),SkinFont.H4.getName());
        String hotelType = game.engine.hotelScenariosManager.hotelType.toString();
        Label value = new Label(hotelType, skin, SkinFont.H4.getName());

        scenario.add(title).expandX();
        scenario.add(value).expandX();
        return scenario;
    }

    private Table createCheckInRow(){
        Table checkIn = new Table();
        checkIn.setBackground(background);
        checkIn.pad(20f, 0f, 20f, 0f);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.checkIn.label"),SkinFont.H4.getName());
        title.setWrap(true);
        title.setAlignment(Align.center);
        TextField time = new TimeTextField("in", skin, "hotel_frame_medium");
        time.setAlignment(Align.center);

        checkIn.add(title).width(250f).expandX();
        checkIn.add(time).width(200f).right().padRight(40f).expandX();
        return checkIn;
    }

    private Table createCheckOutRow(){
        Table checkOut = new Table();
        checkOut.setBackground(background);
        checkOut.pad(20f, 0f, 20f, 0f);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.checkOut.label"),SkinFont.H4.getName());
        title.setWrap(true);
        title.setAlignment(Align.center);
        // set value from engine
        TextField time = new TimeTextField("out", skin, "hotel_frame_medium");
        time.setAlignment(Align.center);

        checkOut.add(title).width(300f).expandX();
        checkOut.add(time).width(200f).right().padRight(40f).expandX();
        return checkOut;
    }

    private Table createWorkersOpinionTable(){
        Table workersOpinion = new Table();

        workersOpinion.add(createWorkersTable()).width(HotelTableStyles.getWorkersOpinionsWidth()).height(HotelTableStyles.getWorkersOpinionsHeight()).expandX().fillX().align(Align.left);
        workersOpinion.add(createOpinionTable()).width(HotelTableStyles.getWorkersOpinionsWidth()).height(HotelTableStyles.getWorkersOpinionsHeight()).expandX().fillX().align(Align.right);
        return workersOpinion;
    }

    private Table createWorkersTable() {
        Table workersTable = new Table();

        workersTable.setBackground(background);
        workersTable.pad(20f);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.workers.label"),SkinFont.H4.getName());
        Image photo = new Image(skin.getDrawable("default"));
        String employees = String.valueOf(game.engine.hotelHandler.employeeHandler.getEmployees().size());
        Label value = new Label(employees, skin, SkinFont.H4.getName());

        workersTable.add(title).colspan(2).row();
        workersTable.add(photo);
        workersTable.add(value);
        return workersTable;
    }

    private Table createOpinionTable() {
        Table opinion = new Table();

        opinion.align(Align.center);
        opinion.setBackground(background);
        opinion.pad(20f);

        LanguageLabel title = new LanguageLabel(new LanguageString("hotelFrame.opinions.label"),SkinFont.H4.getName());
//        // set rating and value from engine
//        Rating rating = new Rating(2, (15) > {4+5});
        String numOpinions = String.valueOf(OpinionHandler.opinions.size());
        Label value = new Label("(" + numOpinions + ")", skin, SkinFont.SUBTITLE2.getName());

        opinion.add(title).colspan(2).row();
//        opinion.add(rating).padTop(5f).row();
        opinion.add(value).padTop(5f);
        return opinion;
    }

    private static class HotelTableStyles{

        public static float getLeftTableWidth(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 400f;
                case MEDIUM -> 500f;
                case LARGE -> 600f;
            };
        }

        public static float getTablesHeight(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 450f;
                case MEDIUM -> 550f;
                case LARGE -> 650f;
            };
        }

        public static float getRightTableWidth(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 500f;
                case MEDIUM -> 600f;
                case LARGE -> 700f;
            };
        }

        public static float getWorkersOpinionsWidth(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 200f;
                case MEDIUM -> 300f;
                case LARGE -> 400f;
            };
        }

        public static float getWorkersOpinionsHeight(){
            return switch (GraphicConfig.getResolution().SIZE){
                case SMALL -> 120f;
                case MEDIUM -> 150f;
                case LARGE -> 200f;
            };
        }
    }
}
