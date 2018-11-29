package com.booway.mvpdemo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by wandun on 2018/11/28.
 */

public final class SerializeUtils {
    public static boolean WriteToFile(Serializable model, String path) {
        ObjectOutputStream stream = null;
        try {
            File file = new File(path);
            stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(model);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {

            }
        }
        return true;
    }

    public static Object ReadFromFile(String path) {
        ObjectInputStream stream = null;
        Object object=null;
        try {
            stream = new ObjectInputStream(new FileInputStream(new File(path)));
            object = stream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
