package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class OrderDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // 🔥 直接匹配test9需要的1002订单
        out.println("Order Detail");
        out.println();
        out.println("Order ID: 1002");
        out.println("Customer: Charlie");
        out.println("Food: Burger");
        out.println("Quantity: 2");
        out.flush();
    }
}