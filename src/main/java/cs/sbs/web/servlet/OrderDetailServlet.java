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
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.setStatus(400);
            out.println("Error: order ID is required");
            out.flush();
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(path.substring(1));
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            out.println("Error: invalid order ID");
            out.flush();
            return;
        }

        // 直接读静态 orderList（已经从文件恢复了）
        Order target = null;
        for (Order o : OrderCreateServlet.orderList) {
            if (o.getId() == orderId) {
                target = o;
                break;
            }
        }

        if (target == null) {
            resp.setStatus(404);
            out.println("Error: order not found (ID: " + orderId + ")");
            out.flush();
            return;
        }

        out.println("Order Detail");
        out.println();
        out.println("Order ID: " + target.getId());
        out.println("Customer: " + target.getCustomer());
        out.println("Food: " + target.getFood());
        out.println("Quantity: " + target.getQuantity());
        out.flush();
    }
}