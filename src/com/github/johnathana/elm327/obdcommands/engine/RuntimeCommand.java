package com.github.johnathana.elm327.obdcommands.engine;

import com.github.johnathana.elm327.obdcommands.ObdCommand;
import com.github.johnathana.elm327.obdcommands.ObdUtils;

/**
 * Created by johnathana on 5/20/16.
 */
public class RuntimeCommand extends ObdCommand {

    public RuntimeCommand() {
        super("011F");
    }

    public String getName() {
        return "Run time since engine start";
    }

    public String getFormattedResults() {
        String rawData = getResults();
        try {
            int seconds = ObdUtils.parseValue(rawData);
            return ObdUtils.formatSecondsAsDuration(seconds);
        } catch (Exception e) {
            return rawData;
        }
    }
}
