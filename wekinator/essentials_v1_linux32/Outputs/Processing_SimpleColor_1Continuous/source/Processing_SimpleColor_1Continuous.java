import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import oscP5.*; 
import netP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Processing_SimpleColor_1Continuous extends PApplet {

//This demo allows wekinator to control background color (hue)
//This is a continuous value between 0 and 1

//Necessary for OSC communication with Wekinator:


OscP5 oscP5;
NetAddress dest;

//Parameters of sketch
float myHue;
PFont myFont;

public void setup() {
  //Initialize OSC communication
  oscP5 = new OscP5(this,12000); //listen for OSC messages on port 12000 (Wekinator default)
  dest = new NetAddress("127.0.0.1",6448); //send messages back to Wekinator on port 6448, localhost (this machine) (default)
  
  colorMode(HSB);
  size(400,400, P3D);
  smooth();
  background(255);

  //Initialize appearance
  myHue = 255;
  sendOscNames();
  myFont = createFont("Arial", 14);
}

public void draw() {
  background(myHue, 255, 255);
  drawtext();
}

//This is called automatically when OSC message is received
public void oscEvent(OscMessage theOscMessage) {
 if (theOscMessage.checkAddrPattern("/wek/outputs")==true) {
     if(theOscMessage.checkTypetag("f")) { // looking for 1 control value
        float receivedHue = theOscMessage.get(0).floatValue();
        myHue = map(receivedHue, 0, 1, 0, 255);
     } else {
        println("Error: unexpected OSC message received by Processing: ");
        theOscMessage.print();
      }
 }
}

//Sends current parameter (hue) to Wekinator
public void sendOscNames() {
  OscMessage msg = new OscMessage("/wekinator/control/setOutputNames");
  msg.add("hue"); //Now send all 5 names
  oscP5.send(msg, dest);
}

//Write instructions to screen.
public void drawtext() {
    stroke(0);
    textFont(myFont);
    textAlign(LEFT, TOP); 
    fill(0, 0, 255);
    text("Receiving 1 continuous parameter: hue, in range 0-1", 10, 10);
    text("Listening for /wek/outputs on port 12000", 10, 40);
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Processing_SimpleColor_1Continuous" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
