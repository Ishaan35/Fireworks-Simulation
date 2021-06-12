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
     xSpeed = random(-0.75,0.75);
  }
  
  public float Alpha(){
    return currentAlpha;
  }
  
  //when firework goes up
  void LaunchSparks(){
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
  void ExplodeTrailSparks(){
    if(currentAlpha >= 0){
          currentAlpha -= alphaDifference/1.5; 
          fill(color(255, 119, 0, currentAlpha));
          yPos += ySpeed;
          xPos += xSpeed/2;
          ySpeed += gravity/2;
          ellipse(xPos, yPos, 2.5,2.5);
    }

  }
  
  
  
  
}  
