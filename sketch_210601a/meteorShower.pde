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
  
  void show(){
    
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
