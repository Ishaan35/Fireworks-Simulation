class Explosion{
  
  public int framesPassed;

  private float xPos;
  private float yPos;
  private ArrayList<ExplodeBall> explodeBalls;
  
  private boolean makeTrail;
      
  public Explosion(float x, float y, color c){
    xPos = x;
    yPos = y;
    
    explodeBalls = new ArrayList<ExplodeBall>();
    
    makeTrail = random(0,2) > 1 ? true: false;
    
    for(int i = 0; i < 120; i++){
      explodeBalls.add(new ExplodeBall(xPos, yPos, 2.7, 4, 8, c, 0.02, random(30, 100), 0.0005, makeTrail, this)); //start position x and y, minimum speed, maximum speed, size, color, drag amount (wind resistance), change in alpha (flickering speed), gravity, yes or no trail, and the explosion object reference 
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
