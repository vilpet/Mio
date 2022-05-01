package com.mygdx.mio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CreditScreen implements Screen {
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
    private BitmapFont font;
    private Skin skin;
    private Table table;
    private TextButton back;
    private TextButton.TextButtonStyle textButtonStyle;
    //Credits for the string
    private String credits;
    // Layout for strings + font generation
    private static GlyphLayout glyphLayout = new GlyphLayout();
    ;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    CreditScreen(Launcher mg) {
        //Launcher catcher + Camera and viewport + image atlast identifier + how many backgrounds
        game = mg;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT, camera);
        textureAtlas = new TextureAtlas("images.atlas");
        background = new TextureRegion[1];
        background[0] = textureAtlas.findRegion("background3");
        //Initalises the image / gather objects / render
        batch = new SpriteBatch();
        stage = new Stage(viewport);
        skin = new Skin();
        table = new Table(skin);
        //table bounds
        table.setBounds(1, 0, GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT);
        //Allows generators from the files + takes its parameters and then generators a font with those paramamteters
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);
        font.setUseIntegerPositions(false);
        skin.addRegions(textureAtlas);
        //https://itch.io/profile/totuslotus texture assets
        //https://stackoverflow.com/questions/21488311/how-to-create-a-button-in-libgdx - Kévin Berthommier code
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Sprite-0009");
        textButtonStyle.over = skin.getDrawable("Sprite-0007");
        //Allows the  rescale the button to a desired size
        //Initalises the button with what type of button style they want
        font.getData().setScale(0.2f, 0.2f);
        back = new TextButton("", textButtonStyle);
        back.setTransform(true);
        back.setScale(0.5f);
        back.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));

            }
        });
        // adds the buttons to the table
        table.add(back);
        table.left().top();
        stage.addActor(table);
        //credits
        credits = "Brandon Grasley - Core Game\n"
                + "Ron Gelinas - Fulfilled - Music\n"
                + "LateNightCoffee - Clouds Assets\n"
                + "ALEXATYPE - Font Style Asset\n"
                + "TotusLotus - Button Assets\n"
                + "Cania - Background Assets\n"
                + "BennX - 1st Game Solution\n"
                + "Tenfour04 - 1st Font Solution\n"
                + "Netero - 2nd Font Solution\n"
                + "Joshflux - 1st Button Solution\n"
                + "Kévin Berthommier/vps11/ - \n3rd Font Solution\n "
                + "Raymond Buckley - Skin asset\n"
                + "Tenfour04 - 4th Font solution\n"
                + "danielz - Button / font solution\n"
                + "useof.org - Slider ";


    }

    @Override
    public void show() {
        // Allows the screen to detect input from the buttons
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        //renders the credits
        batch.begin();
        batch.draw(background[0], 0, 0, GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT);
        glyphLayout.setText(font, credits);
        font.draw(batch, credits, GameScreen.WORLD_WIDTH / 2 - glyphLayout.width / 2, GameScreen.WORLD_HEIGHT / 2 + glyphLayout.height);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //Resizes to width and height of screen
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
        textureAtlas.dispose();
        font.dispose();
        stage.dispose();


    }
}
