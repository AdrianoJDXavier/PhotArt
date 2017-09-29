package org.exemplo.adriano.atividade06;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Adriano Xavier
 */
public class AcessaJsp extends HttpServlet {

@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute("usuario") != null) {
            request.getRequestDispatcher("WEB-INF/add-comment.jsp").forward(request, response);
        } else {
            request.setAttribute("erro", "Login ou senha incorretos!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}