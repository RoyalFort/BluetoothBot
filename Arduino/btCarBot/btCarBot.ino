#include <SoftwareSerial.h>
 
SoftwareSerial mySerial(2, 3); // RX, TX

const int leftFwd = 9; // green
const int leftRev = 10; // blue
const int rightFwd = 5; // grey
const int rightRev = 6; // purple

boolean ran = false;
byte velocity = 255;

void setup()  
{
  // Open serial communications and wait for port to open:
  Serial.begin(57600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }

  Serial.println("Start your engines!");

  // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);
  mySerial.println("Hello, world?");
}

void loop() // run over and over
{
  byte b = 0;
  if (mySerial.available())
    b = mySerial.read();
  
  if (b != 0)
    Serial.write(b);
  else
    return;

  // Not connected right now
  /*if (Serial.available())
    mySerial.write(Serial.read());*/
  
  int commandDelay = 20;
  switch (b)
  {
    case 'F':
    case 'w':
      Serial.println("Forward");
      analogWrite(leftFwd, velocity);
      analogWrite(rightFwd, velocity);
      delay(commandDelay);
      analogWrite(leftFwd, 0);
      analogWrite(rightFwd, 0);
      break;
    case 'B':
    case 's':
      Serial.println("Backward");
      analogWrite(leftRev, velocity);
      analogWrite(rightRev, velocity);
      delay(commandDelay);
      analogWrite(leftRev, 0);
      analogWrite(rightRev, 0);
      break;
    case 'L':
    case 'a':
      Serial.println("Left");
      analogWrite(leftRev, velocity);
      analogWrite(rightFwd, velocity);
      delay(commandDelay);
      analogWrite(leftRev, 0);
      analogWrite(rightFwd, 0);
      break;
    case 'R':
    case 'd':
      Serial.println("Right");
      analogWrite(leftFwd, velocity);
      analogWrite(rightRev, velocity);
      delay(commandDelay);
      analogWrite(leftFwd, 0);
      analogWrite(rightRev, 0);
      break;
    default:
      Serial.print("Set speed: ");
      Serial.println(b);
      if (b > 0 && b < 255)
        velocity = b;
      break;
  }
}

