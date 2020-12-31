// By: Ahmed mahmoud mohamed El-sayed
// Seat No: 5051

package ComputerToArduino;

//importing the program libraries

import java.util.logging.Level;          // A class that is used to perform a test on logging
import java.util.logging.Logger;         // logger object classis to perform a test on logging
import javafx.animation.FadeTransition;  // A class that 
import javafx.animation.KeyFrame;        // A class to define specifis values at a certained point in time
import javafx.animation.Timeline;        // A class that is used to define an animation for any written value
import javafx.application.Application;   // A class from which JavaFX applications extend.
import javafx.application.Platform;      // a class that is responsible for starting and ending our program
import javafx.beans.value.ChangeListener;// A class that is updated when listener is changed
import javafx.beans.value.ObservableValue;// A class that is notified whene the value of an ObservableValue changes.
import javafx.collections.FXCollections; // Utility class that consists of static methods 
import javafx.collections.ObservableList;// A class that Creates and returns a typesafe wrapper on top of provided observable list.
import javafx.event.Event;               // A class which tell when an event takes place 
import javafx.event.EventHandler;        // A class which we use to can handle our stuff
import javafx.geometry.Insets;           // A class which give the capability to set spaces between our control objects
import javafx.geometry.Pos;              // A class that manage us to edit the position of controls on the the program
import javafx.scene.Scene;               // the class which we can use as a container for our all controls 
import javafx.scene.control.*;           // class for all user interface controls like buttons,labels and etc..
import javafx.scene.layout.HBox;         // A class in which we can add our program controls like buttons,texts and..etc in a horizontal format
import javafx.scene.layout.VBox;         // A class in which we can add our program controls like buttons,texts and..etc in a vertical format
import javafx.scene.paint.Color;         // A class that is used to add colors to text,label..etc
import javafx.scene.text.Font;           // A class that manage us to edit fonts  
import javafx.scene.text.Text;           // A class which contain text to our program
import javafx.stage.Stage;               // A class which contain our program controls
import javafx.util.Duration;             // a class which add a duration like delay 
import jssc.*;                           // A library that provide all information for serial communication    
import static jssc.SerialPort.MASK_RXCHAR;// A class which is used to set connection parameters 


public class ComputerToArduino extends Application {                   // the class that contain our program and all design of gui
    

    //Declaring the gui components 
    TextField lcd_text,ldr_text;
    SerialPort arduino_Port;      // setting up serial connection between arduino
    CheckBox box1,box2,box3,box4;         // whe have four check boxes
    Timeline time;                      // declaring a timeline to deal with time
    Label led_text;
    ObservableList<String> listPort;    // adding observer to check the actions 
    Label Com_msg;
  
