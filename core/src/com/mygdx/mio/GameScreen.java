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

class GameScreen implements Screen{

    private Launcher game;

     private Camera camera;
     private Viewport viewport;
     //Image gather and Display
     public SpriteBatch batch;
     private TextureAtlas textureAtlas;
     //display the background
     private TextureRegion[] background;

     // displays thea area of the texture
     private TextureRegion playerRegion,enemyRegion,enemyRegion1,enemyRegion2,playerlives;
     //Realtime
     private float[] backgroundOffsets = {0,0};
     private float backgroundMaxScrollingSpeed;
     private float timebetweenspawn[] = {1f,2f,3f};;
     private float enemySpawnTimer[] = {0,0,0};
     //world parameters
     public static int WORLD_WIDTH=72;
     public static int WORLD_HEIGHT=128;
     private  final float MOVEMENT_THRESHHOLD = 1f;
     //game objects
     private LinkedList<Cloud> enemycloudslist;

     private Player player1;
     public static int score = 0;

     private Stage stage,stage1;
     private BitmapFont font;
     private Skin skin,skin1;
     private Table table,table1;
     private TextButton pausebutton,playbutton,settingsbutton;
     private TextButton.TextButtonStyle textButtonStyle,textButtonStyle1,textButtonStyle2;

    private TextureRegion background1,background2;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    //heads up

