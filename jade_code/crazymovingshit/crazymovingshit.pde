int xPos;
int yPos;
int xSpeed;
int ySpeed;
float x,y,R,r,angle;
int xm,ym;

void setup() {
  background(158,231,250);
  size(800,400);
  
  xPos = width/2;
  yPos = height/2;
  
  xSpeed=6;
  ySpeed=5; 
  
  r = 20; 
  R = 150; 
  x = 0;
  angle = 0;
  y = height/2;
  
  xm=0;
  ym=0;
}

void draw(){
  background(158,231,250);
  
  int n,i;
  n=25;
  i=0;
  
  for(;i<5;){
    fill(255, 95, 166);
    arc(n, 200, 50, 50, PI, PI*2);
    n = n+50;
    i=i+1;
  }
  
  n = 50;
  for(i=0; i<3; i++){
    fill(158,231,250);
    stroke(255, 95, 166);
    arc(n, 225, 50, 50, PI, PI*2 );
    n = n+50;
  }
  stroke(100);
  line(200, 350, 400, 5);
  line(600, 350, 400, 5);
  
  fill(158,231,250);
  stroke(100);
  ellipse(440, 80, 120, 120);
  
  fill(158,231,250);
  stroke(255, 95, 166);
  ellipse(470,131,60,60);
  
  stroke(255, 95, 166);
  noFill();
  rect(27, 27, 427, 27);
  
  fill(255, 95, 166);
  ellipse(600, 320, 300, 79);
  
  fill(255, 95, 166);
  stroke(100);
  triangle(0, 400, 800, 0, 420, 220);
  
  ellipse(xPos, yPos, 50, 50);
  //xPos = xPos+1;
  //xPos +=1;
  textSize(32);
  fill(0, 102, 153);
  text("Crazy moving sh*t", 10, 30); 
  fill(0, 102, 153);

if(xPos > width-25 || xPos <25){
   xSpeed *= -1;
   //xSpeed = xSpeed * -1;
}

if(yPos > height-25 || yPos <25){
   ySpeed *=-1;
}

  xPos = xPos + xSpeed;
  yPos = yPos + ySpeed;
  
  x = R *cos(angle)+width/2;
  y = R * sin(angle)+height/2;
  ellipse(x, y, r, r);
  angle += 0.05;
  
  xm = mouseX;
  ym = mouseY;
  fill(128,128,128);
  ellipse(xm, ym, 50, 50);
}