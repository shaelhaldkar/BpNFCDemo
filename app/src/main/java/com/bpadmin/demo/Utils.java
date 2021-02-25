package com.bpadmin.demo;

import android.media.MediaPlayer;
import java.text.DecimalFormat;


import static android.content.Context.AUDIO_SERVICE;

public class Utils {
    static MediaPlayer notifyUserNotFound;
    public static DecimalFormat precision = new DecimalFormat("#.00");

    public static String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            int b = bytes[i] & 0xff;// we need to change here byte shifting

            if (b < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toString(b));  // Make this to String.. Not HEX
//            if (i >= 0) {
//                sb.append(" ");
//            }
        }
        return sb.toString();
    }

}


