package com.mygdx.mio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

import jdk.internal.org.jline.utils.Log;
import sun.tools.jar.Main;

class GameScreen implements Screen {
    //Launcher class catcher
    private Launcher game;
    //Viewing angles - Brandon Grasley
    private Camera camera;
    private Viewport viewport;
    //Image gather and Display - Brandon Grasley
    public SpriteBatch batch;
    private TextureAtlas textureAtlas;
    //display the backgrounds - Brandon Grasley
    private TextureRegion[] background;
    private TextureRegion background1, background2;
    // displays thea area of the texture - Brandon Grasley
    private TextureRegion playerRegion, enemyRegion, enemyRegion1, enemyRegion2, playerlives;
    //Realtime - Brandon Grasley
    private float[] backgroundOffsets = {0, 0};
    private float backgroundMaxScrollingSpeed;
    private float timebetweenspawn[] = {1f, 2f, 3f};
    private float enemySpawnTimer[] = {0, 0, 0};
    //world parameters - Brandon Grasley
    public static int WORLD_WIDTH = 72;
    public static int WORLD_HEIGHT = 128;
    private final float MOVEMENT_THRESHHOLD = 1f;
    //game objects - Brandon Grasley
    private LinkedList<Cloud> enemycloudslist;
    //player object - Brandon Grasley
    private Player player1;
    //Score object - Brandon Grasley
    public static int score = 0;
    //Control and font Objects - https://stackoverflow.com/users/1306976/danielz - danielz
    private Stage stage, stage1;
    private BitmapFont font;
    private Skin skin, skin1;
    private Table table, table1;
    private TextButton pausebutton, playbutton, settingsbutton;
    private TextButton.TextButtonStyle textButtonStyle, textButtonStyle1, textButtonStyle2;
    //font Objects - https://stackoverflow.com/users/4015726/k%c3%a9vin-berthommier - Kévin Berthommier
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    //heads up display - Brandon Grasley
    float hudverticalmargin, hudleftx, hudrightx, hudcentrex, hudrow1, hudrow2, hudrightxx, hudrow3, hudsectionwidth, hudrow4, hudrow5;

    //Game states - https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX
    public enum State {
        PAUSE,
        RUN,
        RESUME,
        MENU
    }

