package com.github.johnathana.elm327.screens;

import com.github.johnathana.elm327.BluetoothRFComm;
import com.github.johnathana.elm327.RMSUtils;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import java.io.IOException;

/**
 * Created by johnathana on 6/2/16.
 */
public class BluetoothDiscovery extends List implements CommandListener, DiscoveryListener {

    private MIDlet miDlet;
    private Displayable prevMenu;

    private final Command exit = new Command("Exit", Command.EXIT, 0);
    private final Command refresh = new Command("Refresh", Command.SCREEN, 1);

    public BluetoothDiscovery(MIDlet miDlet, Displayable prevMenu) {
        super("List of devices", List.IMPLICIT);
        this.miDlet = miDlet;
        this.prevMenu = prevMenu;

        this.addCommand(exit);
        this.addCommand(refresh);
        this.setCommandListener(this);
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == List.SELECT_COMMAND) {
            String selection = this.getString(this.getSelectedIndex());
            String macAddress = selection.substring(0, selection.indexOf(' ')).trim();

            RMSUtils.writeOBDMac(macAddress);
            BluetoothRFComm.getInstance().connect(macAddress);

            Display.getDisplay(miDlet).setCurrent(prevMenu);

        } else if (command == refresh) {
            this.deleteAll();
            startScan();

        } else if (command == exit) {
            Display.getDisplay(miDlet).setCurrent(prevMenu);
        }
    }

    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {

        try {
            String deviceAddress = remoteDevice.getBluetoothAddress() + " " + remoteDevice.getFriendlyName(true);
            this.insert(0, deviceAddress, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

    }

    public void serviceSearchCompleted(int i, int i1) {

    }

    public void inquiryCompleted(int discType) {
        Alert dialog;
        if (discType != DiscoveryListener.INQUIRY_COMPLETED) {
            dialog = new Alert("Bluetooth Error", "The inquiry failed to complete normally", null, AlertType.ERROR);
        } else {
            dialog = new Alert("Inquiry Completed", "The inquiry completed normally", null, AlertType.INFO);
        }
        dialog.setTimeout(1000);
        Display.getDisplay(miDlet).setCurrent(dialog);
    }

    private void startScan() {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();

            agent.cancelInquiry(this);
            agent.startInquiry(DiscoveryAgent.GIAC, this);

        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }
    }
}
