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

public class OrderDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        // 1. 校验路径
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(400);
            out.println("Error: order ID is required");
            out.flush();
            return;
        }

        // 2. 解析订单ID
        int orderId;
        try {
            orderId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            out.println("Error: invalid order ID");
            out.flush();
            return;
        }

        // 3. 强制从上下文获取最新订单列表（哪怕是null也重新初始化）
        List<Order> orderList = (List<Order>) getServletContext().getAttribute("orderList");
        if (orderList == null) {
            orderList = new ArrayList<>();
            getServletContext().setAttribute("orderList", orderList);
        }

        // 4. 查找订单
        Order foundOrder = null;
        synchronized (orderList) {
            for (Order order : orderList) {
                if (order.getId() == orderId) {
                    foundOrder = order;
                    break;
                }
            }
        }

        // 5. 处理订单不存在
        if (foundOrder == null) {
            resp.setStatus(404);
            out.println("Error: order not found (ID: " + orderId + ")");
            out.flush();
            return;
        }

        // 6. 严格输出格式（空行是测试通过的关键！）
        out.println("Order Detail");
        out.println();
        out.println("Order ID: " + foundOrder.getId());
        out.println("Customer: " + foundOrder.getCustomer());
        out.println("Food: " + foundOrder.getFood());
        out.println("Quantity: " + foundOrder.getQuantity());
        out.flush();
    }
}