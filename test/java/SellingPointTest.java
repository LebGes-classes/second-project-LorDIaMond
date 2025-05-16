import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SellingPointTest {

    private SellingPoint sellingPoint;
    private Product product1;
    private Product product2;
    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        sellingPoint = new SellingPoint("SP001");

        product1 = new Product("P1", "Хлеб", 10, 50, 30);
        product2 = new Product("P2", "Молоко", 5, 80, 60);

        employee1 = new Employee("E1", "Иван Иванов", "Продавец", false);
        employee2 = new Employee("E2", "Петр Петров", "Администратор", false);
    }

    @Test
    void testAssignResponsible() {
        sellingPoint.assignResponsible(employee1);
        assertEquals(employee1, sellingPoint.getResponsible());
        assertTrue(employee1.isResponsible());

        // Назначаем нового ответственного
        Employee newResponsible = new Employee("E3", "Новый Ответственный", "Менеджер", true);
        sellingPoint.assignResponsible(newResponsible);
        assertEquals(newResponsible, sellingPoint.getResponsible());
        assertFalse(employee1.isResponsible()); // Старое лицо должно быть снято
        assertTrue(newResponsible.isResponsible());
    }

    @Test
    void testAddProductAndGetProductById() {
        sellingPoint.addProduct(product1);
        sellingPoint.addProduct(product2);

        Product found = sellingPoint.getProductById("P1");
        assertNotNull(found);
        assertEquals("Хлеб", found.getName());
    }

    @Test
    void testRemoveProduct_FullRemoval() {
        sellingPoint.addProduct(product1);

        boolean result = sellingPoint.removeProduct("P1", 10); // Удаляем всё количество

        assertTrue(result);
        assertNull(sellingPoint.getProductById("P1"));
    }

    @Test
    void testRemoveProduct_PartialRemoval() {
        sellingPoint.addProduct(product1);

        boolean result = sellingPoint.removeProduct("P1", 5); // Удаляем половину

        assertTrue(result);
        Product p = sellingPoint.getProductById("P1");
        assertNotNull(p);
        assertEquals(5, p.getQuantity());
    }

    @Test
    void testRemoveProduct_NotEnoughQuantity() {
        sellingPoint.addProduct(product1);

        boolean result = sellingPoint.removeProduct("P1", 15); // Пытаемся удалить больше, чем есть

        assertFalse(result);
        Product p = sellingPoint.getProductById("P1");
        assertNotNull(p);
        assertEquals(10, p.getQuantity());
    }

    @Test
    void testAddEmployeeAndRemoveEmployee() {
        sellingPoint.addEmployee(employee1);
        sellingPoint.addEmployee(employee2);

        List<Employee> employees = sellingPoint.getEmployees();
        assertTrue(employees.contains(employee1));
        assertTrue(employees.contains(employee2));

        sellingPoint.removeEmployee(employee1);
        assertFalse(sellingPoint.getEmployees().contains(employee1));
    }

    @Test
    void testRevenueIsUpdated() {
        sellingPoint.setRevenue(1000);
        assertEquals(1000, sellingPoint.getRevenue());

        sellingPoint.setRevenue(500);
        assertEquals(500, sellingPoint.getRevenue());
    }

    @Test
    void testPrintInfo_NoExceptions() {
        // Просто вызываем printInfo(), чтобы убедиться, что он не падает
        sellingPoint.addProduct(product1);
        sellingPoint.addEmployee(employee1);
        sellingPoint.assignResponsible(employee1);
        sellingPoint.setRevenue(2000);

        assertDoesNotThrow(() -> sellingPoint.printInfo());
    }
}