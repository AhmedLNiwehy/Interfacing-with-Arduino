#include <LiquidCrystal.h> 
String stringVal;
int led1 = 2;
int led2 = 3;
int led3 = 4;
int buz = 12;
int ldrpin=A0;
int ldrVal=0;

// initialize the library by associating any needed LCD interface pin
// with the arduino pin number it is connected to
const int rs = 6, en = 7, d4 = 8, d5 = 9, d6 = 10, d7 = 11;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

void setup() {
  Serial.begin(9600);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);
  pinMode(buz,OUTPUT);
  
  lcd.begin(16, 2);
  lcd.print("Text me ");
}

void loop() {

  if (Serial.available() > 0) {
    stringVal = Serial.readString();      //reading serial data as string
    if (stringVal == "10@" || stringVal == "20#" || stringVal == "30$"
        || stringVal == "40%" ||  stringVal == "50^" || stringVal == "60&" 
        || stringVal == "70"  || stringVal == "80" || stringVal == "90")
    {
     int newVal = stringVal.toInt(); 
      switch (newVal) {
        case 10:
          digitalWrite(led1, HIGH);
          break;

        case 20:
          digitalWrite(led1, LOW);
          break;

        case 30:
          digitalWrite(led2, HIGH);
         for(int i=0;i<=2;i++){
          tone(buz,450);
          delay(250);
          noTone(buz);
          delay(90);}
          break;

        case 40:
          digitalWrite(led2, LOW);
          break;

        case 50:
          digitalWrite(led3, HIGH);
          break;

        case 60:
          digitalWrite(led3, LOW);
          break;

        case 70:
          digitalWrite(led1, HIGH);
          digitalWrite(led2, HIGH);
          digitalWrite(led3, HIGH);
          break;

        case 80:
          digitalWrite(led1, LOW);
          digitalWrite(led2, LOW);
          digitalWrite(led3, LOW);
          break;

        case 90:
          digitalWrite(led1, LOW);
          digitalWrite(led2, LOW);
          digitalWrite(led3, LOW);
          lcd.clear();
          lcd.setCursor(0, 0);
          break;  
      }
    }
    else {
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print( stringVal);
    }
  }
    ldrVal = analogRead(ldrpin); 
    Serial.write(ldrVal);
    delay(250);
  
}