    float hudverticalmargin, hudleftx,hudrightx,hudcentrex,hudrow1,hudrow2,hudrightxx,hudrow3,hudsectionwidth,hudrow4,hudrow5;
    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        MENU

    }


    GameScreen(Launcher mg) {
        game = mg;
        //Type of camera
        camera = new OrthographicCamera();
        //what we can visibly see
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        //gets images from a pack of textures
        textureAtlas = new TextureAtlas("images.atlas");
        //how many backgrounds
        background = new TextureRegion[2];
        // number of background + what the backgrounds location is base on the atlas
        background[0] = textureAtlas.findRegion("background1");
        background[1] = textureAtlas.findRegion("background2");


        //initalises the enemy

        enemyRegion = textureAtlas.findRegion("Sprite-0001");
        enemyRegion1 = textureAtlas.findRegion("Sprite-0003");
        enemyRegion2 = textureAtlas.findRegion("Sprite-0004");

        playerlives = textureAtlas.findRegion("heart");

        batch = new SpriteBatch();
        stage = new Stage(viewport);
        skin = new Skin();
        table = new Table(skin);

        table1 = new Table(skin1);
        stage1 = new Stage(viewport);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);

        skin.addRegions(textureAtlas);
        table.setBounds(1,0,WORLD_WIDTH,WORLD_HEIGHT);
        table1.setBounds(4,0,WORLD_WIDTH,WORLD_HEIGHT);

        textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle2.font = font;
        textButtonStyle2.up = skin.getDrawable("Sprite-0010");
        textButtonStyle2.down = skin.getDrawable("Sprite-0008");

        textButtonStyle1 = new TextButton.TextButtonStyle();
        textButtonStyle1.font = font;
        textButtonStyle1.up = skin.getDrawable("Sprite-0009");
        textButtonStyle1.down = skin.getDrawable("Sprite-0007");

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Sprite-0013");
        textButtonStyle.down = skin.getDrawable("Sprite-0012");


        settingsbutton = new TextButton("",textButtonStyle2);
        settingsbutton.setTransform(true);
        settingsbutton.setScale(0.5f);
        pausebutton = new TextButton("",textButtonStyle);
        pausebutton.setTransform(true);
        pausebutton.setScale(0.5f);
        playbutton = new TextButton("",textButtonStyle1);
        playbutton.setTransform(true);
        playbutton.setScale(0.5f);

        playbutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameState(State.RUN);
            }
        });

        pausebutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameState(State.PAUSE);
            }
        });
        settingsbutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new MainMenu(game));
                return;
            }
        });
        table.left().top();
        table.add(pausebutton);
        table1.add(settingsbutton);
        table1.add(playbutton);




        stage1.addActor(table1);

        stage.addActor(table);

        enemycloudslist = new LinkedList<>();
        //initalises the player
        playerRegion = textureAtlas.findRegion("Mio");
        player1 = new Player(100,WORLD_WIDTH/2,WORLD_HEIGHT/4,3,15,15,playerRegion);
        //makes it scrollable
        backgroundMaxScrollingSpeed = (float) (WORLD_HEIGHT)/4;
        //to draw the sprites player / enemy
        background1 = textureAtlas.findRegion("Sprite-0011");
        background2 = textureAtlas.findRegion("background1");

        hud();


    }
    private void hud()
    {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = fontGenerator.generateFont(fontParameter);


        font.getData().setScale(0.2f);
        font.setUseIntegerPositions(false);

        hudverticalmargin = font.getCapHeight() / 2;
        hudleftx = hudverticalmargin;
        hudrightx = WORLD_WIDTH *2 /3 - hudverticalmargin;
        hudrightxx = WORLD_WIDTH -16;

        hudcentrex = WORLD_HEIGHT - hudverticalmargin;
        hudrow1 = WORLD_HEIGHT - hudverticalmargin - font.getCapHeight();
        hudrow2 = hudrow1 - hudsectionwidth - font.getCapHeight();
        hudrow3 = (float) (WORLD_HEIGHT-WORLD_HEIGHT*0.09);
        hudrow4 = (float) (WORLD_HEIGHT - WORLD_HEIGHT*0.1);
        hudrow5 = (float) (WORLD_HEIGHT - WORLD_HEIGHT*0.12);

        hudsectionwidth = WORLD_WIDTH / 3;

    }
     State state = State.RUN;
     @Override
     public void render(float deltaTime) {
         switch (state)
         {
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
             case PAUSE:
                 Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
                 batch.begin();

                 batch.draw(background2,0,0,WORLD_WIDTH,WORLD_HEIGHT);
                 batch.draw(background1, WORLD_WIDTH/6,WORLD_HEIGHT/3,50,50);
                 font.draw(batch,String.format(Locale.getDefault(),"%07d",score),WORLD_WIDTH/3, (float) (WORLD_HEIGHT*2/3)-10,hudsectionwidth,Align.center,false);
                 font.draw(batch,"Current Score:",WORLD_WIDTH/3,(WORLD_HEIGHT*2/3),hudsectionwidth, Align.center,false);
                 batch.end();
                 stage1.draw();

//do stuff here

                 break;
             case RESUME:
                 setGameState(State.RUN);

                 break;

             case MENU:
                 game.setScreen(game.menu);
                 break;
             default:
                 break;
         }


     }
    public void setGameState(State s){
        this.state = s;
    }
     private void updateandrenderscore(){
            font.draw(batch,"Score",hudrightx,hudrow4,hudsectionwidth, Align.right,false);
            font.draw(batch,String.format(Locale.getDefault(),"%05d",score),hudrightx,hudrow5,hudsectionwidth,Align.right,false);

     }
     private void updateandrenderlives(){
         font.draw(batch,"Lives",hudrightx,hudrow1,hudsectionwidth, Align.right,false);
         if(player1.lives == 3)
         {
             batch.draw(playerlives,hudrightxx,hudrow3,5.4f,4.5f);
             batch.draw(playerlives,hudrightxx+5,hudrow3,5.4f,4.5f);
             batch.draw(playerlives,hudrightxx+10,hudrow3,5.4f,4.5f);
         }
         else if(player1.lives == 2)
         {
             batch.draw(playerlives,hudrightxx,hudrow3,5.4f,4.5f);
             batch.draw(playerlives,hudrightxx+5,hudrow3,5.4f,4.5f);
         }
         else if(player1.lives == 1)
         {
             batch.draw(playerlives,hudrightxx,hudrow3,5.4f,4.5f);
         }
     }
     private void rendergameover() {
         if (player1.lives <= 0) {
             game.setScreen(game.Gameover);
         }
     }
     private void spawnEnemies(float deltaTime){
        enemySpawnTimer[0] += deltaTime;
         enemySpawnTimer[1] += deltaTime;
         enemySpawnTimer[2] += deltaTime;

        if(enemySpawnTimer[0] > timebetweenspawn[0]){
            enemycloudslist.add(new Cloud(20,Launcher.random.nextFloat()*(WORLD_WIDTH-1)+5,WORLD_HEIGHT+20,15   ,15,enemyRegion));

            enemySpawnTimer[0] -= timebetweenspawn[0];
        }
         if(enemySpawnTimer[1] > timebetweenspawn[1]){
             enemycloudslist.add(new Cloud(20,Launcher.random.nextFloat()*(WORLD_WIDTH-1)+5,WORLD_HEIGHT+20,15,15,enemyRegion1));

             enemySpawnTimer[1] -= timebetweenspawn[1];
         }
         if(enemySpawnTimer[2] > timebetweenspawn[2]){
             enemycloudslist.add(new Cloud(20,Launcher.random.nextFloat()*(WORLD_WIDTH-1)+5,WORLD_HEIGHT+20,15,15,enemyRegion2));
             enemySpawnTimer[2] -= timebetweenspawn[2];
         }
     }

     private void detectCollision() {
         ListIterator<Cloud> enemyIterator = enemycloudslist.listIterator();
         while (enemyIterator.hasNext())
         {
             Cloud enemycloud = enemyIterator.next();

                 if(enemycloud.touches(player1.getBoundingbox()))
                 {
                     player1.lives = player1.lives - 1;
                     enemyIterator.remove();
                 }
         }
    }

    public void inputDetection(float deltaTime){
        float leftLimit, rightLimit, upLimit, downLimit;
        //border limits for the player to move around from
        leftLimit = -player1.boundingbox.x;
        downLimit = -player1.boundingbox.y;
        rightLimit = WORLD_WIDTH - player1.boundingbox.x - player1.boundingbox.width;
        upLimit = WORLD_HEIGHT*3/4 - player1.boundingbox.y - player1.boundingbox.height;
        //inputs
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& rightLimit > 0){
            player1.translate(Math.min(player1.movementspeed*deltaTime,rightLimit),0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)&leftLimit < 0){
            player1.translate(Math.max(-player1.movementspeed*deltaTime,leftLimit),0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)&& upLimit > 0){
            player1.translate(0f,Math.min(player1.movementspeed*deltaTime,upLimit));

        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)&& downLimit < 0){
            player1.translate(0f,Math.max(-player1.movementspeed*deltaTime,leftLimit));
        }
        //touch input
        if(Gdx.input.isTouched())
        {
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            Vector2 touchpoint = new Vector2(xTouchPixels,yTouchPixels);
            touchpoint = viewport.unproject(touchpoint);

            Vector2 playermio = new Vector2(
                    player1.boundingbox.x+player1.boundingbox.width/2,
                    player1.boundingbox.y+player1.boundingbox.height/2);

            float touchDistance = touchpoint.dst(playermio);

            if(touchDistance > MOVEMENT_THRESHHOLD){
                float xTouchdifference = touchpoint.x - playermio.x;
                float yTouchdifference = touchpoint.y - playermio.y;

                float xMove = xTouchdifference / touchDistance* player1.movementspeed* deltaTime;
                float yMove = yTouchdifference / touchDistance* player1.movementspeed* deltaTime;

                if(xMove > 0) xMove = Math.min(xMove,rightLimit);
                else xMove = Math.max(xMove,leftLimit);
                if(yMove > 0) yMove = Math.min(yMove,upLimit);
                else yMove = Math.max(yMove,downLimit);

                player1.translate(xMove,yMove);
            }


        }
    }
    //how to render the background which will later be used to add multiple background on top of each other
    //to give various effects
    private void enemymovement(Cloud enemycloud ,float deltaTime){
        float xMove = enemycloud.getDirectionVector().x * enemycloud.movementspeed*deltaTime;
        float yMove = enemycloud.getDirectionVector().y *enemycloud.movementspeed*deltaTime;

        enemycloud.translate(xMove,yMove);
    }
    private void renderBackground(float deltaTime){
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 8;

        for (int layer = 0; layer < backgroundOffsets.length;layer++){
            if(backgroundOffsets[layer]>WORLD_HEIGHT){
                backgroundOffsets[layer] = 0;
            }
            batch.draw(background[layer],0,-backgroundOffsets[layer],WORLD_WIDTH,WORLD_HEIGHT);
            batch.draw(background[layer],0,-backgroundOffsets[layer]+WORLD_HEIGHT,WORLD_WIDTH,WORLD_HEIGHT);
        }
    }
     @Override
     public void resize(int width, int height) {
     viewport.update(width, height,true);
     batch.setProjectionMatrix(camera.combined);

     }

     @Override
     public void pause() {
         this.state = State.PAUSE;


     }

     @Override
     public void resume() {
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
         Gdx.input.setInputProcessor(new InputMultiplexer(stage,stage1));

     }
 }
