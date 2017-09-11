package org.exemplo.adriano.atividade06;

import Classes.Avaliacao;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Adriano Xavier
 */
public class CriaAvaliacao extends HttpServlet {
        static ArrayList<Avaliacao> avaliacao = new ArrayList<>();

        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession(true);
            
            String titulo = request.getParameter("titulo");
            String usuario = request.getParameter("usuario");
            String avalia = request.getParameter("avaliacao");
            Float nota_final = Float.parseFloat(request.getParameter("inlineRadioOptions"));
            
            session.setAttribute("titulo", titulo);
            session.setAttribute("usuario", usuario);
            session.setAttribute("avalia", avalia);
            session.setAttribute("nota_final", nota_final);
                        
            avaliacao.add(new Avaliacao(titulo, usuario, avalia, nota_final));
            
            session.setAttribute("avaliacao", avaliacao);
            
            RequestDispatcher id = request.getRequestDispatcher("index.jsp");
            id.forward(request, response);
        }
    }
