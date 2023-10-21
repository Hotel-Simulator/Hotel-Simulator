package pl.agh.edu.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import pl.agh.edu.GdxGame;
import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.component.button.DifficultyButton;
import pl.agh.edu.actor.component.button.ScenarioButton;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.enums.HotelType;

import java.util.ArrayList;

public class ScenariosScreen implements Screen {
    private final Stage stage = GraphicConfig.stage;
    private final Skin skin = GameSkin.getInstance();
    private final Stack stack = new Stack();

    private final Table mainTable = new Table();
    private final Table difficultyTable = new Table();
    private final Table scenariosTable = new Table();
    private final GdxGame game;

    NinePatchDrawable buttonBackground = new NinePatchDrawable(skin.getPatch("button"));
    NinePatchDrawable onSelect = new NinePatchDrawable(skin.getPatch("button_selected"));
    private Label.LabelStyle titleLabel;

    // for difficulty
    public ArrayList<DifficultyButton> myButtons = new ArrayList<>();
    public int width = GraphicConfig.getResolution().WIDTH;
    public int height = GraphicConfig.getResolution().HEIGHT;
    public DifficultyButton currentlySelected = null;
    public int diffWidth = width/7;
    public int diffHeight = height/7;
    public String difficultyButtonFont;
    public String titleFont;
    public String backFont;
    public String scenarioTitleFont;
    public float largePaddingMultiplier = 1;

    // for scenarios
    public ArrayList<ScenarioButton> scenarios = new ArrayList<>();
    public ScenarioButton selected = null;
    public Label errorLabel;
    public boolean errorDisplayed = false;


    public ScenariosScreen(GdxGame game) {
        this.game = game;
        Image background = new Image(skin.getDrawable("hotel-room"));
        background.setScaling(Scaling.stretch);
        background.setAlign(Align.center);
        stack.setFillParent(true);
        stack.add(background);
        stage.addActor(stack);
        setParams();

        createDifficultyFrame();
        createScenariosFrame();

        setFillParent(mainTable);

        stage.addActor(mainTable);
        mainTable.add();
        mainTable.add(scenariosTable).growX();

//        mainTable.debug();
    }

