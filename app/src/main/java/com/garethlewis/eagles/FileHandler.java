package com.garethlewis.eagles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {

    static Context context = null;

    public static void setContext(Context c) {
        context = c;
    }

    public static File getFilesDirectory(String filename) {
        if (context != null) {
            return new File(context.getExternalFilesDir(null), filename);
        } else {
            return null;
        }
    }

    public static boolean deleteFile(String filename) {
        File file = getFilesDirectory(filename);
        return file.delete();
    }

    public static boolean saveDrawableToBitmap(Drawable drawable, String id) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return saveBitmap(id, bitmap);
    }

    public static Drawable getDrawableFromFile(String filename) {
        if (filename == null) {
            // Set filename to the path to the default image
            return null;
        } else {
            if ("spadaro".equals(filename)) {
                return context.getResources().getDrawable(R.drawable.spadaro_app_tbn);
            }
        }
        filename = getFilesDirectory(filename).getAbsolutePath();
        return Drawable.createFromPath(filename);
    }

    public static boolean saveBitmap(String filename, Bitmap bmp) {
        FileOutputStream out = null;
        File file = getFilesDirectory(filename);
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // Do nothing.
            }
        }
        return true;
    }

}
