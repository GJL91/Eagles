package com.garethlewis.eagles.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.garethlewis.eagles.util.FileHandler;
import com.garethlewis.eagles.entities.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MediaSQLiteHelper extends MasterDatabase {

    private static MediaSQLiteHelper sInstance;

    public static MediaSQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MediaSQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private MediaSQLiteHelper(Context context) {
        super(context);
    }

    /**
     * Takes a single <code>NewsItem</code> and adds it to the database.
     * @param item
     *      The NewsItem to insert.
     * @return
     *      If the insert was successful.
     */
    public boolean insertStory(NewsItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String id = UUID.randomUUID().toString().replace("'", "[apos]");
        contentValues.put("Post_ID", id);
        contentValues.put("Title", item.getTitle().replace("'", "[apos]"));
        contentValues.put("Link", item.getLink().replace("'", "[apos]"));
        contentValues.put("Time", item.getTime().replace("'", "[apos]"));
        contentValues.put("Type", item.getType());

        String source = item.getImgSource();
        if (source != null && !"spadaro".equals(source)) {
            boolean success = FileHandler.saveDrawableToBitmap(item.getImg(), id);
            if (!success) source = null;
            else source = id;
        }

        contentValues.put("Image", source);

        return (db.insert("Media", null, contentValues) != -1);
    }

    public boolean containsStory(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Media WHERE Title = '" + title.replace("'", "[apos]") + "'", null);
        boolean contained = res.getCount() > 0;
        res.close();
        return contained;
    }

//    public boolean deleteStory(String title) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return (db.delete("Media", "Title = ? ", new String[] { title }) != 0);
//    }

    /**
     * Deletes a number of old entries from the database
     * @param count
     *      The number of stories to delete.
     */
    public void deleteOldStories(int count) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase dbw = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Media", null);
        res.moveToFirst();
        List<String> titles = new ArrayList<>();
        List<String> imgs = new ArrayList<>();
        int i = 0;
        while (i < count && !res.isAfterLast()) {
            titles.add(res.getString(res.getColumnIndex("Title")).replace("[apos]", "'"));

            String source = res.getString(res.getColumnIndex("Image"));
            if (source != null && !"spadaro".equals(source)) {
                imgs.add(res.getString(res.getColumnIndex("Post_ID")));
            }

            res.moveToNext();
            i++;
        }

        for (String img : imgs) {
            FileHandler.deleteFile(img);
        }

        for (i = 0; i < titles.size(); i++) {
            dbw.delete("Media", "Title = ? ", new String[] { titles.get(i) });
        }

        res.close();
    }

//    public boolean deleteAllStories() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM Media", null);
//        res.moveToFirst();
//
//        List<String> imgNames = new ArrayList<String>();
//
//        int imageIndex = res.getColumnIndex("Image");
//        int idIndex = res.getColumnIndex("Post_ID");
//
//        for (int i = 0; i < res.getCount(); i++) {
//            String source = res.getString(imageIndex);
//            if (source != null && !"spadaro".equals(source)) {
//                imgNames.add(res.getString(idIndex));
//            }
//            res.moveToNext();
//        }
//
//        for (String img : imgNames) {
//            FileHandler.deleteFile(img);
//        }
//
//        SQLiteDatabase dbw = this.getWritableDatabase();
//        boolean success = (dbw.delete("Media", "1", null) > 0);
//
//        res.close();
//
//        return success;
//    }

    public NewsItem[] getStories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Media", null);
        res.moveToLast();

        NewsItem[] newsItems = new NewsItem[res.getCount()];
        for (int i = 0; i < newsItems.length; i++) {
//            String id = res.getString(res.getColumnIndex("Post_ID"));
            String title = res.getString(res.getColumnIndex("Title")).replace("[apos]", "'");
            String link = res.getString(res.getColumnIndex("Link")).replace("[apos]", "'");
            String time = res.getString(res.getColumnIndex("Time")).replace("[apos]", "'");
            String source = res.getString(res.getColumnIndex("Image"));

            Drawable d;
            d = FileHandler.getDrawableFromFile(source);

            newsItems[i] = new NewsItem(link, title, source, time, (short) 0, d);
            res.moveToPrevious();
        }

        res.close();

        return newsItems;
    }
}
