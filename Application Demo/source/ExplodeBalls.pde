
//will serve as the individual balls that are in the explosion
class ExplodeBall{

  PVector location;
  PVector velocity;
  
  private float speedMag;
  private float drag;
  private float gravity;
  private float speedChangeY = 0;
  
  private float size;
  private color myColor;
  
  private float alphaChange;
  private float currentAlpha;
  private float maxAlpha = 255;
  
  private ArrayList<TrailSpark> trail;
  private boolean makeTrail;
  
  Explosion explosion;
  
  
  public ExplodeBall(float x, float y, float min, float max, float size, color c, float drag, float a, float grav, boolean makeTrail, Explosion e){

    
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
  
  void makeTrail(){
    
    //if 4 frames have passed, then create a new spark effect
       if(explosion.framesPassed % 4 == 0 && explosion.framesPassed < 120){
          trail.add(new TrailSpark(location.x, location.y, 6, 0.04)); //last two parameters are change in alpha and gravity 
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
