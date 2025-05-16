import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    private Service service;
    private Product product1;
    private Product product2;
    private Employee employee1;
    private Employee employee2;
    private Customer customer1;

    @BeforeEach
    void setUp() {
        service = new Service();

        // Товары
        product1 = new Product("P1", "Хлеб", 10, 100, 80);
        product2 = new Product("P2", "Молоко", 5, 150, 120);

        // Сотрудники
        employee1 = new Employee("E1", "Иван Иванов", "Продавец", false);
        employee2 = new Employee("E2", "Петр Петров", "Администратор", true);

        // Покупатель
        customer1 = new Customer("C1", "Александр Александров");

        // Добавляем склад и пункт продаж
        service.addWarehouse(new Warehouse("WH001", 10));
        service.addSellingPoint(new SellingPoint("SP001"));
    }

    @Test
    void testAddAndRemoveWarehouse() {
        assertNotNull(service.getWarehouses("WH001"));
        service.removeWarehouse("WH001");
        assertNull(service.getWarehouses("WH001"));
    }

    @Test
    void testAddAndRemoveSellingPoint() {
        assertNotNull(service.getSellingPoint("SP001"));
        service.removeSellingPoint("SP001");
        assertNull(service.getSellingPoint("SP001"));
    }

    @Test
    void testHireAndFireEmployee() {
        service.hireEmployee(employee1, "SP001");
        assertEquals(employee1, service.getEmployee("E1"));
        assertTrue(service.getSellingPoint("SP001").getEmployees().contains(employee1));

        service.fireEmployee("E1", "SP001");
        assertNull(service.getEmployee("E1"));
        assertFalse(service.getSellingPoint("SP001").getEmployees().contains(employee1));
    }

    @Test
    void testPurchaseProductToWarehouse() {
        boolean result = service.purchaseProductToWarehouse("WH001", product1, 10);
        assertTrue(result);
        Product stored = service.getWarehouses("WH001").getProductById("P1");
        assertNotNull(stored);
        assertEquals(10, stored.getQuantity());
        assertEquals(800, service.getFinance().getTotalExpenses()); // 10 * 80
    }

    @Test
    void testMoveProductFromWarehouseToSellingPoint() {
        service.purchaseProductToWarehouse("WH001", product1, 10);

        Storage from = service.getWarehouses("WH001");
        Storage to = service.getSellingPoint("SP001");

        boolean moved = service.moveProduct(from, to, "P1", 5);
        assertTrue(moved);

        Product inWarehouse = from.getProductById("P1");
        Product inSellingPoint = to.getProductById("P1");

        assertNotNull(inWarehouse);
        assertNotNull(inSellingPoint);
        assertEquals(5, inWarehouse.getQuantity());
        assertEquals(5, inSellingPoint.getQuantity());
    }

    @Test
    void testSellProduct() {
        service.purchaseProductToWarehouse("WH001", product1, 10);
        service.moveProduct(service.getWarehouses("WH001"), service.getSellingPoint("SP001"), "P1", 5);

        boolean sold = service.sellProduct("SP001", "P1", 3, "C1");
        assertTrue(sold);

        Product p = service.getSellingPoint("SP001").getProductById("P1");
        assertNotNull(p);
        assertEquals(2, p.getQuantity());

        assertEquals(300, service.getSellingPoint("SP001").getRevenue());
        assertEquals(300, service.getFinance().getTotalRevenue());
    }

    @Test
    void testReturnProduct_Successful() {
        // Проверяем, что товар у клиента есть
        Product customerProduct = null;
        for (Product p : service.getCustomer("C1").getGoods().keySet()) {
            if (p.getId().equals("P1")) {
                customerProduct = p;
                break;
            }
        }

        assertNotNull(customerProduct);
        assertEquals(3, customerProduct.getQuantity());

        // Возвращаем товар
        boolean result = service.returnProduct("SP001", "P1", 1, "C1");
        assertTrue(result);

        // Проверяем, что количество изменилось
        assertEquals(2, customerProduct.getQuantity());

        // Проверяем, что товар добавился обратно в пункт продаж
        Product pointProduct = service.getSellingPoint("SP001").getProductById("P1");
        assertNotNull(pointProduct);
        assertEquals(3, pointProduct.getQuantity());
    }

    @Test
    void testAssignResponsiblePerson() {
        service.changeResponsiblePerson(service.findResponsibleUnit("WH001"), "E1");
        assertNull(service.getWarehouses("WH001").getResponsible());

        service.hireEmployee(employee1, "SP001");
        service.changeResponsiblePerson(service.findResponsibleUnit("SP001"), "E1");
        assertEquals(employee1, service.getSellingPoint("SP001").getResponsible());
    }

    @Test
    void testFindStorageUnit() {
        assertNotNull(service.findStorageUnit("WH001"));
        assertNotNull(service.findStorageUnit("SP001"));

        assertNull(service.findStorageUnit("NOT_EXISTS"));
    }

    @Test
    void testGetPrefix() {
        assertEquals("WH123", service.getPrefix("123", "warehouse"));
        assertEquals("SP456", service.getPrefix("456", "sellingPoint"));

        assertEquals("WH789", service.getPrefix("WH789", "warehouse"));
        assertEquals("SP789", service.getPrefix("SP789", "sellingPoint"));

        assertNull(service.getPrefix("", "warehouse"));
        assertNull(service.getPrefix(null, "sellingPoint"));
    }
}