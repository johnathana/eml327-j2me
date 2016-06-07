package com.github.johnathana.elm327.obdcommands.engine;

import com.github.johnathana.elm327.obdcommands.ObdCommand;
import com.github.johnathana.elm327.obdcommands.ObdUtils;

/**
 * Created by johnathana on 5/20/16.
 */
public class RpmCommand extends ObdCommand {

    public RpmCommand() {
        super("010C");
    }

    public String getName() {
        return "Engine RPM";
    }

    public String getFormattedResults() {
        String rawData = getResults();
        try {
            return String.valueOf(ObdUtils.parseValue(rawData) / 4) + " rpm";
        } catch (Exception e) {
            return rawData;
        }
    }
}
