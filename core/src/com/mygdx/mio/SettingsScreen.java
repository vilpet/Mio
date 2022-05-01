package com.mygdx.mio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingsScreen implements Screen {
    //Launcher class catcher
    private Launcher game;
    //View point
    private Camera camera;
    private Viewport viewport;
    //Image gather and Display
    private TextureAtlas textureAtlas,textureAtlas1;
    private SpriteBatch batch;
    //Control and font Objects
    private Stage stage;
    private BitmapFont font;
    private Skin skin,skin1;
    private Table table,table1;
    private TextButton back;
    private TextButton.TextButtonStyle textButtonStyle;
    //slider
    private String volumetext;
    private Slider slider;
    private TextureRegion background;
    //font Objects
    private static GlyphLayout glyphLayout= new GlyphLayout();;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    SettingsScreen(Launcher mg){
        //Launcher class
        game = mg;
        skin = new Skin();
        batch = new SpriteBatch();
        //Type of camera
        camera = new OrthographicCamera();
        //what we can visibly see
        viewport = new StretchViewport(GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT,camera);
        //gets images from a pack of textures
        textureAtlas = new TextureAtlas("images.atlas");
        // + https://itch.io/profile/caniaeast Cania background asset
        background = textureAtlas.findRegion("background1");
        //https://www.dafont.com/alexa.d8340 font file  Alexa
        //Allows generators from the files + takes its parameters and then generators a font with those paramamteters
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);
        //scales the font and renders without spaces
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.2f,0.2f);

        skin = new Skin();
        skin.addRegions(textureAtlas);
        //https://github.com/czyzby/gdx-skins/tree/master/default
        skin1 = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(viewport);

        table = new Table(skin);
        table1 = new Table(skin1);


        table.setBounds(0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);
        table1.setBounds(1,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);

        volumetext = "Volume";
        //slider for the volume to change and adjust
        slider = new Slider(0,100,0.1f,false,skin1);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if( slider.isDragging())
                {
                    Launcher.music.setVolume(slider.getValue()/100f);
                }
            }
        });

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        //https://itch.io/profile/totuslotus texture assets
        textButtonStyle.up = skin.getDrawable("Sprite-0009");
        textButtonStyle.over = skin.getDrawable("Sprite-0007");
        // text button to go back
        //Allows the  rescale the button to a desired size
        //Initalises the button with what type of button style they want
        back = new TextButton("",textButtonStyle);
        back.setTransform(true);
        back.setScale(0.5f);
        //Listener(checks every second if the button is pressed if so change to different state or screen)
        back.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MainMenu(game));

            }
        });
        table.add(slider);
        table.setTransform(true);
        table.setPosition(GameScreen.WORLD_WIDTH/2-7,GameScreen.WORLD_HEIGHT/2-12);
        table.setScale(0.2f);
        table1.add(back);
        table1.left().top();
        stage.addActor(table);
        stage.addActor(table1);



    }
    @Override
    public void show() {
        //detects input
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        //renders the settings background and slider
        batch.begin();
        batch.draw(background,0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);
        glyphLayout.setText(font,volumetext);
        font.draw(batch,volumetext,GameScreen.WORLD_WIDTH/2 - glyphLayout.width/2,GameScreen.WORLD_HEIGHT/2+glyphLayout.height*3);
        batch.end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        //resizes the game to fit the screen
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


    }
}
