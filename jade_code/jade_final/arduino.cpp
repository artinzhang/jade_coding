const int relay_pin = 8;
int last = -1; // last input, make sure different form input

void setup() {
    Serial.begin(9600);         // initialize serial communication
    pinMode(relay_pin, OUTPUT); // initialize the LED pin as an output
}

void loop() {
    // Serial.print("pigpig");
    if (Serial.available()) {                           // see if there's incoming serial data
        int in = Serial.read(); // from p5 : only 0 or 1
        Serial.print(in);
        if (in != last) {
            if (in == 0) {                                  // right
                digitalWrite(relay_pin, HIGH); // link
                delay(100);                    // sleep for 1 second
                last = in;                     // lastInput = current Input
            } else { /* left */
                digitalWrite(relay_pin, LOW);
                delay(100);
                last = in;
            }
        }
    } else { // can't catch data
        Serial.print("pig");
    }
}
