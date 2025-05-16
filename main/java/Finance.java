import java.io.Serializable;

public class Finance implements Serializable {
    private int totalExpenses; // общие затраты
    private int totalRevenue;  // общая выручка

    public Finance() {
        this.totalExpenses = 0;
        this.totalRevenue = 0;
    }

    // Расходы
    public void recordExpense(int costPrice, int quantity) {
        int expense = costPrice * quantity;
        totalExpenses += expense;
    }

    // Доходы
    public void recordRevenue(int price, int quantity) {
        int revenue = price * quantity;
        totalRevenue += revenue;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public int getNetProfit() {
        return totalRevenue - totalExpenses;
    }
}
