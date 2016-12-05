package ir.unicoce.doctorMahmoud.Objects;

/**
 * Created by NooR26.com on 12/4/2016.
 */

public class Object_Vote {

    public String Id;
    public String parentId;
    public String QueAns;

    public Object_Vote(String id, String parentId, String queAns) {
        Id = id;
        this.parentId = parentId;
        QueAns = queAns;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getQueAns() {
        return QueAns;
    }

    public void setQueAns(String queAns) {
        QueAns = queAns;
    }

}// end class
