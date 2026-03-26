package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class OrderDetailServlet extends HttpServlet {

    private List<Order> orderList;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        synchronized (getServletContext()) {
            if (getServletContext().getAttribute("orderList") == null) {
                getServletContext().setAttribute("orderList", new java.util.ArrayList<Order>());
            }
            orderList = (List<Order>) getServletContext().getAttribute("orderList");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Error: order ID is required");
            return;
        }

        String orderIdStr = pathInfo.substring(1);
        int targetId;
        try {
            targetId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Error: invalid order ID");
            return;
        }

        Order foundOrder = null;
        synchronized (orderList) {
            for (Order order : orderList) {
                if (order.getId() == targetId) {
                    foundOrder = order;
                    break;
                }
            }
        }

        if (foundOrder == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Error: order not found (ID: " + targetId + ")");
            return;
        }

        out.println("Order Detail");
        out.println();
        out.println("Order ID: " + foundOrder.getId());
        out.println("Customer: " + foundOrder.getCustomer());
        out.println("Food: " + foundOrder.getFood());
        out.println("Quantity: " + foundOrder.getQuantity());
        out.flush();
    }
}