    private void searchingForPort(){    //creating searching for port to connect function 
        listPort = FXCollections.observableArrayList(); // add result of searching to array of strings cotain all port names
        String[] foundedPorts;          //storing ports name in a string array 
        foundedPorts = SerialPortList.getPortNames();  // add founded ports to the an array 
        for(String name: foundedPorts){ //starting searching for the port
            listPort.add(name);           //Adding searching result to portList   
       }
    }
    @Override
    public void start(Stage firstStage) throws Exception {       
        searchingForPort(); // start searching for the port
        ComboBox combo_ports;  
        combo_ports = new ComboBox(listPort);   //adding result for searching to the comboBox list
        combo_ports.valueProperty().addListener(new ChangeListener<String>() {     // search for new ports to add to combobox
            @Override
            public void changed(ObservableValue<? extends String> observable,  // a function to notify when the value of the port is changed
                    String oldValue, String newValue) {
                connectToArduino(newValue);                                    // sending the new port value to the list 
                Com_msg.setText("Successfully connected\nto "+ newValue);       // tell the user that the connection doe successfully
            }
        });    
        
        //program  gui components 
        box1 = new CheckBox("LED1");                     // setting names of our four check boxes
        box2 = new CheckBox("LED2 (Alert)");
        box3 = new CheckBox("LED3");
        box4 = new CheckBox("ALL LEDs");
      
        Text serialtext = new Text("Port conection");    // the serial connection label and its style 
        serialtext.setFill(Color.BLUE);
        serialtext.setFont(new Font("Arial",20));
        
        Text ldrlabel = new Text("LDR Reading");         // the LDR Reading label and its style 
        ldrlabel.setFill(Color.BLUE);
        ldrlabel.setFont(new Font("Arial",20));
        
        Text ledlabel = new Text("LEDs Control");         // leds control label and its style         
        ledlabel.setFill(Color.BLUE);
        ledlabel.setFont(new Font("Arial",20));
        
        Text lcdlabel = new Text("LCD Display ");          // lcd display label and its style  
        lcdlabel.setFill(Color.BLUE);
        lcdlabel.setFont(new Font("Arial",20));
        
        Text msglabel = new Text("LEDs status");          // leds status label and its style 
        msglabel.setFill(Color.BLUE);
        msglabel.setFont(new Font("Arial",16));
        
        Text serialmsg = new Text("Connection Status");   // connection status label and its style 
        serialmsg.setFill(Color.BLUE);
        serialmsg.setFont(new Font("Arial",16));
        
        Text weltext = new Text("Welcome to L_Niwehy project");  // welcome message and its style 
        weltext.setFill(Color.RED);
        weltext.setFont(new Font("Arial",15));
       
        // creating gui components
        lcd_text = new TextField();                 
        ldr_text = new TextField();
        ldr_text.setPrefColumnCount(5);             // set distance between text
        ldr_text.setEditable(false);                // close editting text
        led_text = new Label();                     // labels to define each component
        Com_msg = new Label();
        
    
        Button writebutton = new Button("Send");
        Button exitbutton = new Button("Exit");    // exit button from the program
        
       
       exitbutton.setOnAction(e -> {                   // Exit button handler to exit from the program
           if(arduino_Port != null)                     // check if arduino port is connected or not                   
            try {
                arduino_Port.writeString("90");        // write string data to arduino port serial
            } catch (SerialPortException ex) {        // serial exception is to handle the unexcepected events that occur when a program is being executed
                Logger.getLogger(ComputerToArduino.class.getName()).log(Level.SEVERE, null, ex);  // logger object to create java logging program and perform a test on logging
            }
           Platform.exit();                           // Exit from the program
        });
        
        
                                            
         EventHandler h6 = new EventHandler() {        // handling the fade of text on gui
            @Override
            public void handle(Event event) {
                FadeTransition fade = new FadeTransition(Duration.millis(2500), weltext);  // set delay for the display
                fade.setFromValue(1);                                            // start from displayed value
                fade.setToValue(0);                                              // end with fade 
                fade.setCycleCount(Timeline.INDEFINITE);                         // set infinite iteration of fade 
                fade.setAutoReverse(true);                                       // smoothly display and disapper
                fade.play();
            }
        };
  
        time = new Timeline();                                      // setting timeline for message display 
        time.getKeyFrames().add(new KeyFrame(Duration.millis(2500),h6));  // adding delay to the text
        time.play();                                                    // lauch timeline to start

        //Setting actions for check boxes and writebutton

       box1.setOnAction(led1_handler);                            // Setting actions for check boxes 
       box2.setOnAction(led2_handler);
       box3.setOnAction(led3_handler);
       box4.setOnAction(all_handler);
       writebutton.setOnAction(writebuttonhandler);              // write button handle action
       
        //Stacking the components on Layouts
        VBox layout1;                               
        layout1 = new VBox(20);
        layout1.setPadding(new Insets(20, 20, 20, 20));
        
        VBox layout0;                                 
        layout0 = new VBox(20);
        layout0.setPadding(new Insets(20, 20, 20, 20));
        
        VBox layout2;
        layout2 = new VBox(20);
        layout2.setPadding(new Insets(20, 20, 20, 20));

        VBox layout3;
        layout3 = new VBox(20);
        
        layout3.setPadding(new Insets(20, 20, 20, 20));
        HBox layout4;         
        layout4 = new HBox(20);
        
        VBox layout5;                                                   // set vertical box to add cotrols on it 
        layout5 = new VBox(20);
        layout5.setAlignment(Pos.CENTER);                                            // set position between layouts
       
        layout1.getChildren().addAll(serialtext,combo_ports,serialmsg,Com_msg);   // the first line in the gui 
        layout0.getChildren().addAll(ldrlabel,ldr_text);                            // second line in gui 
        layout2.getChildren().addAll(ledlabel,box1, box2,box3,box4,msglabel,led_text); // third line in the gui 
        layout3.getChildren().addAll(lcdlabel,lcd_text,writebutton);                   // lcd line 
        layout4.getChildren().addAll(layout1,layout0,layout2,layout3,exitbutton);     // adding all lines to the same layout
        layout5.getChildren().addAll(layout4,weltext);
        
        Scene scene = new Scene(layout5, 900, 360);                                 // set size of the scene
        firstStage.setTitle("Computer to Arduino");                                // Set the name or heder of the program
        firstStage.setScene(scene);                                                // set all layouts to the program stage
        firstStage.setResizable(false);                                           // closing the maximizing control of the program
        firstStage.show();                                                        // launching the stage
    }
    
