
package org.exemplo.adriano.atividade06;

import Classes.Usuario;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController extends HttpServlet {

    protected void DoPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        
        String nome = request.getParameter("nome");
        String senha = request.getParameter("senha");
        
        if(nome.equals("admin")&& senha.equals("admin")){
            Usuario usuario = new Usuario(nome, senha);
            session.setAttribute("usuario", usuario);
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }else{
            request.setAttribute("erro", "Usuario ou senha incorreto!");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
}
}
