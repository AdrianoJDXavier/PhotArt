package org.exemplo.adriano.atividade06;

import Classes.Avaliacao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        if (session.getAttribute("usuario") == null) {
            request.setAttribute("erro", "Favor realize login para continuar!");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        } else {
            List<Avaliacao> avaliacao = (List<Avaliacao>) session.getAttribute("avaliacao");
            if (avaliacao == null) {
                avaliacao = new ArrayList<>();
            } else {
                String titulo = request.getParameter("titulo");
                String usuario = request.getParameter("usuario");
                String avalia = request.getParameter("avaliacao");
                int nota_final = Integer.parseInt(request.getParameter("inlineRadioOptions"));

                avaliacao.add(new Avaliacao(titulo, usuario, avalia, nota_final));

                float media = 0f;

                for (Avaliacao a : avaliacao) {
                    media += a.getNota_final() / avaliacao.size();
                }
                session.setAttribute("media", media);
                session.setAttribute("avaliacao", avaliacao);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/index.jsp");
                rd.forward(request, response);
            }
        }
    }
}
