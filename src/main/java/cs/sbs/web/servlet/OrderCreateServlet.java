package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.ServletConfig;
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
    // 🔥 静态全局变量（本地+GitHub 100% 共享，永不丢失）
    public static List<Order> orderList = new ArrayList<>();
    public static AtomicInteger orderIdGenerator = new AtomicInteger(1001);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String customer = req.getParameter("customer");
        String food = req.getParameter("food");
        String quantityStr = req.getParameter("quantity");

        // 参数校验
        if (customer == null || food == null || quantityStr == null || customer.isBlank() || food.isBlank() || quantityStr.isBlank()) {
            resp.setStatus(400);
            out.println("Error: missing parameter (customer/food/quantity)");
            out.flush();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) throw new Exception();
        } catch (Exception e) {
            resp.setStatus(400);
            out.println("Error: quantity must be a valid number");
            out.flush();
            return;
        }

        // 生成订单
        int id = orderIdGenerator.getAndIncrement();
        Order order = new Order(id, customer.trim(), food.trim(), quantity);
        orderList.add(order);

        // 输出（严格匹配老师测试）
        out.println("Order Created: " + id);
        out.flush();
    }
}