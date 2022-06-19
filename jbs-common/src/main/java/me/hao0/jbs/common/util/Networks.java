package me.hao0.jbs.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class Networks {


    public static String getHostName() {

        String name = null;
        try {
            Enumeration<NetworkInterface> infs = NetworkInterface.getNetworkInterfaces();
            while (infs.hasMoreElements() && (name == null)) {
                NetworkInterface net = infs.nextElement();
                if (net.isLoopback()) {
                    continue;
                }
                Enumeration<InetAddress> addr = net.getInetAddresses();
                while (addr.hasMoreElements()) {

                    InetAddress inet = addr.nextElement();

                    if (inet.isSiteLocalAddress()) {
                        name = inet.getHostAddress();
                    }

                    if (!inet.getCanonicalHostName().equalsIgnoreCase(inet.getHostAddress())) {
                        name = inet.getCanonicalHostName();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            name = "localhost";
        }
        return name;
    }


    public static String getSiteIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.isSiteLocalAddress()) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }


    public static Integer ip2Num(String ipStr) {
        if (ipStr == null || "".equals(ipStr)) {
            return -1;
        }

        if (ipStr.contains(":")) {

            ipStr = "127.0.0.1";
        }

        String[] ips = ipStr.split("\\.");

        return (Integer.parseInt(ips[0]) << 24) + (Integer.parseInt(ips[1]) << 16) + (Integer.parseInt(ips[2]) << 8) + Integer.parseInt(ips[3]);
    }


    public static String num2Ip(int ipNum) {
        return ((ipNum >> 24) & 0xFF) + "." + ((ipNum >> 16) & 0xFF) + "." + ((ipNum >> 8) & 0xFF) + "." + (ipNum & 0xFF);
    }
}
