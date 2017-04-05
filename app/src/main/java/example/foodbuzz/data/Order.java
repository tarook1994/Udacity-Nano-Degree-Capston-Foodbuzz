package example.foodbuzz.data;

/**
 * Created by DELL on 4/5/2017.
 */

public class Order {
    String title;
    String price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    String icon;
    String payment;

    public Order(String title,String price, String icon, String payment){
        this.title = title;
        this.price = price;
        this.icon = icon;
        this.payment = payment;

    }
}
