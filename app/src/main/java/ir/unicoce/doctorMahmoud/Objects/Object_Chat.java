package ir.unicoce.doctorMahmoud.Objects;

/**
 * Created by soheil syetem on 12/18/2016.
 */

public class Object_Chat {
    String message ;
    boolean isme;

    public Object_Chat(String message, boolean isme) {
        this.message = message;
        this.isme = isme;
    }

    public String getMessage() {
        return message;
    }

    public boolean isme() {
        return isme;
    }
}
