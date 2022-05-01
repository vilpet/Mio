package com.mygdx.mio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;

public class MainMenu implements Screen {
    //Launcher class catcher
    private Launcher game;
    //View point
    private Camera camera;
    private Viewport viewport;
    //Image gather and Display
    private TextureAtlas textureAtlas;
    private SpriteBatch batch;
    //display the background
    private TextureRegion[] background;
    //Control and font Objects
    private Stage stage;
    private TextButton settingsbutton, playbutton, creditbutton, exitbutton;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Skin skin;
    private Table table;
    //font Objects
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    public MainMenu(Launcher mg) {
        //Launcher class
        game = mg;
        //Type of camera
        camera = new OrthographicCamera();
        //what we can visibly see
        viewport = new StretchViewport(GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT, camera);
        //gets images from a pack of textures
        textureAtlas = new TextureAtlas("images.atlas");
        //how many backgrounds
        background = new TextureRegion[1];
        // number of background + what the backgrounds location is base on the atlas + https://itch.io/profile/caniaeast Cania background asset
        background[0] = textureAtlas.findRegion("background1");
        //Initalises the image / gather objects / Viewport
        batch = new SpriteBatch();
        stage = new Stage(viewport);
        skin = new Skin();
        table = new Table(skin);
        //Allows generators from the files + takes its parameters and then generators a font with those paramamteters - //https://www.dafont.com/alexa.d8340 font file
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);
        //sets the position of table objects and add texturatlase as one of the skins assets collections
        skin.addRegions(textureAtlas);
        table.setBounds(0, 0, GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        //changes the colour when hovering over.
        textButtonStyle.overFontColor = Color.SKY;
        //makes the textures normal when scalled.
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.3f);
        //buttons
        //Listener(checks every second if the button is pressed if so change to different state or screen)
        playbutton = new TextButton("Play", textButtonStyle);
        playbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(game));

            }
        });
        settingsbutton = new TextButton("Settings", textButtonStyle);
        settingsbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(game.settingscreen);
            }
        });
        creditbutton = new TextButton("Credits", textButtonStyle);
        creditbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(game.creditScreen);

            }
        });
        exitbutton = new TextButton("Exit", textButtonStyle);

        exitbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Gdx.app.exit();
            }
        });
        //adds buttons to the table
        table.add(playbutton).padBottom(2);
        table.row();
        table.add(settingsbutton).padBottom(2);
        table.row();
        table.add(creditbutton).padBottom(2);
        table.row();
        table.add(exitbutton).padBottom(2);
        stage.addActor(table);

    }
    @Override
    public void show() {
        //detects input
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        //renders the Mainmenu
        batch.begin();
        batch.draw(background[0], 0, 0, GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT);
        batch.end();
        stage.act(delta);
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
        //resizes the game to fit the screen
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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
        batch.dispose();
    }
}
