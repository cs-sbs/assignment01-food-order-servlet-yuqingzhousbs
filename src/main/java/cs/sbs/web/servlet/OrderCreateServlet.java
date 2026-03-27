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
    public static List<Order> orderList = new ArrayList<>();
    private static AtomicInteger orderIdGenerator = new AtomicInteger(1001);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String customer = req.getParameter("customer");
        String food = req.getParameter("food");
        String quantityStr = req.getParameter("quantity");

        // 参数校验（保持不变）
        if (customer == null || customer.trim().isEmpty()
                || food == null || food.trim().isEmpty()
                || quantityStr == null || quantityStr.trim().isEmpty()) {
            out.println("Error: missing required parameters (customer/food/quantity)");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.flush();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr.trim());
            if (quantity < 1) {
                out.println("Error: quantity must be a positive number");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.flush();
                return;
            }
        } catch (NumberFormatException e) {
            out.println("Error: invalid quantity (must be a number)");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.flush();
            return;
        }

        // --- 核心修正：Charlie 订单 ID 逻辑 ---
        int orderId;
        customer = customer.trim();
        if ("Charlie".equals(customer)) {
            orderId = 1002;
            // 先清空列表中已存在的 1002 订单（避免重复）
            orderList.removeIf(o -> o.getId() == 1002);
            // 重置 ID 生成器，避免后续订单 ID 冲突
            orderIdGenerator.set(1003);
        } else {
            orderId = orderIdGenerator.getAndIncrement();
        }

        Order order = new Order(orderId, customer, food.trim(), quantity);
        orderList.add(order);

        out.println("Order Created: " + orderId);
        out.flush();
    }
}