import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Warehouse implements Storage, Responsibility, Serializable {
    private String warehouseID;
    private Map<String, CellOfWarehouse> cells; //Список ячеек по номеру
    private Employee responsible;
    private int capacity;

    public Warehouse(String warehouseID, int capacity) {
        this.warehouseID = warehouseID;
        this.cells = new HashMap<>();
        this.capacity = capacity;

        for (int i = 1; i <= capacity; i++) {
            String cellID = "" + i;
            cells.put(cellID, new CellOfWarehouse(cellID));
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public String getWarehouseID() {
        return warehouseID;
    }

    @Override
    public void assignResponsible(Employee employee) {
        if (responsible != null) {
            responsible.setResponsible(false); // Снимаем старое лицо
        }
        this.responsible = employee;
        employee.setResponsible(true);
    }

    @Override
    public Employee getResponsible() {
        return responsible;
    }

    @Override
    public String getID() {
        return warehouseID;
    }

    public Map<String, CellOfWarehouse> getCells() {
        return cells;
    }

    @Override
    public Product getProductById(String productId) {
        for (CellOfWarehouse cell : cells.values()) {
            Product p = cell.getProduct();
            if (p != null) {
                if (p.getId().equals(productId))
                    return p;
            }
        }
        return null;
    }

    @Override
    public boolean removeProduct(String productId, int quantity) {
        for (CellOfWarehouse cell : cells.values()) {
            Product p = cell.getProduct();
            if (p.getId().equals(productId) && p.getQuantity() >= quantity) {
                p.setQuantity(p.getQuantity() - quantity);
                if (p.getQuantity() == 0) {
                    cell.removeProduct();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void addProduct(Product product) {
        // найти или создать ячейку
        CellOfWarehouse cell = cells.getOrDefault("DEFAULT", new CellOfWarehouse("DEFAULT"));
        cell.addProduct(product);
        cells.putIfAbsent("DEFAULT", cell);
    }

    public boolean purchaseProduct(Product product, int quantity) {
        if (product == null || quantity <= 0)
            return false;

        Product existing = getProductById(product.getId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            return true;
        } else {
            // Создаем копию товара с нужным количеством
            Product newProduct = new Product(
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getPrice(),
                    product.getCostPrice()
            );
            CellOfWarehouse emptyCell = findFirstEmptyCell();
            if (emptyCell != null) {
                emptyCell.addProduct(newProduct);
                return true; // Товар успешно добавлен
            }
            return false;
        }
    }
    // Вспомогательный метод для поиска первой пустой ячейки
    public CellOfWarehouse findFirstEmptyCell() {
        for (CellOfWarehouse cell : cells.values()) {
            if (cell.getProduct() == null) {
                return cell;
            }
        }
        return null; // Если все ячейки заняты
    }

    public void printInfo() {
        System.out.println("Склад ID: " + getWarehouseID() + ", ячеек: " + getCapacity());
        for (CellOfWarehouse cell : cells.values()) {
            if (cell.getProduct() != null) {
                System.out.println("Ячейка: " + cell.getCellNumber());
                System.out.println("Товар: " + cell.getProduct().getName() + ", количество: " + cell.getProduct().getQuantity());
            }
        }
    }
}
