#include <SoftwareSerial.h>

SoftwareSerial mySerial(2, 3); // RX, TX

const int leftFwd = 9; // green
const int leftRev = 10; // blue
const int rightFwd = 5; // grey
const int rightRev = 6; // purple

boolean ran = false; // What is this for?
byte velocity = 255; // I assume this will be the default speed
int commandDelay = 20;

/* Ethan's config idea:
  By pressing the "c" key, you leave drive(d) mode enter configuration(c) mode:
  Once in (c) mode, you can press the following key to change a corresponding option:
	- v for changing velocity
	- Add more config options later if we need to
  Followed by pressing the "d" key (for "done"), which would put you back into drive(d) mode
  
  Maybe it's a bit confusing, but an android app could have a front-end to this, allowing the user
  to make a speed selection, and the app would output all these key codes automatically. (User would
  never know they left drive mode)
  
*/

// d = drive
// c = config
// o = option-set mode

char mode = 'd';

void setup() {
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

void loop() {
	
	byte read = 0;
	if (mySerial.available()) read = mySerial.read();
	
	switch (mode) {
		
		// Drive (d) mode by default
		case 'd': driveCommand(read); break;
		case 'c':
		case 'o': config(read) break;
		// If something weird happens and mode isn't a valid 
		// value, put everything back into drive mode
		default: mode = 'd'; 
		
	}

	switch (read) {

		default: {
			Serial.print("Set speed: ");
			if (read > 0 && read < 255) velocity = read;
		} break;
	}
}

void driveCommand(byte read) {
	switch(read) {
		case 'F':
		case 'w': goFwd(); break;
		case 'B':
		case 's': goBack(); break;
		case 'L':
		case 'a': goLeft(); break;
		case 'R':
		case 'd': goRight(); break;
	}
}

void goFwd() {
Serial.println("Forward");
analogWrite(leftFwd, velocity);
analogWrite(rightFwd, velocity);
delay(commandDelay);
analogWrite(leftFwd, 0);
analogWrite(rightFwd, 0);
}

void goBack() {
Serial.println("Backward");
analogWrite(leftRev, velocity);
analogWrite(rightRev, velocity);
delay(commandDelay);
analogWrite(leftRev, 0);
analogWrite(rightRev, 0);
}

void goLeft() {
Serial.println("Left");
analogWrite(leftRev, velocity);
analogWrite(rightFwd, velocity);
delay(commandDelay);
analogWrite(leftRev, 0);
analogWrite(rightFwd, 0);
}

void goRight() {
Serial.println("Right");
analogWrite(leftFwd, velocity);
analogWrite(rightRev, velocity);
delay(commandDelay);
analogWrite(leftFwd, 0);
analogWrite(rightRev, 0);
}