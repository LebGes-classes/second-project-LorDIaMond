import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Customer implements Serializable {
    private String customerID;
    private String name;
    private Map<Product, Integer> goods = new HashMap<>();

    public Customer(String name, String customerID) {
        this.customerID = customerID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void addGood(Product product, int quantity) {
        goods.put(product, quantity);
    }

    public HashMap<Product, Integer> getGoods() {
        return (HashMap<Product, Integer>) goods;
    }

    public void removeGood(Product product, int quantity) {
        if (goods.get(product) - quantity == 0) {
            goods.remove(product);
        }else
            goods.put(product, goods.get(product) - quantity);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + getName() + "товары" + getGoods() + '\'' +
                '}';
    }
}