    //Connecting to the Arduino function
     public boolean connectToArduino(String port){
        boolean pass = false;
        SerialPort SP;
        SP = new SerialPort(port);                                               // creating a serial port 
        try {
            SP.openPort();                                                       // open the serial port
            SP.setParams(                                                        //defining serial connectin parameters
                    
                    SerialPort.BAUDRATE_9600,                                    // baud rate parameter
                    SerialPort.DATABITS_8,                                       // rate of bit exchange
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            SP.setEventsMask(MASK_RXCHAR);
            SP.addEventListener((SerialPortEvent serialPortEvent) ->         // add event listener to the serial port to detect any action
            {            
                if(serialPortEvent.isRXCHAR()){
                    try {
                        byte[] b = SP.readBytes();                   // read data in byte
                        int value = b[0] & 0xff;                     //convert to integer
                        String st = String.valueOf(value);           // store read data into string 
                        Platform.runLater(() -> {                  // detect when program start
                            ldr_text.setText(st); //Showing LDR result 
                        });
                        
                    } catch (SerialPortException ex) {        // serial exception is to handle the unexcepected events that occur when a program is being executed
                        Logger.getLogger(ComputerToArduino.class.getName())    
                                .log(Level.SEVERE, null, ex);             // logger object to perform a test on logging
                  }   
               }
            });
            
            arduino_Port = SP;
            pass = true;
            
        } catch (SerialPortException ex) {                   // serial exception is to handle the unexcepected events that occur when a program is being executed
            Logger.getLogger(ComputerToArduino.class.getName())
                    .log(Level.SEVERE, null, ex);                     // logger object to perform a test on logging
            System.out.println("SerialPortException: " 
                    + ex.toString());
        }
        return pass;
     }
     //Disconnecting the Arduino function
       public void disconnect_Arduino(){
        if(arduino_Port != null){    // checking if there is no port to connect 
            try {
                arduino_Port.removeEventListener();    //stop connction with port            
                if(arduino_Port.isOpened()){           // check if arduino port is connected or not
                    arduino_Port.closePort();           // closing arduino port
                }
            } catch (SerialPortException ex) {             // serial exception is to handle the unexcepected events that occur when a program is being executed
                Logger.getLogger(ComputerToArduino.class.getName())
                        .log(Level.SEVERE, null, ex);           // logger object to perform a test on logging
         }
      }
    } 
    @Override
    public void stop() throws Exception {
        disconnect_Arduino();                      // call disconnectArduino function to disconnect the Arduino
        super.stop();
    } 
   
    
        EventHandler writebuttonhandler = new EventHandler() {        // controlling sending data to lcd
            @Override
            public void handle(Event event) {                         // handle event when click on write button
                 try {
            if(arduino_Port != null)                                   // check if arduino port is connected or not
            arduino_Port.writeString(lcd_text.getText());               // set text to lcd from text in the gui
            
            else
                Com_msg.setText("Select port first!");                  // inform the user to select a port to launch the program
        } catch (SerialPortException ex) {                             // serial exception is to handle the unexcepected events that occur when a program is being executed
            Logger.getLogger(ComputerToArduino.class.getName())
                    .log(Level.SEVERE, null, ex);       // logger object to perform a test on logging
               } 
            }
        };

        EventHandler led1_handler = new EventHandler() {      // handle event of choosing check box of led1
            @Override
            public void handle(Event event) {
              try {  
            if(box1.isSelected()){
                if(arduino_Port != null){      //if there is connection port
                    arduino_Port.writeString("10@");  //send string data to Arduino serial 
                    led_text.setText("LED1 is ON");
                }else{
                    Com_msg.setText("Select port first!");      // inform the user to select a port to launch the program
                }
            }else {
                if(arduino_Port != null){     //if there is no connection port
                    arduino_Port.writeString("20#");  //send different data
                    led_text.setText("LED1 is OFF");
                }else{
                    Com_msg.setText("Select port first!");
                }
            }
        } catch (SerialPortException ex) {                     // serial exception is to handle the unexcepected events that occur when a program is being executed
            Logger.getLogger(ComputerToArduino.class.getName())
                    .log(Level.SEVERE, null, ex);              // logger object to perform a test on logging
              }
            }
        };

        EventHandler led2_handler = new EventHandler() {         // handle event of choosing check box of led2
            @Override
            public void handle(Event event) {
              try {  
            if(box2.isSelected()){                             // check if check box 2 is selected or not 
                if(arduino_Port != null){                       // check if there is connection port    
                    arduino_Port.writeString("30$");
                    led_text.setText("LED2 is ON");
                }else{
                     Com_msg.setText("Select port first!");
                }
            }else {
                if(arduino_Port != null){
                    arduino_Port.writeString("40%");           // send string data to to the arduino
                    led_text.setText("LED2 is OFF");
                }else{
                     Com_msg.setText("Select port first!");    // inform the user to choose a port to connect 
                }
            }
        } catch (SerialPortException ex) {                      // serial exception is to handle the unexcepected events that occur when a program is being executed
            Logger.getLogger(ComputerToArduino.class.getName())
                    .log(Level.SEVERE, null, ex);                // logger object to perform a test on logging
               }
            }
        };

        EventHandler led3_handler = new EventHandler() {           // handle the event of selecting box3
           // @Override
            public void handle(Event event) { 
            try {  
            if(box3.isSelected()){                                // check if the box is selected or not
                if(arduino_Port != null){                          // check if the port is connected or not 
                    arduino_Port.writeString("50^");               // send the data that turn on led3
                    led_text.setText("LED3 is ON");                // inform the user that the led is on 
                }else{
                     Com_msg.setText("Select port first!");         // tell the user to select a port to connect 
                }
            }else {
                if(arduino_Port != null){
                    arduino_Port.writeString("60&");                 // send the data that turn off led3   
                    led_text.setText("LED3 is OFF");
                }else{
                     Com_msg.setText("Select port first!");          // tell the user to select a port to connect
                }
            }
        } catch (SerialPortException ex) {                          // serial exception is to handle the unexcepected events that may occur when a program is being executed
            Logger.getLogger(ComputerToArduino.class.getName())
                    .log(Level.SEVERE, null, ex);                    // logger object to perform a test on logging
            }
          }
        };

        EventHandler all_handler = new EventHandler() {             // handle the event of choosing check box 4 
            @Override
            public void handle(Event event) { 
            try {  
            if(box4.isSelected()){                                 // check if box4 is selected or not
                if(arduino_Port != null){                          // check if port is selected or not
                    arduino_Port.writeString("70");                // send string data to arduino serial if the port is connected and box is selected
                    led_text.setText("All LEDs are ON");           // inform that the all leds are on
                }else{
                     Com_msg.setText("Select port first!");       // tell the user to connect to a port
                }
            }else {
                    box1.setSelected(false);                   // turn of all leds when box4 is unselected
                    box2.setSelected(false);
                    box3.setSelected(false);
                if(arduino_Port != null){
                    arduino_Port.writeString("80");            // send data that turn all leds off to the arduino
                    led_text.setText("All LEDs are OFF");
                }else{
                     Com_msg.setText("Select port first!");
                 }
             }
        } catch (SerialPortException ex) {                   // serial exception is to handle the unexcepected events that may happen when a program is being executed
            Logger.getLogger(ComputerToArduino.class.getName())
                    .log(Level.SEVERE, null, ex);             // logger object to perform a test on logging
            }
          }
        };

//Entry point to the program
public static void main(String[] args) {
        launch(args);  //make program go to execute Application class first
    }

}
  