import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private int quantity;
    private int price;
    private int costPrice;

    public Product(String id, String name, int quantity, int price, int costPrice) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.costPrice = costPrice;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getCostPrice() {
        return costPrice;
    }

    //Информация о товаре
    @Override
    public String toString() {
        return "Товар{id='" + id + "', название ='" + name + "', количество =" + quantity + "', цена =" + price + "}";
    }
}
