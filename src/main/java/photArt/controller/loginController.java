package photArt.controller;

import photArt.modell.Usuario;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class loginController extends HttpServlet {

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        
        String nome = request.getParameter("email");
        String senha = request.getParameter("password");
        
        if(nome.equals("admin")&& senha.equals("admin")){
            Usuario usuario = new Usuario(nome, senha);
            session.setAttribute("usuario", usuario);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/index.jsp");
            rd.forward(request, response);
        }else{
            request.setAttribute("erro", "Usuario ou senha incorreto!");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
}
}
