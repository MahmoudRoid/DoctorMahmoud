package ir.unicoce.doctorMahmoud.Helper;

/**
 * Created by soheil syetem on 11/23/2016.
 */

public class News {
    public String title,content,date,imageUrl;
    int id;

    public News(int id, String title, String content, String date, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
