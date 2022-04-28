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
    private Launcher game;
    //View point
    private Camera camera;
    private Viewport viewport;
    //Image gather and Display
    private TextureAtlas textureAtlas;
    private SpriteBatch batch;
    //display the background

    private Stage stage;
    private BitmapFont font;
    private Skin skin,skin1;
    private Table table,table1;
    private TextButton back;
    private TextButton.TextButtonStyle textButtonStyle;

    private String volumetext;
    private Slider slider;
    private TextureRegion background;

    private static GlyphLayout glyphLayout= new GlyphLayout();;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    SettingsScreen(Launcher mg){
        game = mg;
        skin = new Skin();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT,camera);
        textureAtlas = new TextureAtlas("images.atlas");
        background = textureAtlas.findRegion("background1");

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.2f,0.2f);

        skin = new Skin();
        skin.addRegions(textureAtlas);

        skin1 = new Skin(Gdx.files.internal("clean-crispy-ui.json"));

        stage = new Stage(viewport);

        table = new Table(skin);
        table1 = new Table(skin1);

        table.setBounds(0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);
        table1.setBounds(1,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);

        volumetext = "Volume";

        slider = new Slider(0,100,0.1f,false,skin1);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if( slider.isDragging())
                {
                    System.out.println(Launcher.music.getVolume());
                    Launcher.music.setVolume(slider.getValue()/100f);
                }
            }
        });

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Sprite-0009");
        textButtonStyle.over = skin.getDrawable("Sprite-0007");

        back = new TextButton("",textButtonStyle);
        back.setTransform(true);
        back.setScale(0.5f);
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
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background,0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);
        glyphLayout.setText(font,volumetext);
        font.draw(batch,volumetext,GameScreen.WORLD_WIDTH/2 - glyphLayout.width/2,GameScreen.WORLD_HEIGHT/2+glyphLayout.height*3);
        batch.end();
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


    }
}
