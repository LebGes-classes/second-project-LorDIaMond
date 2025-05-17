import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service implements Serializable {
    private Map<String, Warehouse> warehouses;
    private Map<String, SellingPoint> sellingPoints;
    private Map<String, Employee> employees;
    private Map<String, Customer> customers;
    private Map<String, Product> products = new HashMap<>();
    private Finance finance = new Finance();

    public Service() {
        warehouses = new HashMap<>();
        sellingPoints = new HashMap<>();
        employees = new HashMap<>();
        customers = new HashMap<>();
    }

    public Map<String, Warehouse> getAllWarehouses() {
        return warehouses;
    }
    public Map<String, SellingPoint> getAllSellingPoints() {
        return sellingPoints;
    }
    public Map<String, Employee> getAllEmployees() {
        return employees;
    }
    public Map<String, Customer> getAllCustomers() {
        return customers;
    }
    public Finance getFinanceManager() {
        return finance;
    }

    // Добавить cклад
    public void addWarehouse(Warehouse warehouse) {
        if (getWarehouse(warehouse.getID()) == null) {
            warehouses.put(warehouse.getID(), warehouse);
            System.out.println("Вы открыли новый склад");
            warehouse.printInfo();
        }else
            System.out.println("Склад с таким id уже существует");
    }

    public Warehouse getWarehouse(String warehouseID) {
        return warehouses.get(warehouseID);
    }

    public boolean containsWarehouse(String warehouseId) {
        return warehouses.containsKey(warehouseId);
    }

    // Получить товар по id
    public Product getProductById(String productId) {
        return products.get(productId);
    }

    public boolean containsCustomer(String customerId) {
        return customers.containsKey(customerId);
    }

    // Добавляем товар в указанный склад
    public void addProductToWarehouse(String warehouseId, String cellId, Product product, int quantity) {
        Warehouse warehouse = warehouses.get(warehouseId);
        if (warehouse != null) {
            CellOfWarehouse cell = warehouse.getCell(cellId);
            if (cell != null)
                cell.addProduct(new Product(product.getId(), product.getName(), quantity, product.getPrice(), product.getCostPrice()));
        }
    }

    // Добавляем товар в пункт продаж
    public void addProductToSalesPoint(String pointId, Product product, int quantity) {
        SellingPoint point = sellingPoints.get(pointId);
        if (point != null) {
            point.addProduct(new Product(product.getId(), product.getName(), quantity, product.getPrice(), product.getCostPrice()));
        }
    }

    // Закрыть склад
    public void removeWarehouse(String warehouseID) {
        if (getWarehouse(warehouseID) != null) {
            warehouses.remove(warehouseID);
            System.out.println("Вы закрыли склад");
        }else
            System.out.println("Склад с таким id не существует");
    }

    // Открыть пункт продаж
    public void addSellingPoint(SellingPoint point) {
        if (getSellingPoint(point.getID()) == null) {
            sellingPoints.put(point.getID(), point);
            System.out.println("Вы открыли новый пункт продаж");
            point.printInfo();
        }else
            System.out.println("Пункт продаж с таким id уже существует");
    }

    public SellingPoint getSellingPoint(String id) {
        return sellingPoints.get(id);
    }

    // Закрыть пункт продаж
    public void removeSellingPoint(String pointID) {
        if (getSellingPoint(pointID) != null) {
            sellingPoints.remove(pointID);
            System.out.println("Вы закрыли пункт продаж");
        }else
            System.out.println("Пункт продаж с таким id не существует");
    }

    // Нанять сотрудника
    public void hireEmployee(Employee employee, String pointID) {
        SellingPoint point = getSellingPoint(pointID);
        if (getEmployee(employee.getEmployeeID()) == null) {
            employees.put(employee.getEmployeeID(), employee);
            point.addEmployee(employee);
            System.out.println("Вы создали нового работника");
        }else
            System.out.println("Работник с таким id уже существует");
    }

    public Employee getEmployee(String employeeID) {
        return employees.get(employeeID);
    }

    // Уволить сотрудника
    public void fireEmployee(String employeeID, String pointID) {
        SellingPoint point = getSellingPoint(pointID);
        Employee employee = getEmployee(employeeID);

        if (employee != null) {
            employees.remove(employeeID);
            point.removeEmployee(employee);
            System.out.println("Вы уволили работника");
        }else
            System.out.println("Работник с таким id не существует");
    }

    // Родить покупателя
    public boolean createCustomer(Customer customer) {
        if (customer == null) {
            System.out.println("Ошибка: передан пустой объект покупателя.");
            return false;
        }

        if (getCustomer(customer.getCustomerID()) == null) {
            customers.put(customer.getCustomerID(), customer);
            System.out.println("Вы создали нового покупателя");
            return true;
        }else
            System.out.println("Покупатель с таким id уже существует");
            return false;
    }

    public Customer getCustomer(String customerID) {
        return customers.get(customerID);
    }

    // Закупка товаров на склад
    public boolean purchaseProductToWarehouse(String warehouseID, Product product, int quantity) {
        Warehouse warehouse = warehouses.get(warehouseID);
        if (warehouse == null) return false;

        boolean success = warehouse.purchaseProduct(product, quantity);
        if (success) {
            finance.recordExpense(product.getCostPrice(), quantity);
        }
        return success;
    }

    // Продажа товара
    public boolean sellProduct(String pointId, String productId, int quantity, String customerID) {
        //Проверка на корректность значений
        if (pointId == null || productId == null || customerID == null || quantity <= 0) {
            return false;
        }

        SellingPoint point = sellingPoints.get(pointId);
        Customer customer = customers.get(customerID);

        // Проверяем, существует ли пункт продаж и покупатель
        if (point == null || customer == null) {
            System.out.println("Ошибка: Пункт продаж или покупатель не найден.");
            return false;
        }

        Product p = point.getProductById(productId);

        if (p != null && p.getQuantity() >= quantity) {
            p.setQuantity(p.getQuantity() - quantity);
            customer.addGood(p, quantity); //Добавляем товар покупателю
            point.setRevenue(point.getRevenue() + p.getPrice()*quantity); //Увеличиваем доход пункта
            finance.recordRevenue(p.getPrice(), quantity); // Записываем выручку
            return true;
        }
        return false;
    }

    // Возврат товара
    public boolean returnProduct(String pointId, String productId, int quantity, String customerID) {

        if (pointId == null || productId == null || customerID == null || quantity <= 0) {
            System.out.println("Ошибка: некорректные входные данные.");
            return false;
        }

        SellingPoint point = sellingPoints.get(pointId);
        Customer customer = customers.get(customerID);

        if (point == null) {
            System.out.println("Ошибка: пункт продаж не найден.");
            return false;
        }

        if (customer == null) {
            System.out.println("Ошибка: покупатель не найден.");
            return false;
        }

        Map<Product, Integer> goods = customer.getGoods();
        if (goods == null || goods.isEmpty()) {
            System.out.println("У покупателя нет товаров.");
            return false;
        }

        for (Product p : goods.keySet()) {
            if (p != null && p.getId().equals(productId) && p.getQuantity() >= quantity) {
                p.setQuantity(p.getQuantity() - quantity);
                point.addProduct(p); // добавляем товар обратно в пункт продаж
                customer.removeGood(p, quantity); // Удаляем товар у покупателя
                finance.recordExpense(p.getPrice(), quantity); // Записываем убыток
                point.setRevenue(point.getRevenue() - p.getPrice() * quantity); // Уменьшаем доход пункта

                return true;
            }
        }
        return false;
    }

    // Перемещение товара
    public boolean moveProduct(Storage from, Storage to, String productId, int quantity) {

        if (from == null || to == null) return false;

        Product sourceProduct = from.getProductById(productId);
        if (sourceProduct == null || sourceProduct.getQuantity() < quantity) {
            return false;
        }

        // Копируем часть товара
        Product movedProduct = new Product(
                sourceProduct.getId(),
                sourceProduct.getName(),
                quantity,
                sourceProduct.getPrice(),
                sourceProduct.getCostPrice()
        );

        // Удаляем из источника
        if (!from.removeProduct(productId, quantity)) return false;

        // Добавляем в цель
        to.addProduct(movedProduct);

        return true;
    }
    // Вспомогательный метод для нахождения storage
    public Storage findStorageUnit(String id) {
        Warehouse warehouse = warehouses.get(id);
        SellingPoint sellingPoint = sellingPoints.get(id);
        return warehouse != null ? warehouse : sellingPoint;
    }
    // Вспомогательный меод для нахождения unit
    public Responsibility findResponsibleUnit(String id) {
        Warehouse warehouse = warehouses.get(id);
        SellingPoint sellingPoint = sellingPoints.get(id);
        return warehouse != null ? warehouse : sellingPoint;
    }



    // Назначение ответственного лица
    public void changeResponsiblePerson(Responsibility unit, String employeeId) {
        //Responsibility unit = units.get(unitId);
        Employee employee = employees.get(employeeId);

        if (unit != null && employee != null) {
            unit.assignResponsible(employee);
            System.out.println("Ответственное лицо изменено на " + employee.getName());
        } else {
            System.out.println("Ошибка: Объект или сотрудник не найден.");
        }
    }

    public void printAllStoragesInfo() {
        if (warehouses.isEmpty()) {
            System.out.println("Нет складов");
        } else {
            System.out.println("Общее количество складов: " + warehouses.size());
            for (Warehouse warehouse: warehouses.values()) {
                warehouse.printInfo();
                System.out.println("-------------------------");
            }
        }
    }

    public void printAllSalesPointsInfo() {
        if (sellingPoints.isEmpty()) {
            System.out.println("Нет пунктов продаж.");
        } else {
            System.out.println("Общее количество пунктов продаж: " + sellingPoints.size());
            System.out.println("Общая выручка по всем пунктам: " + finance.getTotalRevenue());
            System.out.println("Общий убыток по всем пунктам: " + finance.getTotalExpenses());
            System.out.println("Итого доход: " + finance.getNetProfit());
            for (SellingPoint point : sellingPoints.values()) {
                point.printInfo();
                System.out.println("-------------------------");
            }
        }
    }

    public String getPrefix(String id, String type) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        // Проверяем, есть ли уже префикс
        if (type.equalsIgnoreCase("warehouse") && id.startsWith("WH")) {
            return id;
        } else if (type.equalsIgnoreCase("sellingPoint") && id.startsWith("SP")) {
            return id;
        }

        // Добавляем префикс, если его нет
        if (type.equalsIgnoreCase("warehouse")) {
            return "WH" + id;
        } else if (type.equalsIgnoreCase("sellingPoint")) {
            return "SP" + id;
        }

        return null;
    }

    // Импорт из Excel: данные приходят как список Map<колонка, значение>
    public void importProducts(List<Map<String, Object>> data) {
        for (Map<String, Object> row : data) {
            String id = (String) row.get("ID");
            String name = (String) row.get("Название");
            int quantity = Integer.parseInt(row.get("Количество").toString());
            int price = Integer.parseInt(row.get("Цена").toString());
            int costPrice = Integer.parseInt(row.get("Себестоимость").toString());

            Product product = new Product(id, name, quantity, price, costPrice);
            addProduct(id, product);
        }
    }

    public void addProduct(String id, Product product) {
        products.put(id, product);
    }


    // Сохранение данных в файл
    public void saveData(String filename) {
        DataSaving.saveToFile(this, filename);
    }

    public static Service loadData(String filename) {
        return (Service) DataSaving.readFromFile(filename);
    }

    public Finance getFinance() {
        return finance;
    }
}
