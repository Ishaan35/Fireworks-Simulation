import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_210601a extends PApplet {


ArrayList<Firework> fireworks = new ArrayList<Firework>();

float time; //start time in seconds
PImage bg;
SoundFile rocket; //rocket sound
SoundFile crackle; //rocket sound
SoundFile explode; //rocket sound


meteorShower m;//x speed and y speed

//make meteorshowerobject and call its show method
public void setup()
{

  //setting up sound and volume
  rocket = new SoundFile(this,"Rocket.wav");
  crackle = new SoundFile(this,"FireworkCrackle.wav");
  explode = new SoundFile(this, "FireworkExplode.wav");
  rocket.amp(0.025f);
  crackle.amp(0.5f);
  explode.amp(0.05f);
  
    



  noStroke();
  
  
  time = millis()/1000;
  bg = loadImage("Sky.jpg");
  bg.resize(1600,900); //autoadjust background for any selected size
  
  m = new meteorShower(-20, 10);
}

 

public void draw()
{
   background(bg);
    
    m.show();

  //add firework every second. time is an int so any milleseconds between current 1000 and next 1000 will be ignored
  if(millis()/1000 > time){
    time = millis()/1000; 
    fireworks.add(new Firework( 6, 0.02f));
  }
  //enhanced for loop to continue running each firework
  for(Firework f : fireworks){
    f.Go();
  }
  
    //run a loop that checks if firework is done (using its isDone method) and if so, remove from list of fireworks
  int j = 0;
  while(j < fireworks.size()){
    if(fireworks.get(j).isDone()){
      fireworks.remove(j);
    }else{
      j++;
    }
  }
  
  
  
  
}

//will serve as the individual balls that are in the explosion
class ExplodeBall{

  PVector location;
  PVector velocity;
  
  private float speedMag;
  private float drag;
  private float gravity;
  private float speedChangeY = 0;
  
  private float size;
  private int myColor;
  
  private float alphaChange;
  private float currentAlpha;
  private float maxAlpha = 255;
  
  private ArrayList<TrailSpark> trail;
  private boolean makeTrail;
  
  Explosion explosion;
  
  
  public ExplodeBall(float x, float y, float min, float max, float size, int c, float drag, float a, float grav, boolean makeTrail, Explosion e){

    
    location = new PVector(x,y);
    velocity = new PVector(random(-1,1),random(-1,1)); //set direction
    gravity = grav;
    
    speedMag = random(min, max);    
    this.size = size;
    this.drag = drag;
    myColor = c;
    
    currentAlpha = random(0, 255);
    alphaChange = a;
    
    trail = new ArrayList<TrailSpark>();
    this.makeTrail = makeTrail;
    
    explosion = e;
  }
  
  public void move(){
        
    fill(myColor);
    location.add(velocity);
    velocity.setMag(speedMag);
    speedMag -= drag;
    ellipse(location.x, location.y, size,size);
    
    velocity.set(velocity.x, velocity.y + speedChangeY);
    speedChangeY+=gravity;
    
    //twinkle
    if(explosion.framesPassed > 20){
      myColor = color(red(myColor), green(myColor), blue(myColor), currentAlpha);
      
      currentAlpha += alphaChange;
      if(currentAlpha >=maxAlpha || currentAlpha <= 0){ //change alpha between 0 and maxAlpha
        alphaChange*=-1;
      }
    }
    //if 100 frames have passed, then gradually change the maximum alpha that the twinkle can reach to 0, which will make it evenually die out
    if(explosion.framesPassed >= 100){
      maxAlpha -= 5;
      alphaChange -=10;
    } 
    
    if(makeTrail){
       makeTrail();
    }
    
  }
  
  public void makeTrail(){
    
    //if 4 frames have passed, then create a new spark effect
       if(explosion.framesPassed % 4 == 0 && explosion.framesPassed < 120){
          trail.add(new TrailSpark(location.x, location.y, 6, 0.04f)); //last two parameters are change in alpha and gravity 
       }

       //the spark class has a method that takes care of display and movement
       for(int i = 0; i < trail.size(); i++){
         trail.get(i).ExplodeTrailSparks();
       }
       
       //only increment if you do not remove anything, to make sure the next element won't get skipped (because the list size shrinks)
       int j = 0;
       while(j < trail.size()){
         if(trail.get(j).Alpha() <= 20){ //no alpha = safe to remove
           trail.remove(j);
         }
         else
           j++;
       }  
  }
  
  
}
class Explosion{
  
  public int framesPassed;

  private float xPos;
  private float yPos;
  private ArrayList<ExplodeBall> explodeBalls;
  
  private boolean makeTrail;
      
  public Explosion(float x, float y, int c){
    xPos = x;
    yPos = y;
    
    explodeBalls = new ArrayList<ExplodeBall>();
    
    makeTrail = random(0,2) > 1 ? true: false;
    
    for(int i = 0; i < 120; i++){
      explodeBalls.add(new ExplodeBall(xPos, yPos, 2.7f, 4, 8, c, 0.02f, random(30, 100), 0.0005f, makeTrail, this)); //start position x and y, minimum speed, maximum speed, size, color, drag amount (wind resistance), change in alpha (flickering speed), gravity, yes or no trail, and the explosion object reference 
    }
    int rand = (int)random(0,3);
    if(rand == 0)
      crackle.play();
    else
      explode.play();
  }
  
  public void explode(){
    framesPassed++;
    for(ExplodeBall e: explodeBalls){
      e.move(); //each colored ball has a method to move
    }
  }
  
  public boolean isDone(){
    if(framesPassed > 200){ //200 frames passed which is about 6-7 seconds assuming your computer runs at 60fps (most do). Firework is done by this time
      return true;
    }
    return false;
  }
}


class Firework{
  

   private float startY;   
   private float endY;
   private float currentY;
   private float launchSpeedY;
   private float gravityY;
   private float xPos;
   private Explosion explosion;
   
   private int[] colors = {
     color(255, 0, 68),
     color(27, 139, 250),
     color(0, 255, 0),
     color(255, 0, 250),
     color(255, 132, 0),
     color(255, 255, 0),
     color(0, 242, 255),
     color(255, 0, 68),
     color(27, 139, 250),
   };
   
   
   private float trailWaitFrames = 0; //keeps track of how many frames have passed before creating another spark ellipse
   
   ArrayList<TrailSpark> trail;
   
   public Firework(float lY, float grav){
     endY = random(200, 500); //assuming min height of screen is 400  
     startY = random(height-200, height-50);
     currentY = startY;
     xPos = random(100, width-100);
     trail = new ArrayList<TrailSpark>();
     
     launchSpeedY = lY;
     gravityY = grav;
     
     int rand = (int)random(0,2);
     if(rocket != null && rand == 0)
       rocket.play();
     
     
     
   }
   
   public void Go(){
      if(currentY >= endY){ //still going up
        shootInAir();
      }
      else{
        Explode();
      }

   }
   
   public void shootInAir(){
    
       currentY-= launchSpeedY; 
       launchSpeedY-= gravityY; //gravity acceleration 
       trailWaitFrames++;
       
       //if 2 frames have passed, then create a new spark effect
       if(trailWaitFrames >= 2){
          trail.add(new TrailSpark(xPos, currentY, 6, 0.04f)); //last two parameters are change in alpha and gravity 
          trailWaitFrames=0;
       }

       //the spark class has a method that takes care of display and movement
       for(int i = 0; i < trail.size(); i++){
         trail.get(i).LaunchSparks();
       }
       
       //only increment if you do not remove anything, to make sure the next element won't get skipped (because the list size shrinks)
       int j = 0;
       while(j < trail.size()){
         if(trail.get(j).Alpha() <= 0){ //no alpha = safe to remove
           trail.remove(j);
         }
         else
           j++;
       }  
   }
   
   public void Explode(){
     //create object initially, only once
     if(explosion == null){
       explosion = new Explosion(xPos, endY, colors[(int)random(0,colors.length)]);
     }
     explosion.explode();
   }
   
   public boolean isDone(){
     if(explosion != null){
        return explosion.isDone(); 
     }
     return false;
   }
}
//meant for spark and trail effects (the orange things when firework is going up, and the explode trails
class TrailSpark{
  
  private float xPos;
  private float yPos;
  private float alphaDifference; //step amount to reduce alpha by
  private float currentAlpha; 
  private float gravity = 0;
  private float ySpeed = 0;
  
  private float xSpeed;
  
  
  public TrailSpark(float x, float y, float a, float gravity){
     xPos = x + random(-4,4);
     yPos = y;
     alphaDifference = a;
     currentAlpha = 255;
     this.gravity = gravity;
     xSpeed = random(-0.75f,0.75f);
  }
  
  public float Alpha(){
    return currentAlpha;
  }
  
  //when firework goes up
  public void LaunchSparks(){
    if(currentAlpha >= 0){
          currentAlpha -= alphaDifference;  //sparks gradually fade out
          fill(color(255, 119, 0, currentAlpha));
          yPos += ySpeed;
          xPos += xSpeed;
          ySpeed += gravity;
          ellipse(xPos, yPos, 6,6);
    }

  }
  //for explosion
  public void ExplodeTrailSparks(){
    if(currentAlpha >= 0){
          currentAlpha -= alphaDifference/1.5f; 
          fill(color(255, 119, 0, currentAlpha));
          yPos += ySpeed;
          xPos += xSpeed/2;
          ySpeed += gravity/2;
          ellipse(xPos, yPos, 2.5f,2.5f);
    }

  }
  
  
  
  
}  
class meteorShower{
  
  int startTime = millis()/1000;
  float waitTime;
  ArrayList<Float> xPositions; //Float wrapper class of float primitive type
  ArrayList<Float> yPositions;
  
  float xSpeed;
  float ySpeed;
  
  PImage meteor;

  
  
  public meteorShower(float x, float y){
    meteor = loadImage("meteor.png");
    xPositions = new ArrayList<Float>();
    yPositions = new ArrayList<Float>();

    xSpeed = x;
    ySpeed = y;
  }
  
  public void show(){
    
    if(millis()/1000 > startTime){
      startTime = millis()/1000;
      xPositions.add((float)width+500);
      yPositions.add(random(-600, 300));
      
    }
    
    for(int i = 0; i < xPositions.size(); i++){
        image(meteor, xPositions.get(i), yPositions.get(i), 120, 120);
        
        xPositions.set(i, xPositions.get(i) + xSpeed);
        yPositions.set(i, yPositions.get(i) + ySpeed);
    }
    int j = 0;
    
    while(j < xPositions.size()){
      if(xPositions.get(j) < -200){
        xPositions.remove(j);
        yPositions.remove(j);
      }
      else{j++;}
    }
    
  }
  
}
  public void settings() {  size(1600,900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_210601a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
