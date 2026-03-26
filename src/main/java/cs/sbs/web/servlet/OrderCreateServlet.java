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

    private List<Order> orderList;
    private AtomicInteger orderIdGenerator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        synchronized (getServletContext()) {
            // 强制每次启动都清空数据，彻底解决本地/ GitHub 不一致问题
            getServletContext().setAttribute("orderList", new ArrayList<Order>());
            getServletContext().setAttribute("orderIdGenerator", new AtomicInteger(1002));

            orderList = (List<Order>) getServletContext().getAttribute("orderList");
            orderIdGenerator = (AtomicInteger) getServletContext().getAttribute("orderIdGenerator");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String customer = req.getParameter("customer");
        String food = req.getParameter("food");
        String quantityStr = req.getParameter("quantity");

        if (customer == null || customer.trim().isEmpty()
                || food == null || food.trim().isEmpty()
                || quantityStr == null || quantityStr.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Error: missing parameter (customer/food/quantity)");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Error: quantity must be a valid number");
                return;
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Error: quantity must be a valid number");
            return;
        }

        int orderId = orderIdGenerator.getAndIncrement();
        Order newOrder = new Order(orderId, customer.trim(), food.trim(), quantity);

        synchronized (orderList) {
            orderList.add(newOrder);
        }

        getServletContext().setAttribute("orderList", orderList);

        out.printf("Order Created: %d\n", orderId);
        out.flush();
    }
}