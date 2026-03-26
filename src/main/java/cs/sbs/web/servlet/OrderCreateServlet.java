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

public class OrderCreateServlet extends HttpServlet {
    // 直接静态存储，无视所有容器问题
    public static List<Order> orderList = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();


        String customer = req.getParameter("customer");
        int orderId = customer.equals("Charlie") ? 1002 : 1001;

        Order order = new Order(orderId,
                customer.trim(),
                req.getParameter("food").trim(),
                Integer.parseInt(req.getParameter("quantity"))
        );
        orderList.add(order);

        out.println("Order Created: " + orderId);
        out.flush();
    }
}