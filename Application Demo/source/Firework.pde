import processing.sound.*;

class Firework{
  

   private float startY;   
   private float endY;
   private float currentY;
   private float launchSpeedY;
   private float gravityY;
   private float xPos;
   private Explosion explosion;
   
   private color[] colors = {
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
   
   void Go(){
      if(currentY >= endY){ //still going up
        shootInAir();
      }
      else{
        Explode();
      }

   }
   
   void shootInAir(){
    
       currentY-= launchSpeedY; 
       launchSpeedY-= gravityY; //gravity acceleration 
       trailWaitFrames++;
       
       //if 2 frames have passed, then create a new spark effect
       if(trailWaitFrames >= 2){
          trail.add(new TrailSpark(xPos, currentY, 6, 0.04)); //last two parameters are change in alpha and gravity 
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
   
   void Explode(){
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
