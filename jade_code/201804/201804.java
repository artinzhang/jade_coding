import codeanticode.syphon.*;
PGraphics canvas;
SyphonServer server;
import org.openkinect.processing.*;

Kinect2 kinect2;//类型
float minThresh = 50;
float maxThresh = 1800;
PImage img;
//for kinect

final int nbWeeds = 60;// amount of lines
SeaWeed[] weeds;// [] 数组 SeaWeed[0]，SeaWeed[1]，SeaWeed[2]...
PVector rootNoise = new PVector(random(123456), random(123456));//0～123456，
int mode = 1;
float radius = 220; //big circle r
Boolean noiseOn = true;//布尔型
PVector center;
//for drawing
//声明 call
//类型+space+名字（=初始值）；

void setup()
{
  size(640,640,P3D); //画布
  canvas = createGraphics(640,640,P3D); //画布+mapping
  server = new SyphonServer(this,"Processing Syphon");//服务器
  //for projection mapping

  center = new PVector(width/2, height/2);
  strokeWeight(1);
  weeds = new SeaWeed[nbWeeds];// weeds = new SeaWeed[60];
  for (int i = 0; i < nbWeeds; i++) //
  {
    weeds[i] = new SeaWeed(i*TWO_PI/nbWeeds, 3*radius);//
  }
  //for drawing

  kinect2 = new Kinect2(this);

  kinect2.initDepth();//深度画布 grey
  kinect2.initDevice(0);

  img = createImage(kinect2.depthWidth, kinect2.depthHeight, RGB);//创建一个长宽和kinect一样的图像
  //for kinect
}

void draw()
{ //background(50);
  canvas.beginDraw();
  //canvas.background();
  canvas.noStroke();
  canvas.fill(20, 10, 20, 70);//, 50);
  canvas.rect(0, 0, width, height);
  /////////////////////////////////////////////////
  PImage dImg = kinect2.getDepthImage();//深度图 grey
  //img.loadPixels();
  //mage(img,0,0);
  int[] depth = kinect2.getRawDepth();//用一个数组来储存深度原始数据

  int record = 4500; //distance
  int rx = 0;//最近点x
  int ry = 0;//最近点y

  for (int x = int(kinect2.depthWidth*0.30); x < kinect2.depthWidth*0.70; x++) {
    for (int y = int(kinect2.depthHeight*0.30); y < kinect2.depthHeight*0.70; y++) {
      int offset = x + y * kinect2.depthWidth;//(x,y)在rawdata上的序数
      int d = depth[offset];//(x,y)此点离屏幕的距离
      if (d > minThresh && d < maxThresh && x > 100) {
        img.pixels[offset] = color(255, 0, 150);

        if  ( d < record){
          record = d;
          rx = x;
          ry = y;
        } else {
          img.pixels[offset] = dImg.pixels[offset];
        }
      }
    }
    img.updatePixels();
    //image(img,0,0);
  }
  //for kinect find the nearest point

rootNoise.add(new PVector(.01, .01));
  canvas.strokeWeight(1.9);
  for (int i = 0; i < nbWeeds; i++)
  {
    weeds[i].update(rx+88,ry+48);
  }
  canvas.stroke(120, 0, 0, 220);
  canvas.strokeWeight(4);
  canvas.noFill();
  canvas.ellipse(center.x, center.y, 2*radius, 2*radius);

  canvas.endDraw();
  image(canvas, 0, 0);

  server.sendImage(canvas);
}
//特效

void keyPressed()
{
  if(key == 'n')
  {
    noiseOn = !noiseOn;
  }else
  {
  mode = (mode + 1) % 2;
  }
}

class MyColor
{
  float R, G, B, Rspeed, Gspeed, Bspeed;
  final static float minSpeed = .6;
  final static float maxSpeed = 1.8;
  final static float minR = 10;//10 //200
  final static float maxR = 255;//255   //255
  final static float minG = 0; //0  //20
  final static float maxG = 0;//0  //120
  final static float minB = 0;
  final static float maxB = 0;

  MyColor()
  {
    init();
  }
   public void init()
  {
    R = random(minR, maxR);
    G = random(minG, maxG);
    B = random(minB, maxB);
    Rspeed = (random(1) > .5 ? 1 : -1) * random(minSpeed, maxSpeed);
    Gspeed = (random(1) > .5 ? 1 : -1) * random(minSpeed, maxSpeed);
    Bspeed = (random(1) > .5 ? 1 : -1) * random(minSpeed, maxSpeed);
  }

