import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

class WarehouseTest {

    private Warehouse warehouse;
    private Product product1;
    private Product product2;
    private Employee employee1;

    @BeforeEach
    void setUp() {
        // Инициализируем склад на 3 ячейки
        warehouse = new Warehouse("WH001", 3);

        // Создаем товары
        product1 = new Product("P1", "Товар 1", 10, 100, 80);
        product2 = new Product("P2", "Товар 2", 5, 200, 150);

        // Создаем сотрудника
        employee1 = new Employee("E1", "Иван Иванов", "менеджер", false);
    }

    @Test
    void testWarehouseInitialization() {
        assertEquals("WH001", warehouse.getWarehouseID());
        assertEquals(3, warehouse.getCapacity());

        // Проверяем, что все ячейки созданы
        Map<String, CellOfWarehouse> cells = warehouse.getCells();
        assertEquals(3, cells.size());
        assertTrue(cells.containsKey("1"));
        assertTrue(cells.containsKey("2"));
        assertTrue(cells.containsKey("3"));
    }

    @Test
    void testAssignResponsible() {
        warehouse.assignResponsible(employee1);
        assertEquals(employee1, warehouse.getResponsible());
        assertTrue(employee1.isResponsible());
    }

    @Test
    void testGetProductById() {
        warehouse.purchaseProduct(product1, 10);
        Product found = warehouse.getProductById("P1");
        assertNotNull(found);
        assertEquals("Товар 1", found.getName());
        assertEquals(10, found.getQuantity());
    }

    @Test
    void testPurchaseProduct_AddsToEmptyCell() {
        boolean result = warehouse.purchaseProduct(product1, 10);
        assertTrue(result);

        Product found = warehouse.getProductById("P1");
        assertNotNull(found);
        assertEquals(10, found.getQuantity());
    }

    @Test
    void testPurchaseProduct_IncreasesExistingQuantity() {
        warehouse.purchaseProduct(product1, 10); // Добавляем первый раз
        warehouse.purchaseProduct(product1, 5);  // Увеличиваем количество

        Product found = warehouse.getProductById("P1");
        assertNotNull(found);
        assertEquals(15, found.getQuantity());
    }

    @Test
    void testRemoveProduct() {
        warehouse.purchaseProduct(product1, 10);

        boolean removed = warehouse.removeProduct("P1", 5);
        assertTrue(removed);

        Product found = warehouse.getProductById("P1");
        assertNotNull(found);
        assertEquals(5, found.getQuantity());
    }

    @Test
    void testRemoveProduct_FullRemoval() {
        warehouse.purchaseProduct(product1, 10);

        boolean removed = warehouse.removeProduct("P1", 10);
        assertTrue(removed);

        Product found = warehouse.getProductById("P1");
        assertNull(found);
    }

    @Test
    void testAddProduct_DefaultCell() {
        warehouse.addProduct(product2);

        Product found = warehouse.getProductById("P2");
        assertNotNull(found);
        assertEquals(5, found.getQuantity());
    }

    @Test
    void testPrintInfo() {
        // Тест для метода printInfo — просто вызываем, чтобы проверить, что он не падает
        warehouse.purchaseProduct(product1, 10);
        warehouse.purchaseProduct(product2, 5);
        warehouse.printInfo(); // Вывод в консоль
    }
}