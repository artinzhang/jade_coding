import processing.sound.*;
int stage, t = 0, n=0,f=30;
PImage background, female,male,nurse;
PFont font;
SoundFile sound;
float factor = 1;

void setup() {
    size(1050, 600);
    font = createFont("Dizzy Edge DEMO", 75);
    background = loadImage("background.jpg");
    female= loadImage("girl.jpg");
    male= loadImage("boy.jpg");
    nurse = loadImage("nurse.jpg");
    stage = 0;
    sound = new SoundFile(this,"1.mp3");
    sound.play();
}
void draw() {
    background(0);
    textFont(font, 150);
    textSize(75);
    
    fill(255);
    imageMode(CENTER);
    image(background, width/2, height/2, 1050, 750);
    
    fill(0, 150);
    rect(0, 300, 1050, 180);
 
    if (stage == 0) {
      fill(255);
      text("Battle of memories", 240, 375);
      textSize(45);
      text("Watch your dream carefully.", 270, 450);
      textSize(45);
      fill(150, 150, 255);
      text("1.Female", 375, 555);
      text("2.Male", 570, 555);
   
    }
  
    if (stage == 1) {
        click(405,570,t);
        t++;
    
        fill(255);
        textSize(30);
        text("This story is happen in 2050. Your husband wants to divorce. But you",45, 345);
        text("have a lot of beautiful memories with him, so you decide to go to 'Memory", 45, 375);
        text("Control Center. You choose to",45, 405);
        
        fill(150, 150, 255);
        text("1.Delete your memory", 45, 450);
        text("2.just have a look", 375, 450);
        text("3.get other people's memory", 675, 450);
    
        fill(255);
        image(female, width/2, height/3, 105, 150);
    }

    if (stage == 2) {
        click(405, 570, t);
        t++; 

        fill(255);
        textSize(30);
        text("This story is happen in 2050. Your wife wants to divorce. But you", 45, 345);
        text("have a lot of beautiful memories with her, so you decide to go to 'Memory", 45, 375);
        text("Control Center. You choose to", 45, 405);
        fill(150, 150, 255);
        text("1.Delete your memory", 45, 450);
        text("2.just have a look", 375, 450);
        text("3.get other people's memory", 675, 450);
    
        fill(255);
        image(male, width/2, height/3, 150, 150);
    }
       
    if (stage == 3) {
        fill(255);
        textSize(30);
        text(" Your memories deleted successfully, and saved in a memory box.", 45, 345);
        text(" Suddenly, a man run rush to you, your memory box fall down to the floor", 45, 375);
        text(" 'Hey! watch our man!'. You yelled.", 45, 405);
        fill(150, 150, 255);
        textSize(45);
        text("Press space to continue.",315,465);
        fill(150, 150, 255);
    }
   
    if (stage == 4) {
        fill(255);
        textSize(45);
        text("Wrong choice. Game over.", 45, 345);
        
        fill(150, 150, 255);
        text("Press space to back to the beginning.", 210, 420);
        fill(150, 150, 255);
    } 
   
    if (stage == 5) {
        fill(255);
        textSize(30);
        text("Then something weird happened in your memory, you always see someone ", 45, 345);
        text("who you even don't, but the scene is so vivid in your mind. ", 45, 375);
        text("Then you decided to figure out.", 45, 105);
        fill(150, 150, 255);
        text("1.Try to see the doctor", 45, 450);
        text("2.Let it go", 435, 450);
        text("3.Go back to Memory Center", 645, 450);
        
    }
   
    if (stage == 6){
        fill(255);
        image(nurse, width/2, height/2, 1050, 705);
             
        fill(255);
        textSize(30);
        text("'It looks like you got others' memory saved in your mind.", 45, 450);
        text("'We heared there is a murder just happened, the victim looked", 45, 480);
        text("'similar with your descreption.'", 45, 510);
          
        fill(150, 150, 255);
        text("1.Freak out", 45, 555);
        text("2.Go to the police station", 330, 555);
        text("3.Cry to mom", 750, 555);
    }
    
    if (stage == 7){
        fill(255);
        image(background, width/2, height/2, 1050, 750);
             
        fill(255);
        textSize(30);
        text("77777", 45, 450);
        text(" ", 45, 480);
        text(" ", 45, 510);
          
        fill(150, 150, 255);
        text("1.Freak out", 45, 555);
        text("2.Go to the police station", 330, 555);
        text("3.Cry to mom", 750, 555);
    }
    
    if (stage == 8){
        fill(0);
        background(231,223,199);
        //image(background, width/2, height/2, 1050, 750);
             
        fill(67,100,125,80);
        f = mouseX-525;
        if(f<0)f=-f;
        if(f<50)f=50;
        if(f>263)f=f*2;
        if(f<450){
            textSize(f);
            text(" ", 45, 450);
            text("YOU WIN!!", width/2-2.5*f, 480);
            text("", 45, 510);
        }
        fill(150, 150, 255);
        text(" ", 45, 555);
        text(" ", 330, 555);
        text(" ", 750, 555);
        
        circles(width/2,height/2,100);
        n = mouseX;
        factor=map(n,0,1050,-2,2);
    }
}

void keyPressed() {
    t=0;
    
    if (stage == 0) {
        if (key == '1') {
            stage = 1;
        }
        if (key == '2') {
            stage = 2;
        }
        if (key == '3') {
            stage = 3;
        }
    } else if (stage == 1 || stage == 2) {
        if (key == '1') {
            stage = 3;
        }
        if (key == '2') {
            stage = 4;
        }
        if (key == '3') {
            stage = 4;
        }
    } else if (stage == 3) {
        if (keyPressed == true) {
            stage = 5;
        }
    } else if (stage == 4) {
        if (keyPressed == true) {
            stage = 0;
        }
    }else if(stage == 5) {
        if (key == '1') {
            stage = 4;
        }
        if (key == '2') {
            stage = 4;
        }
        if (key == '3') {
            stage = 6;
        } 
    }else if(stage == 6) {
        if (key =='1' || key == '3') 
            stage = 4;
        if (key =='2')
            stage = 7;
    }else if(stage == 7) {
        if (key =='1')
            stage = 8;
    }
}


void click(int x,int y,int size){
    fill(150,150,250,40);
    ellipse(x, y, size, size);
    fill(150,150,250,40);
    ellipse(x, y, size*2, size*2);
    fill(150,150,250,40);
    ellipse(x, y, size*4, size*4);
}

void circles(float x, float y, int size) {
  fill(67,100,125,20);
  ellipse(x, y, size, size);
  if (size > 10) {
    noStroke();
    fill(67,100,125,20);
    circles(x + size/factor, y, size/2);
    circles(x, y + size/factor, size/2);
    circles(x - size/factor, y, size/2);
    circles(x, y - size/factor, size/2);
    
    circles(x + size/factor/sqrt(2), y + size/factor/sqrt(2), size/2);
    circles(x + size/factor/sqrt(2), y - size/factor/sqrt(2), size/2);
    circles(x - size/factor/sqrt(2), y + size/factor/sqrt(2), size/2);
    circles(x - size/factor/sqrt(2), y - size/factor/sqrt(2), size/2);
  }//recurion
}