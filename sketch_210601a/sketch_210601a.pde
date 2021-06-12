
ArrayList<Firework> fireworks = new ArrayList<Firework>();

float time; //start time in seconds
PImage bg;
SoundFile rocket; //rocket sound
SoundFile crackle; //rocket sound
SoundFile explode; //rocket sound


meteorShower m;//x speed and y speed

//make meteorshowerobject and call its show method
void setup()
{

  //setting up sound and volume
  rocket = new SoundFile(this,"Rocket.wav");
  crackle = new SoundFile(this,"FireworkCrackle.wav");
  explode = new SoundFile(this, "FireworkExplode.wav");
  rocket.amp(0.025);
  crackle.amp(0.5);
  explode.amp(0.05);
  
  size(1600,900);  



  noStroke();
  
  
  time = millis()/1000;
  bg = loadImage("Sky.jpg");
  bg.resize(1600,900); //autoadjust background for any selected size
  
  m = new meteorShower(-20, 10);
}

 

void draw()
{
   background(bg);
    
    m.show();

  //add firework every second. time is an int so any milleseconds between current 1000 and next 1000 will be ignored
  if(millis()/1000 > time){
    time = millis()/1000; 
    fireworks.add(new Firework( 6, 0.02));
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