    GameScreen(Launcher mg) {
        //Launcher class - Vilius Petrauskas
        game = mg;
        //Type of camera - Brandon Grasley
        camera = new OrthographicCamera();
        //what we can visibly see - Brandon Grasley
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        //gets images from a pack of textures - Brandon Grasley
        textureAtlas = new TextureAtlas("images.atlas");
        //how many backgrounds - Brandon Grasley
        background = new TextureRegion[2];
        // number of background + what the backgrounds location is base on the atlas - Brandon Grasley  + https://itch.io/profile/caniaeast Cania
        background[0] = textureAtlas.findRegion("background1");
        background[1] = textureAtlas.findRegion("background2");
        //initalises the enemy - Brandon Grasley + https://itch.io/profile/latenightcoffe LateNightCoffee
        enemyRegion = textureAtlas.findRegion("Sprite-0001");
        enemyRegion1 = textureAtlas.findRegion("Sprite-0003");
        enemyRegion2 = textureAtlas.findRegion("Sprite-0004");
        //initalises the hearts for player - Vilius Petrauskas
        playerlives = textureAtlas.findRegion("heart");
        //Initalises the image / gather objects Brandon Grasley  code+ Vilius Petrauskas code
        batch = new SpriteBatch();
        stage = new Stage(viewport);
        skin = new Skin();
        table = new Table(skin);
        table1 = new Table(skin1);
        stage1 = new Stage(viewport);
        //https://www.dafont.com/alexa.d8340 font file + https://stackoverflow.com/users/4015726/k%c3%a9vin-berthommier - Kévin Berthommier code
        //Allows generators from the files + takes its parameters and then generators a font with those paramamteters
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);
        //gathers the texture atlas - Brandon Grasley code + Vilius Petrauskas code +  https://stackoverflow.com/users/4015726/k%c3%a9vin-berthommier - Kévin Berthommier code
        //sets the position of table objects
        skin.addRegions(textureAtlas);
        table.setBounds(1, 0, WORLD_WIDTH, WORLD_HEIGHT);
        table1.setBounds(4, 0, WORLD_WIDTH, WORLD_HEIGHT);
        //https://itch.io/profile/totuslotus texture assets
        //https://stackoverflow.com/questions/21488311/how-to-create-a-button-in-libgdx - danielz code + Vilius Petrauskas
        textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle2.font = font;
        textButtonStyle2.up = skin.getDrawable("Sprite-0010");
        textButtonStyle2.down = skin.getDrawable("Sprite-0008");
        //https://stackoverflow.com/questions/21488311/how-to-create-a-button-in-libgdx - danielz code + Vilius Petrauskas
        //https://itch.io/profile/totuslotus texture assets
        textButtonStyle1 = new TextButton.TextButtonStyle();
        textButtonStyle1.font = font;
        textButtonStyle1.up = skin.getDrawable("Sprite-0009");
        textButtonStyle1.down = skin.getDrawable("Sprite-0007");
        //https://stackoverflow.com/questions/21488311/how-to-create-a-button-in-libgdx - Kdanielz code
        //https://itch.io/profile/totuslotus texture assets
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Sprite-0013");
        textButtonStyle.down = skin.getDrawable("Sprite-0012");
        //https://stackoverflow.com/questions/21488311/how-to-create-a-button-in-libgdx - danielz code
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Button.html
        //Allows the  rescale the button to a desired size
        //Initalises the button with what type of button style they want
        settingsbutton = new TextButton("", textButtonStyle2);
        settingsbutton.setTransform(true);
        settingsbutton.setScale(0.5f);
        pausebutton = new TextButton("", textButtonStyle);
        pausebutton.setTransform(true);
        pausebutton.setScale(0.5f);
        playbutton = new TextButton("", textButtonStyle1);
        playbutton.setTransform(true);
        playbutton.setScale(0.5f);
        //Listener(checks every second if the button is pressed if so change to different state or screen)
        playbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameState(State.RUN);
            }
        });

        pausebutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameState(State.PAUSE);
            }
        });
        settingsbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new MainMenu(game));
            }
        });
        //position the table where it needs to be
        table.left().top();
        // adds the buttons to the table
        table.add(pausebutton);
        table1.add(settingsbutton);
        table1.add(playbutton);

        // table is added to stage which regulates if inputs are pressed
        stage1.addActor(table1);
        stage.addActor(table);
        //enemy cloud list is initalised - Brandon Grasley
        enemycloudslist = new LinkedList<>();
        //initalises the player - - Brandon Grasley + Vilius Petrauskas
        playerRegion = textureAtlas.findRegion("Mio");
        player1 = new Player(100, WORLD_WIDTH / 2, WORLD_HEIGHT / 4, 3, 15, 15, playerRegion);
        //makes it scrollable - Brandon Grasley
        backgroundMaxScrollingSpeed = (float) (WORLD_HEIGHT) / 4;
        //to draw the sprites player / enemy - Vilius Petrauskas + //https://itch.io/profile/totuslotus texture assets +  + https://itch.io/profile/caniaeast Cania
        background1 = textureAtlas.findRegion("Sprite-0011");
        background2 = textureAtlas.findRegion("background1");
        //Hud - Brandon Grasley
        hud();


    }

    private void hud() {

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);

        font.getData().setScale(0.2f);
        //https://stackoverflow.com/questions/37968025/irregular-font-spacing-in-libgdx Tenfour04
        font.setUseIntegerPositions(false);
        //Hud - Brandon Grasley + Vilius Petrauskas
        hudverticalmargin = font.getCapHeight() / 2;
        hudleftx = hudverticalmargin;
        hudrightx = WORLD_WIDTH * 2 / 3 - hudverticalmargin;
        hudrightxx = WORLD_WIDTH - 16;

        hudcentrex = WORLD_HEIGHT - hudverticalmargin;
        hudrow1 = WORLD_HEIGHT - hudverticalmargin - font.getCapHeight();
        hudrow2 = hudrow1 - hudsectionwidth - font.getCapHeight();
        hudrow3 = (float) (WORLD_HEIGHT - WORLD_HEIGHT * 0.09);
        hudrow4 = (float) (WORLD_HEIGHT - WORLD_HEIGHT * 0.1);
        hudrow5 = (float) (WORLD_HEIGHT - WORLD_HEIGHT * 0.12);

        hudsectionwidth = WORLD_WIDTH / 3;

    }

    // https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX - game states
    State state = State.RUN;

    @Override
    public void render(float deltaTime) {
        // https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX - game states + BrandonGrasley + Vilius Petrauskas
        switch (state) {
            case RUN:

                batch.begin();
                renderBackground(deltaTime);
                inputDetection(deltaTime);
                player1.update(deltaTime);
                spawnEnemies(deltaTime);
                ListIterator<Cloud> enemyIterator = enemycloudslist.listIterator();
                while (enemyIterator.hasNext()) {
                    Cloud enemycloud = enemyIterator.next();
                    enemymovement(enemycloud, deltaTime);
                    enemycloud.update(deltaTime);
                    enemycloud.draw(batch);

                }
                player1.draw(batch);
                score += 1;
                detectCollision();
                updateandrenderscore();
                updateandrenderlives();
                rendergameover();

                batch.end();
                stage.draw();
//do suff here
                break;
            // https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX - game states
            case PAUSE:
                //Vilius Petrauskas Draws the pause screen
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                batch.begin();
                batch.draw(background2, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
                batch.draw(background1, WORLD_WIDTH / 6, WORLD_HEIGHT / 3, 50, 50);
                font.draw(batch, String.format(Locale.getDefault(), "%07d", score), WORLD_WIDTH / 3, (float) (WORLD_HEIGHT * 2 / 3) - 10, hudsectionwidth, Align.center, false);
                font.draw(batch, "Current Score:", WORLD_WIDTH / 3, (WORLD_HEIGHT * 2 / 3), hudsectionwidth, Align.center, false);
                batch.end();
                stage1.draw();

//do stuff here

                break;
            case RESUME:
                // https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX - game states
                setGameState(State.RUN);

                break;

            case MENU:
                // https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX - game states
                game.setScreen(game.menu);
                break;
            default:
                break;
        }


    }

    // https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX - game states
    public void setGameState(State s) {
        this.state = s;
    }

    //Renders the score in the top right corner - Brandon Grasley + Vilius Petrauskas
    private void updateandrenderscore() {
        font.draw(batch, "Score", hudrightx, hudrow4, hudsectionwidth, Align.right, false);
        font.draw(batch, String.format(Locale.getDefault(), "%05d", score), hudrightx, hudrow5, hudsectionwidth, Align.right, false);

    }

    // Render lives depending on the live count of player - Vilius Petrauskas
    private void updateandrenderlives() {
        font.draw(batch, "Lives", hudrightx, hudrow1, hudsectionwidth, Align.right, false);
        if (player1.lives == 3) {
            batch.draw(playerlives, hudrightxx, hudrow3, 5.4f, 4.5f);
            batch.draw(playerlives, hudrightxx + 5, hudrow3, 5.4f, 4.5f);
            batch.draw(playerlives, hudrightxx + 10, hudrow3, 5.4f, 4.5f);
        } else if (player1.lives == 2) {
            batch.draw(playerlives, hudrightxx, hudrow3, 5.4f, 4.5f);
            batch.draw(playerlives, hudrightxx + 5, hudrow3, 5.4f, 4.5f);
        } else if (player1.lives == 1) {
            batch.draw(playerlives, hudrightxx, hudrow3, 5.4f, 4.5f);
        }
    }

    //renders game over - Vilius Petrauskas
    private void rendergameover() {
        if (player1.lives <= 0) {
            game.setScreen(game.Gameover);

        }
    }

    //spawns enemy clouds in intervals - Brandom Grasley
    private void spawnEnemies(float deltaTime) {
        enemySpawnTimer[0] += deltaTime;
        enemySpawnTimer[1] += deltaTime;
        enemySpawnTimer[2] += deltaTime;

        if (enemySpawnTimer[0] > timebetweenspawn[0]) {
            enemycloudslist.add(new Cloud(20, Launcher.random.nextFloat() * (WORLD_WIDTH - 1) + 5, WORLD_HEIGHT + 20, 15, 15, enemyRegion));

            enemySpawnTimer[0] -= timebetweenspawn[0];
        }
        if (enemySpawnTimer[1] > timebetweenspawn[1]) {
            enemycloudslist.add(new Cloud(20, Launcher.random.nextFloat() * (WORLD_WIDTH - 1) + 5, WORLD_HEIGHT + 20, 15, 15, enemyRegion1));

            enemySpawnTimer[1] -= timebetweenspawn[1];
        }
        if (enemySpawnTimer[2] > timebetweenspawn[2]) {
            enemycloudslist.add(new Cloud(20, Launcher.random.nextFloat() * (WORLD_WIDTH - 1) + 5, WORLD_HEIGHT + 20, 15, 15, enemyRegion2));
            enemySpawnTimer[2] -= timebetweenspawn[2];
        }
    }

    //Detects Collision Vilius Petrauskas + Brandon Grasley
    private void detectCollision() {
        ListIterator<Cloud> enemyIterator = enemycloudslist.listIterator();
        while (enemyIterator.hasNext()) {
            Cloud enemycloud = enemyIterator.next();

            if (enemycloud.touches(player1.getBoundingbox())) {
                player1.lives = player1.lives - 1;
                enemyIterator.remove();
            }
        }
    }

    //Detects user input - Brandon Grasley
    public void inputDetection(float deltaTime) {
        float leftLimit, rightLimit, upLimit, downLimit;
        //border limits for the player to move around from - Brandon Grasley
        leftLimit = -player1.boundingbox.x;
        downLimit = -player1.boundingbox.y;
        rightLimit = WORLD_WIDTH - player1.boundingbox.x - player1.boundingbox.width;
        upLimit = WORLD_HEIGHT * 3 / 4 - player1.boundingbox.y - player1.boundingbox.height;
        //inputs - Brandon Grasley
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            player1.translate(Math.min(player1.movementspeed * deltaTime, rightLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) & leftLimit < 0) {
            player1.translate(Math.max(-player1.movementspeed * deltaTime, leftLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            player1.translate(0f, Math.min(player1.movementspeed * deltaTime, upLimit));

        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            player1.translate(0f, Math.max(-player1.movementspeed * deltaTime, leftLimit));
        }
        //touch input - Brandon Grasley
        if (Gdx.input.isTouched()) {
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            Vector2 touchpoint = new Vector2(xTouchPixels, yTouchPixels);
            touchpoint = viewport.unproject(touchpoint);

            Vector2 playermio = new Vector2(
                    player1.boundingbox.x + player1.boundingbox.width / 2,
                    player1.boundingbox.y + player1.boundingbox.height / 2);

            float touchDistance = touchpoint.dst(playermio);

            if (touchDistance > MOVEMENT_THRESHHOLD) {
                float xTouchdifference = touchpoint.x - playermio.x;
                float yTouchdifference = touchpoint.y - playermio.y;

                float xMove = xTouchdifference / touchDistance * player1.movementspeed * deltaTime;
                float yMove = yTouchdifference / touchDistance * player1.movementspeed * deltaTime;

                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);
                if (yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);

                player1.translate(xMove, yMove);
            }


        }
    }

    //how to render the background which will later be used to add multiple background on top of each other
    //to give various effects - - Brandon Grasley
    private void enemymovement(Cloud enemycloud, float deltaTime) {
        float xMove = enemycloud.getDirectionVector().x * enemycloud.movementspeed * deltaTime;
        float yMove = enemycloud.getDirectionVector().y * enemycloud.movementspeed * deltaTime;

        enemycloud.translate(xMove, yMove);
    }

    //renders the background of the starts and actual background - Brandon Grasley + + https://itch.io/profile/caniaeast Cania
    private void renderBackground(float deltaTime) {
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 8;

        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            if (backgroundOffsets[layer] > WORLD_HEIGHT) {
                backgroundOffsets[layer] = 0;
            }
            batch.draw(background[layer], 0, -backgroundOffsets[layer], WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(background[layer], 0, -backgroundOffsets[layer] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    //Brandon Grasley - Resizes to width and height of screen
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);

    }

    @Override
    public void pause() {
        // https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX - game states
        this.state = State.PAUSE;


    }

    @Override
    public void resume() {
        // https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android BennX - game states
        this.state = State.RESUME;
    }

    @Override
    public void hide() {


    }

    @Override
    public void dispose() {


    }

    @Override
    public void show() {
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/InputMultiplexer.html
        // Allows the screen to detect multiple inputs from two different stages
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, stage1));

    }
}
