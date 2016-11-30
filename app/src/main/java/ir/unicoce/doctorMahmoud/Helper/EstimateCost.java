package ir.unicoce.doctorMahmoud.Helper;

/**
 * Created by soheil syetem on 11/29/2016.
 */

public class EstimateCost {
    public String title,cost;

    public EstimateCost(String title, String cost) {
        this.title = title;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public String getCost() {
        return cost;
    }
}
