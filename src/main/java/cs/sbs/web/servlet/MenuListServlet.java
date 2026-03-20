package cs.sbs.web.servlet;

import cs.sbs.web.model.MenuItem;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MenuListServlet extends HttpServlet {
    private List<MenuItem> menuList;

    @Override
    public void init() throws ServletException {
        menuList = new ArrayList<>();
        menuList.add(new MenuItem("Fried Rice",8));
        menuList.add(new MenuItem("Fried Noodles",9));
        menuList.add(new MenuItem("Burger",10));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String searchName = req.getParameter("name");

        List<MenuItem> result = new ArrayList<>();
        if (searchName == null || searchName.isEmpty()) {
            result = menuList;
        } else {
            for (MenuItem item : menuList) {
                if (item.getName().contains(searchName)) {
                    result.add(item);
                }
            }
        }

        out.println("Menu List:");
        out.println();
        if (result.isEmpty()) {
            out.println("No food found!");
            return;
        }
        int index = 1;
        for (MenuItem item : result) {
            out.printf("%d. %s - $%d\n", index++, item.getName(), item.getPrice());
        }
    }
}