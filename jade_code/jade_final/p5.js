// declare IP address, FILL HERE INFO FROM CLIENT
var kinectronIpAddress = "149.31.198.55";

// variable to hold an instance of the serialport library
var serial;

// declare kinectron
var kinectron = null; // Empty pointer
var handRight = null;
var myCanvas = null;

var num = 1500; // # of particle
var vx = new Array(num); // particle velocity in x
var vy = new Array(num);
var x = new Array(num); //  position of each particle
var y = new Array(num);
var x2 = new Array(num); // origin position -> born
var y2 = new Array(num);
var ax = new Array(num); // acceleration of each particle
var ay = new Array(num);

var magnetism = 100.0; // gravity of particles
var radius = 1; // particle radius
var gensoku = 0.85; // 减速度, 阻力

function setup() { // initialization
    myCanvas = createCanvas(1280, 800);

    // initial Arduino
    serial = new p5.SerialPort();    // make a new instance of the serialport library
    serial.open("/dev/cu.usbmodem142101");

    noStroke();
    fill(0);
    ellipseMode(RADIUS);
    background(0);
    blendMode(ADD); // 渲染模式，叠加

    for (var i = 0; i < num; i++) { // initial all particles
        x[i] = random(width);
        y[i] = random(height);
        x2[i] = x[i]; // record origin position
        y2[i] = y[i];
        vx[i] = 0;
        vy[i] = 0;
        ax[i] = 0;
        ay[i] = 0;
    }
    initKinectron();
}

function draw() {
    fill(0, 0, 0);
    rect(0, 0, width, height);
    var curX = 0; // hand position in x
    var curY = 0;
    if (handRight != null) { // already get info
        curX = handRight.depthX * myCanvas.width;
        curY = handRight.depthY * myCanvas.height;
    }
    // for debug TODELETE
    // curX = mouseX;
    // curY = mouseY;

    // 4 quad
    if (curX < width / 2 && curY < height / 2) {   // left Top : quad 1
        serial.write(0);
        console.log("1");
    } else if (curX >= width / 2 && curY < height / 2) { // rigth top : quad 2
        serial.write(1);
        console.log("2");
    } else if (curX < width / 2 && curY >= height / 2) { // left bottom : quad 3
        serial.write(1);
        console.log("3");
    } else { // right bottom : quad 4
        serial.write(0);
        console.log("4");
    }
    for (var i = 0; i < num; i++) { // traverse all particles
        var distance = dist(curX, curY, x[i], y[i]); // distance of cur & target
        // speed has anti-linear relationship with diatance
        if (curX == 0 && curY == 0) distance = 0; // didn't get hand info
        if (distance > 1) { // update acceleration with distance
            ax[i] = magnetism * (curX - x[i]) / (distance * distance);
            ay[i] = magnetism * (curY - y[i]) / (distance * distance);
        }
        vx[i] += ax[i]; // velocity + acceleration
        vy[i] += ay[i];

        vx[i] = vx[i] * gensoku; // 阻力
        vy[i] = vy[i] * gensoku;

        x[i] += vx[i];  // position += velocity
        y[i] += vy[i];

        var sokudo = dist(0, 0, vx[i], vy[i]); // 速度のX,Y成分から速度を求める
        var r = map(sokudo, 0, 10, 0, 255); //速度に応じた色を計算
        var g = map(sokudo, 0, 10, 64, 255);
        var b = map(sokudo, 0, 5, 128, 255);
        fill(r, g, b, 8);
        ellipse(x[i], y[i], radius, radius); // current particle
        fill(r, g, b, 1);
        ellipse(x2[i], y2[i], radius, radius); // origin particle
    }
}

function initKinectron() {
    // Define and create an instance of kinectron
    kinectron = new Kinectron(kinectronIpAddress);
    // Connect with application over peer
    kinectron.makeConnection();
    kinectron.startTrackedBodies(bodyTracked);
}

function bodyTracked(body) {
    console.log("pig"); // check if connected
    handRight = body.joints[kinectron.HANDRIGHT];
}
