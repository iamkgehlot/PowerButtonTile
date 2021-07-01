package com.iamkgehlot.oos.qstile.tiles;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class basic {

    public static boolean writeLine(String fileName, String value) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(value.getBytes());
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
