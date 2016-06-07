package com.github.johnathana.elm327.obdcommands;

import com.github.johnathana.elm327.BluetoothRFComm;

/**
 * Created by johnathana on 5/19/16.
 */
public abstract class ObdCommand {

    private String command;

    public ObdCommand(final String command) {
        this.command = command;
    }

    public abstract String getName();

    public String getResults() {
        String results;
        try {
            results = BluetoothRFComm.getInstance().executeCommand(command);
        } catch (Exception e) {
            results = e.getMessage();
        }
        return results;
    }

    public abstract String getFormattedResults();
}
