package com.github.johnathana.elm327;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * Created by johnathana on 6/7/16.
 */
public class RMSUtils {

    private final static String REC_STORE = "OBD_MAC";


    public static void writeOBDMac(final String mac) {
        try {
            RecordStore rs = RecordStore.openRecordStore(REC_STORE, true);
            byte[] rec = mac.getBytes();

            if (rs.getNumRecords() > 0) {
                rs.setRecord(1, rec, 0, rec.length);
            } else {
                rs.addRecord(rec, 0, rec.length);
            }

            rs.closeRecordStore();

        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    public static String readOBDMac() {

        String mac = null;

        try {
            RecordStore rs = RecordStore.openRecordStore(REC_STORE, false);

            byte b[]= rs.getRecord(1);
            mac = new String(b);
            rs.closeRecordStore();

        } catch (RecordStoreException e) {
            e.printStackTrace();
        }

        return mac;
    }
}
