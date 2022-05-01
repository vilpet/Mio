package com.mygdx.mio;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
public class Cloud {

    float movementspeed;// movement speed - Brandon Grasley
    Vector2 directionVector;
    float timesincelastdirection = 0;
    float directionchange = 0.75f;
    TextureRegion cloudTexture;
    // will be used in creating collisions
    Rectangle boundingbox;
    //constructor
    public Cloud(float movementspeed, float xCentre, float yCentre, float width, float height, TextureRegion cloudTexture) {
        this.movementspeed = movementspeed;
        this.cloudTexture = cloudTexture;
        this.boundingbox = new Rectangle(xCentre - width/2,yCentre - height/2,width,height);
        directionVector = new Vector2(0,-1);
    }
    public Vector2 getDirectionVector() {
        return directionVector;
    }
    private  void randomizeDirectionVector(){
        directionVector.y -= 0.5;
    }
    //draw the model
    public void draw(Batch batch){
        batch.draw(cloudTexture,boundingbox.x,boundingbox.y,boundingbox.width,boundingbox.height);

    }
    //update in real time
    public void update(float deltaTime){
        timesincelastdirection += deltaTime;
        if(timesincelastdirection > directionchange)
        {
            randomizeDirectionVector();
            timesincelastdirection -= directionchange;
        }
    }

    //detection if the items overlap.
    public boolean touches(Rectangle otherRectangle){
        Rectangle thisRectangle = new Rectangle(boundingbox.x, boundingbox.y, boundingbox.width, boundingbox.height);
        return thisRectangle.overlaps(otherRectangle);
    }
    public void translate(float x,float y) {
        boundingbox.setPosition(boundingbox.x + x, boundingbox.y + y);
    }

}
