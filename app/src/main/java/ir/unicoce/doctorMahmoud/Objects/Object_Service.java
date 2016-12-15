package ir.unicoce.doctorMahmoud.Objects;

public class Object_Service {

    public int id,price;
    public String title , content , imageUrl;
    public boolean ischeked;

    public Object_Service(int id,String title, String content, int price,String imageUrl, boolean ischeked) {
        this.id=id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.imageUrl=imageUrl;
        this.ischeked = ischeked;
    }

    public int getId(){return id;}

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl(){return  imageUrl;}

    public boolean ischeked() {
        return ischeked;
    }



}
