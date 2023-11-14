package pl.agh.edu.ui.component.table;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class HotelTable extends WrapperTable {
    private final Table leftTable = new Table();
    private final Table rightTable = new Table();
    private final GameSkin skin = GameSkin.getInstance();
    private final Drawable background = new NinePatchDrawable(skin.getPatch("modal-glass-background"));

    public HotelTable() {
        createLeftTable();
        createRightTable();
        innerTable.pad(10);
        innerTable.add(leftTable).width(HotelTableStyles.getLeftTableWidth()).height(HotelTableStyles.getTablesHeight()).padRight(30f);
        innerTable.add(rightTable).width(HotelTableStyles.getRightTableWidth()).height(HotelTableStyles.getTablesHeight());
    }

    public void createLeftTable(){
        leftTable.setBackground(background);
        leftTable.pad(50f);

        // from engine scenario
        Image scenarioImage = new Image(skin.getDrawable("scenario-icon-water"));
        TextField hotelName = new TextField("Default name", skin);
        hotelName.setAlignment(Align.center);

        leftTable.add(scenarioImage).grow().row();
        leftTable.add(hotelName).padTop(30f).growX();

    }

    public void createRightTable(){
        rightTable.add(createScenarioRow()).growX().expandY().top().row();
        rightTable.add(createCheckInRow()).growX().expandY().row();
        rightTable.add(createCheckOutRow()).growX().expandY().row();
        rightTable.add(createWorkersOpinionTable()).growX().expandY().bottom().row();
    }

    private Table createScenarioRow(){
        Table scenario = new Table();
        scenario.setBackground(background);
        scenario.pad(20f, 0f, 20f, 0f);

        Label title = new Label("Scenario", skin, SkinFont.H4.getName());
        // set value from engine
        Label value = new Label("Resort", skin, SkinFont.H4.getName());

        scenario.add(title).expandX();
        scenario.add(value).expandX();
        return scenario;
    }

    private Table createCheckInRow(){
        Table checkIn = new Table();
        checkIn.setBackground(background);
        checkIn.pad(20f, 0f, 20f, 0f);

        Label title = new Label("Check-in time", skin, SkinFont.H4.getName());
        title.setWrap(true);
        title.setAlignment(Align.center);
        // set value from engine
        TextField time = new TextField("13:00", skin);
        time.setAlignment(Align.center);

        checkIn.add(title).width(250f).expandX();
        checkIn.add(time).expandX();
        return checkIn;
    }

    private Table createCheckOutRow(){
        Table checkOut = new Table();
        checkOut.setBackground(background);
        checkOut.pad(20f, 0f, 20f, 0f);

        Label title = new Label("Check-out time", skin, SkinFont.H4.getName());
        title.setWrap(true);
        title.setAlignment(Align.center);
        // set value from engine
        TextField time = new TextField("11:00", skin);
        time.setAlignment(Align.center);

        checkOut.add(title).width(250f).expandX();
        checkOut.add(time).expandX();
        return checkOut;
    }

    private Table createWorkersOpinionTable(){
        Table workersOpinion = new Table();

        workersOpinion.add(createWorkersTable()).width(250f).height(150f).expandX().fillX().align(Align.left);
        workersOpinion.add(createOpinionTable()).width(250f).height(150f).expandX().fillX().align(Align.right);
        return workersOpinion;
    }

    private Table createWorkersTable() {
        Table workersTable = new Table();

        workersTable.setBackground(background);
        workersTable.pad(20f);

        Label title = new Label("Workers", skin, SkinFont.H4.getName());
        Image photo = new Image(skin.getDrawable("default"));
        // set value from engine
        Label value = new Label("10", skin, SkinFont.H4.getName());

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

        Label title = new Label("Opinions", skin, SkinFont.H4.getName());
        // set rating and value from engine
        Rating rating = new Rating(2);
        Label value = new Label("(100)", skin, SkinFont.SUBTITLE2.getName());

        opinion.add(title).colspan(2).row();
        opinion.add(rating).row();
        opinion.add(value);
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
    }
}
