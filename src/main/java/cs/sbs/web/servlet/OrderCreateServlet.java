package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderCreateServlet extends HttpServlet {
    // 存数据的文件（CI 里会一直保留）
    private static final String DATA_FILE = "order_data.ser";
    public static List<Order> orderList;
    public static AtomicInteger orderIdGenerator;

    // 🔥 静态块：类加载时自动从文件读数据（服务重启也能恢复）
    static {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            orderList = (List<Order>) ois.readObject();
            orderIdGenerator = (AtomicInteger) ois.readObject();
        } catch (FileNotFoundException e) {
            // 第一次跑：文件不存在，初始化
            orderList = new ArrayList<>();
            orderIdGenerator = new AtomicInteger(1001);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            orderList = new ArrayList<>();
            orderIdGenerator = new AtomicInteger(1001);
        }
    }

    // 🔥 同步保存数据到文件
    private static synchronized void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(orderList);
            oos.writeObject(orderIdGenerator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

        // 参数校验（和之前一样）
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

        // 生成订单 + 存文件
        int id = orderIdGenerator.getAndIncrement();
        Order order = new Order(id, customer.trim(), food.trim(), quantity);
        orderList.add(order);
        saveData(); // 🔥 立刻持久化到文件，服务重启也丢不了

        out.println("Order Created: " + id);
        out.flush();
    }
}