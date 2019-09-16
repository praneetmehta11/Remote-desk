package com.remotedesk.networking.utility;

public class Convert {
    public static byte[] intToByte(int value) {
        byte b[] = new byte[4];
        b[0] = (byte) (value >> 24);
        b[1] = (byte) (value >> 16);
        b[2] = (byte) (value >> 8);
        b[3] = (byte) (value >> 0);
        return b;
    }

    public static int byteToInt(byte[] b) {
        return (((b[0]) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | ((b[3] & 0xff)));
    }
}