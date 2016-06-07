package com.github.johnathana.elm327.screens;

import com.github.johnathana.elm327.obdcommands.engine.RpmCommand;
import com.github.johnathana.elm327.obdcommands.fuel.FuelTankLevelCommand;
import com.github.johnathana.elm327.obdcommands.ObdCommand;
import com.github.johnathana.elm327.obdcommands.engine.RuntimeCommand;
import com.github.johnathana.elm327.obdcommands.temperature.AmbientAirTemperature;
import com.github.johnathana.elm327.obdcommands.temperature.EngineCoolantTemperature;
import com.github.johnathana.elm327.obdcommands.temperature.IntakeAirTemperature;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import java.util.Vector;

/**
 * Created by johnathana on 5/19/16.
 */
public class OBDCommandMenu extends List implements CommandListener {

    private MIDlet miDlet;
    private Displayable prevMenu;

    private static Vector obdCmdVector = new Vector();

    static {
        obdCmdVector.addElement(new EngineCoolantTemperature());
        obdCmdVector.addElement(new AmbientAirTemperature());
        obdCmdVector.addElement(new IntakeAirTemperature());
        obdCmdVector.addElement(new FuelTankLevelCommand());
        obdCmdVector.addElement(new RuntimeCommand());
        obdCmdVector.addElement(new RpmCommand());
    }

    public OBDCommandMenu(MIDlet miDlet, Displayable prevMenu) {
        super("OBD menu", List.IMPLICIT);
        this.miDlet = miDlet;
        this.prevMenu = prevMenu;

        for (int i = 0; i < obdCmdVector.size(); i++) {
            this.append(((ObdCommand)obdCmdVector.elementAt(i)).getName(), null);
        }

        this.addCommand(new Command("Back", Command.BACK, 1));
        this.setCommandListener((CommandListener)this);
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == List.SELECT_COMMAND) {
            String selection = this.getString(this.getSelectedIndex());
            ObdCommand obdCommand = (ObdCommand) obdCmdVector.elementAt(this.getSelectedIndex());
            Alert alert = new Alert(selection, obdCommand.getFormattedResults(), null, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            Display.getDisplay(miDlet).setCurrent(alert);
        } else {
            Display.getDisplay(miDlet).setCurrent(prevMenu);
        }
    }
}
