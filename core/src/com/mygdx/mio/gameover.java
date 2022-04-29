package com.mygdx.mio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

public class gameover implements Screen {
    private Launcher game;
    public SpriteBatch batch;
    private Camera camera;
    private Viewport viewport;

    private TextureRegion background1,background2;
    private TextureAtlas textureAtlas;


    private TextButton retrybutton,settingsbutton;
    private Stage stage;
    private Table table;
    private Skin skin;
    private TextButton.TextButtonStyle textButtonStyle,textButtonStyle1;

    private BitmapFont font;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    float hudsectionwidth;
    gameover(Launcher mg)
    {
        game = mg;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT,camera);
        textureAtlas = new TextureAtlas("images.atlas");

        batch = new SpriteBatch();
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);

        font.getData().setScale(0.2f);
        font.setUseIntegerPositions(false);

        background1 = textureAtlas.findRegion("Sprite-0011");
        background2 = textureAtlas.findRegion("background1");

        stage = new Stage(viewport);
        skin = new Skin();
        table = new Table(skin);

        skin.addRegions(textureAtlas);
        table.setBounds(4.5f,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Sprite-0014");
        textButtonStyle.down = skin.getDrawable("Sprite-0015");

        textButtonStyle1 = new TextButton.TextButtonStyle();
        textButtonStyle1.font = font;
        textButtonStyle1.up = skin.getDrawable("Sprite-0010");
        textButtonStyle1.down = skin.getDrawable("Sprite-0008");

        retrybutton = new TextButton("",textButtonStyle);
        retrybutton.setTransform(true);
        retrybutton.setScale(0.5f);
        settingsbutton = new TextButton("",textButtonStyle1);
        settingsbutton.setTransform(true);
        settingsbutton.setScale(0.5f);

        table.add(retrybutton);
        table.add(settingsbutton);

        stage.addActor(table);

        hudsectionwidth = GameScreen.WORLD_WIDTH / 3;

        settingsbutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));

            }
        });
        retrybutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });





    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background2,0,0,GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT);
        batch.draw(background1,GameScreen.WORLD_WIDTH/6,GameScreen.WORLD_HEIGHT/3,50,50);
        font.draw(batch,"Gameover", GameScreen.WORLD_WIDTH /3,GameScreen.WORLD_HEIGHT*2/3,hudsectionwidth, Align.center,false);
        font.draw(batch,"Score",GameScreen.WORLD_WIDTH/3,(GameScreen.WORLD_HEIGHT*2/3)-5,hudsectionwidth, Align.center,false);
        font.draw(batch,String.format(Locale.getDefault(),"%06d",GameScreen.score),GameScreen.WORLD_WIDTH/3, (float) (GameScreen.WORLD_HEIGHT*2/3)-10,hudsectionwidth,Align.center,false);
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
