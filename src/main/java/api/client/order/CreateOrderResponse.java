package api.client.order;

import api.models.OrderModel;

public class CreateOrderResponse {
    private String name;
    private OrderModel order;
    private boolean success;

    public CreateOrderResponse(String name, OrderModel order, boolean success) {
        this.name = name;
        this.order = order;
        this.success = success;
    }

    public CreateOrderResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
