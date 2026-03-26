package cs.sbs.web.model;

import java.io.Serializable;

// 关键：实现 Serializable 接口，才能把对象存到文件里
public class Order implements Serializable {
    private int id;
    private String customer;
    private String food;
    private int quantity;

    public Order(int id, String customer, String food, int quantity) {
        this.id = id;
        this.customer = customer;
        this.food = food;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public String getFood() { return food; }
    public int getQuantity() { return quantity; }
}