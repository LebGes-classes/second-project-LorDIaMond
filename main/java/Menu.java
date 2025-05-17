import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class Menu implements Serializable {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Service inventoryService = new Service();
    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    moveProduct();
                    break;
                case 2:
                    changePosition();
                    break;
                case 3:
                    sellProduct();
                    break;
                case 4:
                    purchaseProduct();
                    break;
                case 5:
                    returnProduct();
                    break;
                case 6:
                    hireEmployee();
                    break;
                case 7:
                    fireEmployee();
                    break;
                case 8:
                    openWarehouse();
                    break;
                case 9:
                    closeWarehouse();
                    break;
                case 10:
                    openSellingPoint();
                    break;
                case 11:
                    closeSellingPoint();
                    break;
                case 12:
                    viewWarehouseInformation();
                    break;
                case 13:
                    viewSellingPointInformation();
                    break;
                case 14:
                    createCustomer();
                    break;
                case 15:
                    loadFromExcel();
                    break;
                case 16:
                    saveToJson();
                    break;
                case 0:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1 - Переместить товар");
        System.out.println("2 - Сменить ответственное лицо на более привлекательное");
        System.out.println("3 - Продать/купить товар");
        System.out.println("4 - Закупить товар на склад");
        System.out.println("5 - Вернуть товар");
        System.out.println("6 - Нанять сотрудника");
        System.out.println("7 - Уволить сотрудника");
        System.out.println("8 - Открыть новый склад");
        System.out.println("9 - Закрыть склад");
        System.out.println("10 - Открыть новый пункт продаж");
        System.out.println("11 - Закрыть пункт продаж");
        System.out.println("12 - Информация о складе"); //Обо всех или об одном определенном. Далее о товаре
        System.out.println("13 - Информация о пункте продаж"); //Обо всех или об одном определенном. Далее о товаре, о доходе, o сотрудниках.
        System.out.println("14 - Родить покупателя");
        System.out.println("15 - Прочитать с Excel");
        System.out.println("16 - Сохранить информацию в Json");
        System.out.println("0 - Выход");
        System.out.print("Выберите действие: ");
    }

    private static void moveProduct() {
        System.out.print("Введите ID источника (склад/пункт): ");
        String fromID = scanner.nextLine();

        System.out.print("Введите ID цели (склад/пункт): ");
        String toID = scanner.nextLine();

        System.out.print("Введите ID товара: ");
        String productID = scanner.nextLine();

        System.out.print("Введите количество: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        Storage from = inventoryService.findStorageUnit(fromID);
        Storage to = inventoryService.findStorageUnit(toID);

        if (!inventoryService.moveProduct(from, to, productID, quantity)) {
            System.out.println("Ошибка: Источник или цель не найдены.");
        }
    }

    private static void changePosition() {
        System.out.print("Введите ID здания, где хотите назначить ответственное лицо (склад/пункт): ");
        String ResponsibilityID = scanner.nextLine();

        System.out.print("Введите ID работника: ");
        String employeeID = scanner.nextLine();

        Responsibility unit = inventoryService.findResponsibleUnit(ResponsibilityID);

        inventoryService.changeResponsiblePerson(unit, employeeID);
    }

    private static void sellProduct() {
        System.out.print("Введите ID пункта продаж: ");
        String pointID = scanner.nextLine();

        System.out.print("Введите ID товара: ");
        String productID = scanner.nextLine();

        System.out.print("Введите количество покупаемого товара: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Введите ID покупателя: ");
        String customerID = scanner.nextLine();

        if (inventoryService.sellProduct(pointID, productID, quantity, customerID)) {
            System.out.println("Продукт успешно продан");
        }else {
            System.out.println("Не удалось продать товар");
        }
    }

    private static void purchaseProduct() {
        System.out.println("Напишите название товара, которым хотите закупиться");
        String name = scanner.nextLine();

        System.out.println("Напишите id товара");
        String id = scanner.nextLine();

        System.out.println("Напишите количество товара");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.println("Напишите цену товара, за которую будете закупать товар");
        int costPrice = Integer.parseInt(scanner.nextLine());

        System.out.println("Напишите цену товара, за которую планируете продать");
        int price = Integer.parseInt(scanner.nextLine());

        System.out.println("Напишите id склада, в которую будет закупаться товар");
        String warehouseID = scanner.nextLine();

        Product product = new Product(id, name, quantity, price, costPrice);

        if (inventoryService.purchaseProductToWarehouse(warehouseID, product, quantity)) {
            System.out.println("Удалось закупить товар");
        }else {
            System.out.println("Не удалось закупить товар");
        }
    }

    private static void returnProduct() {
        System.out.println("Напишите id пункта продаж, куда хотите вернуть товар");
        String pointID = scanner.nextLine();

        System.out.println("Напишите id товара");
        String productID = scanner.nextLine();

        System.out.println("Напишите количество товара");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.println("Напишите покупателя, которые вернет товар");
        String customerID = scanner.nextLine();

        if (inventoryService.returnProduct(pointID, productID, quantity, customerID)) {
            System.out.println("Товар успешно возвращен");
        }else
            System.out.println("Не удалось вернуть товар");
    }

    private static void hireEmployee() {
        System.out.println("Напишите id пункта продаж, где будет находиться работник");
        String pointID = scanner.nextLine();

        System.out.println("Напишите имя(полное) работника");
        String name = scanner.nextLine();

        System.out.println("Напишите id работника");
        String employeeID = scanner.nextLine();

        System.out.println("Напишите его должность");
        String position = scanner.nextLine();

        System.out.println("Напишитe 'да', если работник будет ответственным лицом");
        String answer = scanner.nextLine();
        boolean isResponsible;
        if (answer.equals("да") || answer.equals("Да")) {
            isResponsible = true;
        }else {
            isResponsible = false;
        }

        Employee employee = new Employee(employeeID, name, position, isResponsible);
        inventoryService.hireEmployee(employee, pointID);
    }

    private static void fireEmployee() {
        System.out.println("Напишите id пункта продаж, откуда будет уволен работник");
        String pointID = scanner.nextLine();

        System.out.println("Напишите id работника, которого хотите уволить");
        String employeeID = scanner.nextLine();

        inventoryService.fireEmployee(employeeID, pointID);
    }

    private static void openWarehouse() {
        System.out.println("Напишите id склада, который хотите открыть");
        String warehouseID = inventoryService.getPrefix(scanner.nextLine(), "warehouse");

        int capacity = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Напишите, сколько ячеек будет в новом складе");
            String input = scanner.nextLine();

            try {
                capacity = Integer.parseInt(input);
                if (capacity > 0) {
                    validInput = true;
                } else {
                    System.out.println("Число должно быть больше 0. Попробуйте еще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: нужно ввести целое число. Попробуйте еще раз.");
            }
        }

        Warehouse warehouse = new Warehouse(warehouseID, capacity);
        inventoryService.addWarehouse(warehouse);
    }

    private static void closeWarehouse() {
        System.out.println("Напишите id склада, который хотите закрыть");
        String warehouseID = scanner.nextLine();

        inventoryService.removeWarehouse(warehouseID);
    }

    private static void openSellingPoint() {
        System.out.println("Напишите id пункта продаж, который хотите открыть");
        String sellingPointID = inventoryService.getPrefix(scanner.nextLine(), "sellingPoint");

        SellingPoint sellingPoint = new SellingPoint(sellingPointID);
        inventoryService.addSellingPoint(sellingPoint);
    }

    private static void closeSellingPoint() {
        System.out.println("Напишите id пункта продаж, который хотите закрыть");
        String sellingPointID = scanner.nextLine();

        inventoryService.removeSellingPoint(sellingPointID);
    }

    private static void viewWarehouseInformation() {
        System.out.println("1 - информация обо всех складах");
        System.out.println("2 - информация об одном определенном складе");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                inventoryService.printAllStoragesInfo();
                break;
            case 2:
                System.out.println("Напишите id интересующего вас склада");
                String warehouseID = scanner.nextLine();
                Warehouse warehouse = inventoryService.getWarehouse(warehouseID);
                warehouse.printInfo();
                break;
            default:
                System.out.println("Неверный выбор");
                break;
        }
    }

    private static void viewSellingPointInformation() {
        System.out.println("1 - информация обо всех пунктах продаж");
        System.out.println("2 - информация об одном определенном пункте продаж");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                inventoryService.printAllSalesPointsInfo();
                break;
            case 2:
                System.out.println("Напишите id интересующего вас пункта продаж");
                String pointID = scanner.nextLine();
                SellingPoint point = inventoryService.getSellingPoint(pointID);
                point.printInfo();
                break;
            default:
                System.out.println("Неверный выбор");
                break;
        }
    }

    private static void createCustomer() {
        System.out.println("Напишите имя покупателя, которого хотите родить");
        String name = scanner.nextLine();

        System.out.println("Напишите id покупателя, которого хотите родить");
        String customerID = scanner.nextLine();

        Customer customer = new Customer(name, customerID);
        inventoryService.createCustomer(customer);
    }

    // Загрузка данных из Excel
    private static void loadFromExcel() {
        try {

            // 1. Загружаем товары
            List<Map<String, Object>> productData = ExcelReader.readExcel("Products");
            for (Map<String, Object> row : productData) {
                String id = (String) row.get("ID");
                String name = (String) row.get("НАЗВАНИЕ");
                int price = toIntSafe(row.get("ЦЕНА"));
                int costPrice = toIntSafe(row.get("СЕБЕСТОИМОСТЬ"));
                int quantity = toIntSafe(row.get("КОЛИЧЕСТВО"));

                Product product = new Product(id, name, price, costPrice, quantity);
                inventoryService.addProduct(id, product);
            }

            // 2. Загружаем склады и ячейки
            List<Map<String, Object>> warehouseData = ExcelReader.readExcel("Warehouses");
            for (Map<String, Object> row : warehouseData) {
                String warehouseId = (String) row.get("WAREHOUSE_ID");
                String cellId = (String) row.get("CELL_ID");
                String productId = (String) row.get("PRODUCT_ID");
                int quantity = toIntSafe(row.get("QUANTITY"));

                if (!inventoryService.containsWarehouse(warehouseId)) {
                    Warehouse warehouse = new Warehouse(warehouseId, 100); // по умолчанию 100 ячеек
                    inventoryService.addWarehouse(warehouse);
                }

                Product product = inventoryService.getProductById(productId);
                if (product != null) {
                    inventoryService.addProductToWarehouse(warehouseId, cellId, product, quantity);
                }
            }

            // 3. Загружаем пункты продаж
            List<Map<String, Object>> sellingPointData = ExcelReader.readExcel("SellingPoints");
            for (Map<String, Object> row : sellingPointData) {
                String pointId = (String) row.get("ID");
                String productId = (String) row.get("PRODUCT_ID");
                int quantity = toIntSafe(row.get("QUANTITY"));

                SellingPoint point = new SellingPoint(pointId);
                inventoryService.addSellingPoint(point);

                Product product = inventoryService.getProductById(productId);
                if (product != null) {
                    inventoryService.addProductToSalesPoint(pointId, product, quantity);
                }
            }

            // 4. Загружаем сотрудников
            List<Map<String, Object>> employeeData = ExcelReader.readExcel("Employees");
            for (Map<String, Object> row : employeeData) {
                String empId = (String) row.get("EMPLOYEE_ID");
                String name = (String) row.get("NAME");
                String position = (String) row.get("POSITION");
                String pointId = (String) row.get("POINT_ID");

                Employee employee = new Employee(empId, name, position, false);
                inventoryService.hireEmployee(employee, pointId);
            }

            // 5. Загружаем покупателей
            List<Map<String, Object>> customerData = ExcelReader.readExcel("Customers");
            for (Map<String, Object> row : customerData) {
                String customerId = (String) row.get("CUSTOMER_ID");
                String name = (String) row.get("NAME");
                String productId = (String) row.get("PRODUCT_ID");
                int quantity = toIntSafe(row.get("QUANTITY"));

                if (!inventoryService.containsCustomer(customerId)) {
                    Customer customer = new Customer(name, customerId);
                    inventoryService.createCustomer(customer);

                    Product product = inventoryService.getProductById(productId);
                    if (product != null && quantity > 0) {
                        customer.addGood(product, quantity);
                    }
                }
            }

            System.out.println("Данные успешно загружены из Excel.");

        } catch (Exception e) {
            System.out.println("Ошибка при загрузке данных из Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void saveToJson() {

        AppState state = new AppState(
                inventoryService.getAllWarehouses(),
                inventoryService.getAllSellingPoints(),
                inventoryService.getAllEmployees(),
                inventoryService.getAllCustomers(),
                inventoryService.getFinanceManager()
        );

        try {
            JsonWriter.writeToJson(state);
            System.out.println("Данные успешно сохранены.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    // Вспомогательный метод для loadfromExcel для перевода чисел в целочисленный тип
    private static int toIntSafe(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Integer i) {
            return i;
        } else if (obj instanceof Double d) {
            return d.intValue();
        } else if (obj instanceof String s && !s.isEmpty()) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Не удалось преобразовать строку в число: " + s);
                return 0;
            }
        } else {
            System.out.println("Неподдерживаемый тип значения: " + obj.getClass());
            return 0;
        }
    }
}

