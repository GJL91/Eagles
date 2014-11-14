package com.garethlewis.eagles.database.entities;

import android.graphics.drawable.Drawable;

public class NewsItem {

    private final String link;
    private final String title;
    private final String imgSource;
    private final String time;
    private final short type;
    private final Drawable img;

    public NewsItem(String link, String title, String imgSource, String time, short type, Drawable img) {
        this.link = link;
        this.title = title;
        this.imgSource = imgSource;
        this.time = time;
        this.type = type;
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getImg() {
//        if (img == null) {
//            return getDrawable();
//        }
        return img;
    }

    public String getImgSource() {
        return imgSource;
    }

    public String getTime() {
        return time;
    }

    public short getType() {
        return type;
    }

//    private Drawable getDrawable() {
//        try {
//            InputStream is = (InputStream) new URL(imgSource).getContent();
//            Drawable d = Drawable.createFromStream(is, null);
//            return d;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}
