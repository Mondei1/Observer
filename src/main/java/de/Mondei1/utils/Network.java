package de.Mondei1.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Network {

    // Used code from https://stackoverflow.com/questions/11884696/how-to-get-machines-mac-address by gYanI. This works for Linux and Windows.
    public static String getMacAddress() throws UnknownHostException, SocketException {
        String command = "/sbin/ifconfig";
        String sOsName = System.getProperty("os.name");
        if (sOsName.startsWith("Windows")) {
            command = "ipconfig";
        } else {
            if ((sOsName.startsWith("Linux")) || (sOsName.startsWith("Mac"))
                    || (sOsName.startsWith("HP-UX"))) {
                command = "/sbin/ifconfig";
            } else {
                System.out.println("The current operating system '" + sOsName
                        + "' is not supported.");
            }
        }

        Pattern p = Pattern.compile("([a-fA-F0-9]{1,2}(-|:)){5}[a-fA-F0-9]{1,2}");
        try {
            Process pa = Runtime.getRuntime().exec(command);
            pa.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(pa.getInputStream()));
            String line;
            Matcher m;
            while ((line = reader.readLine()) != null) {

                m = p.matcher(line);

                if (!m.find())
                    continue;
                line = m.group();
                break;

            }
            return line;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
