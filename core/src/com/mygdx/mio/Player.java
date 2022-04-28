package com.mygdx.mio;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    float movementspeed; // movement speed
    int lives; // charges which will be added later

    TextureRegion playerTexture; // rectangular area of a texture

    // will be used in creating collisions
    Rectangle boundingbox;
    //constructor
    public Player(float movementspeed, float xCentre, float yCentre, int lives ,float width, float height, TextureRegion playerTexture) {
        this.movementspeed = movementspeed;
        this.lives = lives;
        this.playerTexture = playerTexture;
        this.boundingbox = new Rectangle(xCentre - width/2,yCentre - height/2,width,height);
    }
    //draw the model
    public void draw(Batch batch){
        batch.draw(playerTexture,boundingbox.x,boundingbox.y,boundingbox.width,boundingbox.height);

    }
    //update in real time
    public void update(float deltaTime){

    }
    //define what values are being presented.
    public void translate(float x,float y){
        boundingbox.setPosition(boundingbox.x+x,boundingbox.y+y);
    }
    public Rectangle getBoundingbox(){
        return new Rectangle(boundingbox.x, boundingbox.y,boundingbox.width, boundingbox.height);
    }
    public boolean touches(Rectangle otherRectangle){
        Rectangle thisRectangle = new Rectangle(boundingbox.x, boundingbox.y, boundingbox.width, boundingbox.height);
        return thisRectangle.overlaps(otherRectangle);
    }



}
