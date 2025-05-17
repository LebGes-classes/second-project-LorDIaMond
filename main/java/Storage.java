public interface Storage {
    String getID();
    Product getProductById(String productId);
    boolean removeProduct(String productId, int quantity);
    boolean addProduct(Product product);
}