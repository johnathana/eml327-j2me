package com.github.johnathana.elm327.screens;

import com.github.johnathana.elm327.BluetoothRFComm;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import java.io.IOException;

/**
 * Created by johnathana on 5/19/16.
 */
public class TerminalForm extends Form implements CommandListener {

    private TextField commandField = new TextField("Command", "", 15, TextField.ANY);
    private TextField resultField = new TextField("Result", "", 160, TextField.ANY);

    private Command runCommand = new Command("Execute", Command.SCREEN, 0);

    private MIDlet miDlet;
    private Displayable prevMenu;

    public TerminalForm(MIDlet miDlet, Displayable prevMenu) {
        super("Terminal");

        this.miDlet = miDlet;
        this.prevMenu = prevMenu;

        this.append(commandField);
        this.append(resultField);

        this.addCommand(runCommand);
        this.addCommand(new Command("Back", Command.BACK, 1));
        this.setCommandListener(this);
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == runCommand) {
            try {
                String cmd = commandField.getString();
                String result = BluetoothRFComm.getInstance().executeCommand(cmd);
                resultField.setString(result);
                commandField.setString("");
            } catch (IOException e) {
                Alert alert = new Alert("Exception", e.getMessage(), null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                Display.getDisplay(miDlet).setCurrent(alert);
            }
        } else {
            Display.getDisplay(miDlet).setCurrent(prevMenu);
        }
    }
}
