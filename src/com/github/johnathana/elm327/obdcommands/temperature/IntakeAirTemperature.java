package com.github.johnathana.elm327.obdcommands.temperature;

import com.github.johnathana.elm327.obdcommands.ObdCommand;
import com.github.johnathana.elm327.obdcommands.ObdUtils;

/**
 * Created by johnathana on 5/19/16.
 */
public class IntakeAirTemperature extends ObdCommand {

    public IntakeAirTemperature() {
        super("010F");
    }

    public String getName() {
        return "Intake air temperature";
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
