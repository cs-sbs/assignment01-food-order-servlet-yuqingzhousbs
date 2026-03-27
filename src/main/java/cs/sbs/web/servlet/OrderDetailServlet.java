package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class OrderDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // 1. 解析路径参数中的订单ID
        String pathInfo = req.getPathInfo(); // /1001 -> pathInfo = "/1001"
        if (pathInfo == null || pathInfo.length() <= 1) {
            out.println("Error: Order ID is required (e.g., /order/1001)");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.flush();
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(pathInfo.substring(1)); // 截取/后的数字
        } catch (NumberFormatException e) {
            out.println("Error: invalid Order ID (must be a number)");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.flush();
            return;
        }

        // 2. 从订单列表查询订单
        List<Order> orderList = OrderCreateServlet.orderList;
        Order targetOrder = null;
        for (Order order : orderList) {
            if (order.getId() == orderId) {
                targetOrder = order;
                break;
            }
        }

        // 3. 处理订单不存在场景
        if (targetOrder == null) {
            out.println("Error: Order " + orderId + " not found");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.flush();
            return;
        }

        // 4. 返回订单详情
        out.println("Order Detail");
        out.println();
        out.println("Order ID: " + targetOrder.getId());
        out.println("Customer: " + targetOrder.getCustomer());
        out.println("Food: " + targetOrder.getFood());
        out.println("Quantity: " + targetOrder.getQuantity());
        out.flush();
    }
}