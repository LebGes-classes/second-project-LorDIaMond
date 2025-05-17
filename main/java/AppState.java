import java.util.List;
import java.util.Map;

public class AppState {
    private Map<String, Warehouse> warehouses;
    private Map<String, SellingPoint> sellingPoints;
    private Map<String, Employee> employees;
    private Map<String, Customer> customers;
    private Finance finance;

    public AppState(
            Map<String, Warehouse> warehouses,
            Map<String, SellingPoint> sellingPoints,
            Map<String, Employee> employees,
            Map<String, Customer> customers,
            Finance finance) {
        this.warehouses = warehouses;
        this.sellingPoints = sellingPoints;
        this.employees = employees;
        this.customers = customers;
        this.finance = finance;
    }
}

