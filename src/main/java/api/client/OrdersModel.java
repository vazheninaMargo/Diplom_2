package api.client;

public class OrdersModel {
    private OrderModel[] orders;
    private int total;
    private int totalToday;

    public OrdersModel(OrderModel[] orders, int total, int totalToday) {
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    public OrdersModel() {
    }

    public OrderModel[] getOrders() {
        return orders;
    }

    public void setOrders(OrderModel[] orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }
}
