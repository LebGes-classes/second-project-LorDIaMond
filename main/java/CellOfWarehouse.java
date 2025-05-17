import java.io.Serializable;

public class CellOfWarehouse implements Serializable {
    private String cellNumber;
    private Product product;

    public CellOfWarehouse(String cellNumber) { //сначала ячейка пуста
        this.cellNumber = cellNumber;
        this.product = null;
    }

    public boolean isEmpty() {
        return product == null;
    }

    public void addProduct(Product product) {
        this.product = product;
    }

    public void removeProduct() {
        this.product = null;
    }

    public Product getProduct() {
        return product;
    }

    public String getCellNumber() {
        return cellNumber;
    }
}
