#include <LiquidCrystal.h> // library for LCD

// initialize the library with the numbers of the interface pins 
LiquidCrystal lcd(13, 12, 11, 10, 9, 8);

// Measuring Voltage
#include "EmonLib.h"   // Include Emon Library
EnergyMonitor emon1;   // Create an instance

const int relayPin1 = 2;
const int relayPin2 = 3; 
const int relayPin3 = 4;

// Measuring Current Using ACS712
const int Sensor_Pin = A1; // Connect current sensor with A1 of Arduino
const int sensitivity = 100; // use 185 for 5A Module
const int offsetvoltage = 2500; // This is the voltage when no current is flowing

char valueStr[10]; 

float Energy = 0; // Initialize energy variable

void setup()
{
  pinMode(relayPin1, OUTPUT);
  pinMode(relayPin2, OUTPUT);
  pinMode(relayPin3, OUTPUT);

  digitalWrite(relayPin1, LOW); // Вимикаємо всі реле на початку
  digitalWrite(relayPin2, LOW);
  digitalWrite(relayPin3, LOW);

  Serial.begin(9600);
  emon1.voltage(A0, 265, 1.7); // Voltage: input pin, calibration, phase_shift
  // calibration for 220V system, adjust if necessary

  lcd.begin(20, 4); // set up the LCD's number of columns and rows:
  lcd.setCursor(0, 0);
  lcd.print("  THE BRIGHT LIGHT    ");
  lcd.setCursor(0, 1);
  lcd.print("  AC ENERGY METER     ");
}

void loop()
{
  //************************* Measure Voltage ******************************************
  emon1.calcVI(100, 2000); // Calculate all. No.of half wavelengths (crossings), time-out
  float Voltage = emon1.Vrms;  // extract Vrms into Variable

  lcd.setCursor(0, 2);
  lcd.print("V = ");
  lcd.print(Voltage, 2);
  lcd.print("V  ");

  //************************* Measure Current ******************************************
  unsigned int temp = 0;
  float maxpoint = 0;
  for (int i = 0; i < 500; i++)
  {
    temp = analogRead(Sensor_Pin);
    if (temp > maxpoint)
    {
      maxpoint = temp;
    }
  }
  float ADCvalue = maxpoint; 
  double eVoltage = (ADCvalue / 1024.0) * 5000; // Gets you mV
  double Current = ((eVoltage - offsetvoltage) / sensitivity);
  double AC_Current = (Current) / (sqrt(2));

  lcd.print("I = ");
  lcd.print(AC_Current, 2);
  lcd.print("A          "); // unit for the current to be measured

  //************************* Measure Power ******************************************
  float Power = Voltage * AC_Current;

  //************************* Measure Energy ******************************************
  static unsigned long lastTime = 0;
  unsigned long currentTime = millis();
  float elapsedTime = (currentTime - lastTime) / 1000.0; // convert milliseconds to seconds
  
  Energy += (Power * elapsedTime) / 3600.0; // Watt-sec is converted to Watt-Hr by dividing by 3600
  lastTime = currentTime;

  lcd.setCursor(0, 4);
  lcd.print("E = ");
  lcd.print(Energy, 1);
  lcd.print("Wh    "); // unit for the energy to be measured
// Обробка серійного вводу
 if (Serial.available() > 0) {
    String command = Serial.readStringUntil('\n');
    if (command.equals("GET_ENERGY")) {
      Serial.print(Energy);
      Serial.print("\n");
    } else if (command.equals("TURN_ON_RELAY1")) {
      digitalWrite(relayPin1, HIGH); // Включаємо перше реле
    } else if (command.equals("TURN_OFF_RELAY1")) {
      digitalWrite(relayPin1, LOW); // Вимикаємо перше реле
    } else if (command.equals("TURN_ON_RELAY2")) {
      digitalWrite(relayPin2, HIGH); // Включаємо друге реле
    } else if (command.equals("TURN_OFF_RELAY2")) {
      digitalWrite(relayPin2, LOW); // Вимикаємо друге реле
    } else if (command.equals("TURN_ON_RELAY3")) {
      digitalWrite(relayPin3, HIGH); // Включаємо третє реле
    } else if (command.equals("TURN_OFF_RELAY3")) {
      digitalWrite(relayPin3, LOW); // Вимикаємо третє реле
    } else if (command.equals("RESET_ENERGY")) {
      Energy = 0;
    }
  }
}
