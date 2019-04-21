import org.openkinect.processing.*;

PVector rootn = new PVector(random(123), random(123));//noise root
PVector speedn = new PVector(random(-.01, .01), random(-.01, .01));//noise speed
ArrayList<Part> parts = new ArrayList<Part>(), toAdd = new ArrayList<Part>();//Parts
PVector m, pm;//mouse, previous mouse
float maxD = 40;//max distance between two smokes
PGraphics pg;
int b = 10;
//PImage img;
//------------------------------------------
Kinect2 kinect2;
float minThresh = 140;
float maxThresh = 830;
PImage img;
int record = 4500; //distance
int rx = 0;
int ry = 0;
int globleX = 0;
int globleY = 0;

void setup()
{
  size(600, 450, P2D);
  pg = createGraphics(width, height, P2D);
  //------------------------------------------
  kinect2 = new Kinect2(this);

  kinect2.initDepth();
  kinect2.initDevice(0);

  img = createImage(kinect2.depthWidth, kinect2.depthHeight, RGB);
  //------------------------------------------
}

void draw()
{
  //------------------------------------------
  PImage dImg = kinect2.getDepthImage();
  int[] depth = kinect2.getRawDepth();
  record = 4500;

  for (int x = 0; x < kinect2.depthWidth; x++) {
    for (int y = 0; y < kinect2.depthHeight; y++) {
      int offset = x + y * kinect2.depthWidth;
      int d = depth[offset];

      if (d > minThresh && d < maxThresh) {
        img.pixels[offset] = color(255, 0, 150);

        if (d < record) {
          record = d;
          rx = x;
          ry = y;
        } else {
          img.pixels[offset] = dImg.pixels[offset];
        }
      }
    }
    img.updatePixels();
  }
  print("rx=", rx, "; ");
  println("ry=", ry);
  globleX = (int)map(rx, 0, kinect2.depthWidth, 0, width);
  globleY = (int)map(ry, 0, kinect2.depthHeight, 0, height);
  //------------------------------------------
  pg.beginDraw();
  pg.noStroke();
  pg.fill(5, 20);
  pg.rect(0, 0, width, height);

  for (Part p : toAdd) //toAdd part put inside parts
  {
    parts.add(p);
  }
  toAdd = new ArrayList<Part>(); //toAdd clear out

  rootn.add(speedn);
  if (globleX == 0 || globleX == height - 1 || globleY == 0 || globleY == width - 1){
    m = null;
    pm = null;
  } else {
    m = new PVector(globleX, globleY); //move with mouse vector
  }
  int nb = parts.size()-1;
  if (m != null && nb < 800) //parts <= 800
  {
    if (pm == null) pm = m.get();
    else
    {
      float d = PVector.dist(pm, m); //pm = previous mouse
      if ((pm.x != m.x || pm.y != m.y) && d > maxD) //maxDistance between two smokes
      {
        int n = int(d / maxD); //n/ smoke between current mouse and before mouse
        PVector tmp = PVector.sub(m, pm);
        tmp.normalize(); //tmp - buffer
        tmp.mult(maxD);
        PVector tmp2 = m.get();
        for (int i = 0; i < n; i++)// put n-smokes put into parts
        {
          tmp2.sub(tmp);
          parts.add(new Part(tmp2, (int)random(0, 25), (int)random(50, 140), 0));
        }
      }
    }
    parts.add(new Part(m, (int)random(0, 25), (int)random(50, 140), 0));
    pm = m.get();
  }
  nb = parts.size()-1;
  for (int i = nb; i > -1; i--)
  {
    if (parts.get(i).display())
      parts.remove(i); // if smoke display already, then remove
  }

  pg.endDraw();
  image(pg, 0, 0);
  /*
  img = get();
  img.resize(width/2, height/2);
  img.resize(width-b, height-b);
  image(img, b/2, b/2);
  */
}

class Part
{
  float rad, c = random(.8, 1.0), theta = random(TWO_PI), nx, ny;
  int life, age, mod = (int)random(30, 40);
  PVector pos;

  Part(PVector p, int a, int l, float r)
  {
    pos = p.get();
    age = a;
    life = l;
    rad = r;
  }

  Boolean display()
  {
    nx = noise(rootn.x + pos.x/500)-.5;
    ny = noise(rootn.y + pos.y/500)-.5;
    pos.add(new PVector(6*nx, 6*ny)); // now smoke fly to noise
    rad += cos(map(age, 0, life, 0, HALF_PI)) * c; // increase the radius
    pg.stroke(200, 300 * sq(map(age, 0, life, 1, 0)));//white alfa change smoke alpha
    pg.strokeWeight(rad); //point stroke = radius
    pg.point(pos.x, pos.y);
    //age += 2;

    if (age++ % mod == 0)//split the Part in two
    {
      toAdd.add(new Part(new PVector(pos.x + rad/3 * cos(theta), pos.y + rad/3 * sin(theta)), age, life, rad * random(.5, .7)));
      toAdd.add(new Part(new PVector(pos.x - rad/3 * cos(theta), pos.y - rad/3 * sin(theta)), age, life, rad * random(.5, .7)));//.6
      age = life+1; //kill dad
    }
    return age > life;
  }
}