    public void createDifficultyFrame(){
        difficultyTable.left();
        setFillParent(difficultyTable);

        Label label1 = new Label("Choose difficulty",titleLabel);

        Table titleTable = new Table();
        titleTable.setBackground(buttonBackground);
        titleTable.add(label1);
        titleTable.pad(label1.getHeight()/4 ,label1.getWidth()/12, label1.getHeight()/4, label1.getWidth()/12);

        difficultyTable.add(titleTable).left().padLeft((int)(width/12)).padTop((int)(largePaddingMultiplier*height/12)).expandX();

        createDifficultyButtons(buttonBackground, onSelect);
        addListeners(buttonBackground, onSelect);
        difficultyTable.row();
        difficultyTable.row();

        Button back = new TextButton("Back", createButtonStyle(buttonBackground, onSelect, skin, backFont));
        back.pad(back.getHeight()/4, back.getWidth()/6, back.getHeight()/4, back.getWidth()/6);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainTable.clearChildren();
                mainTable.add(scenariosTable).growX();
            }
        });

        Table playBack = new Table();
        playBack.add(back).padRight(3*width/5);

        Button play = new TextButton("Play", createButtonStyle(buttonBackground, onSelect, skin, backFont));
        play.pad(play.getHeight()/4, play.getWidth()/6, play.getHeight()/4, play.getWidth()/6);
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(selected != null && currentlySelected != null){
                    game.setScenariosAdapterData();
                    game.changeScreen(new MainScreen(game));
                }
                else if(!errorDisplayed){
                    errorLabel = createErrorLabel();
                    difficultyTable.add(errorLabel);
                }

            }
        });

        playBack.add(play);
        difficultyTable.add(playBack).padTop((float) (largePaddingMultiplier*height/30));
        difficultyTable.row().padTop((float) (largePaddingMultiplier*10));

    }

    private Label.LabelStyle createTitleLabel() {
        Label.LabelStyle titleLabel = new Label.LabelStyle();
        titleLabel.font = skin.getFont(titleFont);
        titleLabel.fontColor = Color.YELLOW;
        return titleLabel;
    }

    public void createDifficultyButtons(NinePatchDrawable buttonBackground, NinePatchDrawable onSelect){
        String[] names = {"EASY", "MEDIUM", "HARD", "BRUTAL"};
        DifficultyLevel[] names2 = DifficultyLevel.values();

        for(int i=0;i < 4; i++){
            difficultyTable.row();
            DifficultyButton myButton = new DifficultyButton(names[i], createButtonStyle(buttonBackground, onSelect, skin, difficultyButtonFont), names2[i]);
            this.myButtons.add(myButton);
            myButton.pad((diffHeight-myButton.getHeight())/2,(diffWidth-myButton.getWidth())/2,(diffHeight-myButton.getHeight())/2,(diffWidth-myButton.getWidth())/2);
            difficultyTable.add(myButton).left().padTop((int)(largePaddingMultiplier*height/24)).padLeft((int)(width/6 + i*width/24));
        }
    }

    public Label createErrorLabel(){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = skin.getFont("white-h4");
        style.fontColor = Color.RED;

        return new Label("Select scenario and difficulty", style);
    }

    public TextButton.TextButtonStyle createButtonStyle(NinePatchDrawable titleBackground, NinePatchDrawable onSelect, Skin skin, String font){
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = titleBackground;
        buttonStyle.down = onSelect;
        buttonStyle.font = skin.getFont(font);
        buttonStyle.fontColor = Color.YELLOW;
        return buttonStyle;
    }

    public void addListeners(NinePatchDrawable titleBackground, NinePatchDrawable onSelect){
        for(DifficultyButton button : myButtons){
            button.addListener(
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if(currentlySelected != null){
                                currentlySelected.getStyle().up = titleBackground;
                                currentlySelected.unselect();
                            }
                            currentlySelected = button;
                            currentlySelected.select();
                            button.getStyle().up = onSelect;
                            if(selected != null && currentlySelected != null){
                                difficultyTable.removeActor(errorLabel);
                                difficultyTable.row().clearActor();
                            }
                        }
                    }
            );
        }
    }

    public void setParams(){
        switch (GraphicConfig.getResolution().SIZE) {
            case SMALL -> {
                titleFont = "white-h3";
                difficultyButtonFont = "white-subtitle1";
                backFont = "white-h4";
                scenarioTitleFont = "white-h4";
            }
            case MEDIUM -> {
                titleFont = "white-h2";
                difficultyButtonFont = "white-h4";
                backFont = "white-h2";
                scenarioTitleFont = "white-h3";
            }
            case LARGE -> {
                titleFont = "white-h1";
                difficultyButtonFont = "white-h3";
                backFont = "white-h1";
                scenarioTitleFont = "white-h2";
                diffHeight = height/12;
                diffWidth = width/8;
                largePaddingMultiplier = height/1000f * 0.85f;
                System.out.println(largePaddingMultiplier);
                System.out.println(3);
            }
        }
        titleLabel = createTitleLabel();
    }

    public void createScenariosFrame(){
        scenariosTable.top();
        setFillParent(scenariosTable);

        Label label1 = new Label("Choose scenario",titleLabel);

        Table titleTable = new Table();
        titleTable.setBackground(buttonBackground);
        titleTable.add(label1);
        titleTable.pad(label1.getHeight()/4 ,label1.getWidth()/12, label1.getHeight()/4, label1.getWidth()/12);

        scenariosTable.add(titleTable).center().padTop((int)(largePaddingMultiplier*height/10)).padBottom((int)(height/26)).expandX();

        scenariosTable.row();

        Table buttons = new Table();
        buttons.add(addScenariosButton("Seaside Resort", "water", "Most of the clients are people who are on vacation", "Summer", HotelType.RESORT, scenarioTitleFont, difficultyButtonFont));
        buttons.add(addScenariosButton("Sanatorium Ciechocinek", "hospital", "Clients are mostly patients but not only", "Autumn", HotelType.SANATORIUM, scenarioTitleFont, difficultyButtonFont)).padLeft((int)(width/12));
        buttons.add(addScenariosButton("City center Hotel", "hotel", "Hotel guests are mostly businessman", "Spring", HotelType.HOTEL, scenarioTitleFont, difficultyButtonFont)).padLeft((int)(width/12));
        scenariosTable.add(buttons).padTop((int)(largePaddingMultiplier*height/24));

        scenariosTable.row();

        TextButton.TextButtonStyle playStyle = new TextButton.TextButtonStyle();
        playStyle.up = buttonBackground;
        playStyle.down = onSelect;
        playStyle.font = skin.getFont("white-h1"); // Set the custom font
        playStyle.fontColor = Color.YELLOW; // Set the font color

        Button next = new TextButton("Next", playStyle);
        next.pad(15);
        next.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(selected != null && currentlySelected != null){
                    difficultyTable.removeActor(errorLabel);
                }
                mainTable.clearChildren();
                mainTable.add(difficultyTable).growX();
            }
        });

        scenariosTable.add(next).right().padRight((int)(width/12)).padTop((int)(largePaddingMultiplier*height/16));

//        scenariosTable.debug();
    }

    private void setFillParent(Table table) {
        table.setFillParent(true);
    }

    public ScenarioButton addScenariosButton(String title, String image, String description, String season, HotelType hotelType, String scenarioTitleFont, String scenarioTextFont){
        ScenarioButton scenario = new ScenarioButton(title, image, description, season, hotelType, scenarioTitleFont, scenarioTextFont);
        scenario.setTouchable(Touchable.enabled);
        scenario.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(scenario.getSelected()){
                    scenario.setUnselected();
                    selected = null;
                }
                else{
                    scenario.setSelected();
                    if(selected != null){
                        selected.setUnselected();
                    }
                    selected = scenario;
                }
            }
        });
        scenarios.add(scenario);

        return scenario;
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
