package com.github.johnathana.elm327.obdcommands.fuel;

import com.github.johnathana.elm327.obdcommands.ObdCommand;
import com.github.johnathana.elm327.obdcommands.ObdUtils;

/**
 * Created by johnathana on 5/20/16.
 */
public class FuelTankLevelCommand extends ObdCommand {

    public FuelTankLevelCommand() {
        super("012F");
    }

    public String getName() {
        return "Fuel tank level";
    }

    public String getFormattedResults() {
        String rawData = getResults();
        try {
            return ObdUtils.parsePercentage(rawData);
        } catch (Exception e) {
            return rawData;
        }
    }
}
