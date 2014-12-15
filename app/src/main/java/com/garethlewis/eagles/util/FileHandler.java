package com.garethlewis.eagles.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.garethlewis.eagles.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

//    public static FileOutputStream getInternalOutputStream(String filename) {
//        try {
//            return context.openFileOutput(filename, Context.MODE_PRIVATE);
//        } catch (FileNotFoundException e) {
//            return null;
//        }
//    }
//
//    public static FileInputStream getInternalInputStream(String filename) {
//        try {
//            return context.openFileInput(filename);
//        } catch (FileNotFoundException e) {
//            return null;
//        }
//    }

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

    public static class writeScheduleParams extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FileOutputStream outputStream = context.openFileOutput("schedule_parameters.dat", Context.MODE_PRIVATE);

                String string = "LastResultWeek = " + ScheduleParams.getLastResult() + "\n";
                string = string + "FirstFixtureWeek = " + ScheduleParams.getFirstFixture() + "\n";
                string = string + "NextGameTime = " + ScheduleParams.getNextGameTime();

                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (NullPointerException e) {
                Log.e("Eagles", "Could not create file for writing");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class readScheduleParams extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FileInputStream inputStream = context.openFileInput("schedule_parameters.dat");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = bufferedReader.readLine();
                ScheduleParams.setLastResult(Integer.parseInt(line.split(" ")[2]));

                line = bufferedReader.readLine();
                ScheduleParams.setFirstFixture(Integer.parseInt(line.split(" ")[2]));

                line = bufferedReader.readLine();
                ScheduleParams.setNextGameTime(Long.parseLong(line.split(" ")[2]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