  public void update()
  {
    Rspeed = ((R += Rspeed) > maxR || (R < minR)) ? -Rspeed : Rspeed;
    Gspeed = ((G += Gspeed) > maxG || (G < minG)) ? -Gspeed : Gspeed;
    Bspeed = ((B += Bspeed) > maxB || (B < minB)) ? -Bspeed : Bspeed;
  }

  public color getColor()
  {
    return color(R, G, B);
  }
}
//线条的颜色

class SeaWeed
{
  final static float DIST_MAX = 5.5;//length of each segment
  final static float maxWidth = 50;//max width of the base line
  final static float minWidth = 11;//min width of the base line
  final static float FLOTATION = -3.5;//flotation constant
  float mouseDist;//mouse interaction distance
  int nbSegments;//数量
  PVector[] pos;//position of each segment
  color[] cols;//colors array, one per segment
  float[] rad;
  MyColor myCol = new MyColor();
  float x, y;//origin of the weed
  float cosi, sinu;

  SeaWeed(float p_rad, float p_length)
  {
    nbSegments = (int)(p_length/DIST_MAX);// lines amount
    pos = new PVector[nbSegments];
    cols = new color[nbSegments];
    rad = new float[nbSegments];
    cosi = cos(p_rad); // xie lv
    sinu = sin(p_rad);
    x = width/2 + radius*cosi; // begin pos
    y = height/2 + radius*sinu;
    mouseDist = 40; // blank circle r
    pos[0] = new PVector(x, y);
    for (int i = 1; i < nbSegments; i++)
    {
      pos[i] = new PVector(pos[i-1].x - DIST_MAX*cosi, pos[i-1].y - DIST_MAX*sinu);//p[i]关于p[0]的推导式
      cols[i] = myCol.getColor();
      rad[i] = 3;
    }
  }
  //把一个线拆分成DIST长的小线段

  void update(int ux,int uy)//此时ux uy 为最近点的x y
  {
    PVector mouse = new PVector(ux, uy);

    pos[0] = new PVector(x, y);
    for (int i = 1; i < nbSegments; i++)
    {
      float n = noise(rootNoise.x + .002 * pos[i].x, rootNoise.y + .002 * pos[i].y);
      float noiseForce = (.5 - n) * 7; //noise force
      if(noiseOn)
      {
        pos[i].x += noiseForce;
        pos[i].y += noiseForce;
      }
      PVector pv = new PVector(cosi, sinu);
      pv.mult(map(i, 1, nbSegments, FLOTATION,  .6*FLOTATION));
      pv.mult(3);//!
      pos[i].add(pv);
      //mouse interaction
      //if(pmouseX != mouseX || pmouseY != mouseY)
      {
        float d = PVector.dist(mouse, pos[i]);
        if (d < mouseDist)// && pmouseX != mouseX && abs(pmouseX - mouseX) < 12)
        {
          PVector tmpPV = mouse.get();
          tmpPV.sub(pos[i]); //substract
          tmpPV.normalize(); //1
          tmpPV.mult(mouseDist); //multiply
          tmpPV = PVector.sub(mouse, tmpPV);
          pos[i] = tmpPV.get();
        }
      }
       PVector tmp = PVector.sub(pos[i-1], pos[i]);
      tmp.normalize();
      tmp.mult(DIST_MAX);
      //tmp.mult(3);
      pos[i] = PVector.sub(pos[i-1], tmp);
      //遇到离最近点小于40时 开始溜溜球

      //keep the points inside the circle
      if(PVector.dist(center, pos[i]) > radius)
      {
        PVector tmpPV = pos[i].get();
        tmpPV.sub(center);
        tmpPV.normalize();
        tmpPV.mult(radius);
        tmpPV.add(center);
        pos[i] = tmpPV.get();
      }
    }
    //溜溜球的时候不要离开大圆
    updateColors();

    if (mode == 0)
    {
      canvas.stroke(0, 100);
    }
    canvas.beginShape();
    canvas.noFill();
    for (int i = 0; i < nbSegments; i++)
    {
      float r = rad[i];
      if (mode == 1)
      {
        canvas.stroke(cols[i]);
        canvas.vertex(pos[i].x, pos[i].y);
        //line(pos[i].x, pos[i].y, pos[i+1].x, pos[i+1].y);
      } else
      {
        canvas.fill(cols[i]);
        canvas.noStroke();
        canvas.ellipse(pos[i].x, pos[i].y, 2, 2);
      }
    }
    canvas.endShape();
  }
void updateColors()
  {
    myCol.update();
    cols[0] = myCol.getColor();
    for (int i = nbSegments-1; i > 0; i--)
    {
      cols[i] = cols[i-1];
    }
  }
}
