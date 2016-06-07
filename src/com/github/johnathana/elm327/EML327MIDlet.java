/*
 *
 * Copyright (c) 2007, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.johnathana.elm327;

import com.github.johnathana.elm327.screens.BluetoothDiscovery;
import com.github.johnathana.elm327.screens.OBDCommandMenu;
import com.github.johnathana.elm327.screens.TerminalForm;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;


public class EML327MIDlet extends MIDlet implements CommandListener {

    private Command exitCommand = new Command("Exit", Command.EXIT, 1);

    private List mainForm;
    private List obdMenu;
    private Form terminal;
    private List scanForm;

    public EML327MIDlet() {
        mainForm = new List("EML327", List.IMPLICIT);

        mainForm.append("Scan adapter", null);
        mainForm.append("OBD menu", null);
        mainForm.append("Terminal", null);
        mainForm.append("Monitor mode", null);

        mainForm.addCommand(exitCommand);
        mainForm.setCommandListener((CommandListener) this);

        obdMenu = new OBDCommandMenu(this, mainForm);
        terminal = new TerminalForm(this, mainForm);
        scanForm = new BluetoothDiscovery(this, mainForm);
    }

    protected void startApp() {
        String macAddress = RMSUtils.readOBDMac();
        if (macAddress == null) {
            Display.getDisplay(this).setCurrent(scanForm);
        } else {
            BluetoothRFComm.getInstance().connect(macAddress);
            Display.getDisplay(this).setCurrent(mainForm);
        }
    }

    public void commandAction(Command c, Displayable s) {
        if (c == List.SELECT_COMMAND) {
            switch (mainForm.getSelectedIndex()) {
                case 0:
                    Display.getDisplay(this).setCurrent(scanForm);
                    break;
                case 1:
                    Display.getDisplay(this).setCurrent(obdMenu);
                    break;
                case 2:
                    Display.getDisplay(this).setCurrent(terminal);
                    break;
                case 3:
                    BluetoothRFComm.getInstance().startMonitor();
                    break;
            }
        } else if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }
    }

    protected void destroyApp(boolean unconditional) {
    }

    protected void pauseApp() {
    }
}
