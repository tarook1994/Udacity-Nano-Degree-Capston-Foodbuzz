package example.foodbuzz.data;

/**
 * Created by DELL on 3/28/2017.
 */

public class Restaurant {
    int id;
    public Restaurant(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    String name,description,price,rating;

    public Restaurant(int id,String name, String description, String price, String rating){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
    }


}
