package example.foodbuzz.data;

/**
 * Created by DELL on 4/4/2017.
 */

public class OrderItem {
    String name;
    String number;
    String price;
    int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public OrderItem(int ID,String name,String number,String price){
        this.ID = ID;
        this.name = name;
        this.number = number;
        this.price = price;
    }

    public OrderItem(){

    }
}
