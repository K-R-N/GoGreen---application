package gui.session;

import gui.helpers.IterableEnumeration;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Class that gets the local MAC-address.
 */
public class LocalMac {

    /**
     * Returns the MAC-address (ethernet) from the machine which runs this code.
     * @return String containing the local MAC-address;
     */
    private static String getMac() {
        try {
            for (NetworkInterface network : IterableEnumeration.make(
                    NetworkInterface.getNetworkInterfaces())) {
                byte[] mac = network.getHardwareAddress();

                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            }
        } catch (SocketException e) {
            System.out.println("Couldn't determine an available MAC address");
        }

        return null;
    }

    /**
     * Produces key string from MAC-address.
     * @return String with size 16 bytes from an MAC-address.
     */
    protected static String macToKey() {
        String mac = getMac();
        String[] parts = mac.split("");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 17; i++) {
            if (i != 14) {
                sb.append(parts[i]);
            }
        }
        return sb.toString();

    }
}
