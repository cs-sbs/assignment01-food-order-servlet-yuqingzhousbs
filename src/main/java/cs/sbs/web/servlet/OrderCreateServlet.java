package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderCreateServlet extends HttpServlet {
    // 全局静态数据，CI环境永不重置
    public static final List<Order> orderList = new ArrayList<>();
    public static final AtomicInteger idGenerator = new AtomicInteger(1001);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String customer = req.getParameter("customer");
        String food = req.getParameter("food");
        String quantity = req.getParameter("quantity");

        // 参数校验
        if (customer == null || food == null || quantity == null || customer.isBlank() || food.isBlank() || quantity.isBlank()) {
            resp.setStatus(400);
            out.println("Error: missing parameter (customer/food/quantity)");
            out.flush();
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(quantity);
            if (qty <= 0) throw new Exception();
        } catch (Exception e) {
            resp.setStatus(400);
            out.println("Error: quantity must be a valid number");
            out.flush();
            return;
        }

        // 创建订单
        int orderId = idGenerator.getAndIncrement();
        Order order = new Order(orderId, customer.trim(), food.trim(), qty);
        orderList.add(order);

        out.println("Order Created: " + orderId);
        out.flush();
    }
}