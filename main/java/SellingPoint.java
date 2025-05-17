import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SellingPoint implements Storage, Responsibility, Serializable {
    private String pointID;
    private List<Product> products;
    private List<Employee> employees;
    private Employee responsible;
    private int revenue;

    public SellingPoint(String pointID) {
        this.pointID = pointID;
        this.revenue = 0;
        products = new ArrayList<>();
        employees = new ArrayList<>();
    }

    @Override
    public void assignResponsible(Employee employee) {
        if (responsible != null) {
            responsible.setResponsible(false);
        }
        this.responsible = employee;
        employee.setResponsible(true);
    }

    @Override
    public Employee getResponsible() {
        return responsible;
    }

    @Override
    public boolean addProduct(Product product) {
        products.add(product);
        return true;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public String getID() {
        return pointID;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    // Найти товар по его id
    @Override
    public Product getProductById(String productId) {
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    // Удалить некоторое количество товара
    @Override
    public boolean removeProduct(String productId, int quantity) {
        Product p = getProductById(productId);
        if (p != null && p.getQuantity() >= quantity) {
            p.setQuantity(p.getQuantity() - quantity);
            // если товара не осталось, то удаляем насовсем
            if (p.getQuantity() == 0) {
                products.remove(p);
            }
            return true;
        }
        return false;
    }

    // Информация о пункте продаж
    public void printInfo() {
        System.out.println("id пункта продаж: " + pointID);
        System.out.println("Выручка: " + revenue + " рублей");

        System.out.println("Товары:");
        for (Product p : products) {
            System.out.println(" - " + p.getName() + ", цена: " + p.getPrice() + ", количество: " + p.getQuantity());
        }

        System.out.println("Сотрудники:");
        for (Employee e : employees) {
            System.out.println(" - " + e.getName() + " (" + e.getPosition() + ")");
        }

        if (responsible != null) {
            System.out.println("Ответственное лицо: " + responsible.getName());
        } else {
            System.out.println("Ответственное лицо не назначено");
        }

    }
}

