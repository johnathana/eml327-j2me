package com.github.johnathana.elm327.obdcommands.temperature;

import com.github.johnathana.elm327.obdcommands.ObdCommand;
import com.github.johnathana.elm327.obdcommands.ObdUtils;

/**
 * Created by johnathana on 5/19/16.
 */
public class EngineCoolantTemperature extends ObdCommand {

    public EngineCoolantTemperature() {
        super("0105");
    }

    public String getName() {
        return "Engine coolant temperature";
    }

    public String getFormattedResults() {
        String rawData = getResults();
        try {
            return ObdUtils.parseTemp(rawData);
        } catch (Exception e) {
            return rawData;
        }
    }

}
