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
    private Launcher game;
    //View point
    private Camera camera;
    private Viewport viewport;
    //Image gather and Display
    private TextureAtlas textureAtlas;
    private SpriteBatch batch;
    //display the background
    private TextureRegion[] background;

    private Stage stage;
    private TextButton settingsbutton,playbutton,creditbutton,exitbutton;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Skin skin;
    private Table table;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;




    public MainMenu(Launcher mg)
    {

        game = mg;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT,camera);
        textureAtlas = new TextureAtlas("images.atlas");
        background = new TextureRegion[1];
        // number of background + what the backgrounds location is base on the atlas
        background[0] = textureAtlas.findRegion("background1");

        batch = new SpriteBatch();
        stage = new Stage(viewport);

        skin = new Skin();
        table = new Table(skin);



        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);
        skin.addRegions(textureAtlas);

        table.setBounds(0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.overFontColor = Color.SKY;
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.3f);

        playbutton = new TextButton("Play", textButtonStyle);
        playbutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(game));

            }
        });

        settingsbutton = new TextButton("Settings", textButtonStyle);
        settingsbutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(game.settingscreen);

            }
        });

        creditbutton = new TextButton("Credits",textButtonStyle);
        creditbutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(game.creditScreen);

            }
        });

        exitbutton = new TextButton("Exit",textButtonStyle);

        exitbutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Gdx.app.exit();
            }
        });



        table.add(playbutton).padBottom(2);
        table.row();
        table.add(settingsbutton).padBottom(2);
        table.row();
        table.add(creditbutton).padBottom(2);
        table.row();
        table.add(exitbutton).padBottom(2);;
        stage.addActor(table);



    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);




    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background[0],0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT );
        batch.end();
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true);
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
