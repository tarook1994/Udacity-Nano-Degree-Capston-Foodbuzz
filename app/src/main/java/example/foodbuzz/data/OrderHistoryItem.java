package example.foodbuzz.data;

/**
 * Created by DELL on 4/6/2017.
 */

public class OrderHistoryItem {
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    String payment;
    String icon;

    public OrderHistoryItem(String title,String price,String payment,String icon){

        this.title = title;
        this.price = price;
        this.payment = payment;
        this.icon = icon;

    }

}
