package com.afei.camera2getpreview.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    private static final String SAVE_DIR = "/sdcard/DCIM/Camera2GetPreview";

    public static boolean saveBytes(byte[] bytes) {
        File file = new File(SAVE_DIR, System.currentTimeMillis() + ".yuv");
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

}
