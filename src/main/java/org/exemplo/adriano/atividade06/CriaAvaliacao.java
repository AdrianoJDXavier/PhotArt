package org.exemplo.adriano.atividade06;

import Classes.Avaliacao;
import java.io.IOException;
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
    static float soma = 0;
    static int divisor = 0;
    static float media = 0f;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        String titulo = request.getParameter("titulo");
        String usuario = request.getParameter("usuario");
        String avalia = request.getParameter("avaliacao");
        float nota_final = Float.parseFloat(request.getParameter("inlineRadioOptions"));
        avaliacao.add(new Avaliacao(titulo, usuario, avalia, nota_final));

        if (avaliacao.isEmpty()) {
            media = nota_final;
        } else {
            for (int i = 0; i < avaliacao.size(); i++) {
                soma += avaliacao.get(i).getNota_final();
                divisor++;
                media = soma / divisor;
            }
        }
        session.setAttribute("titulo", titulo);
        session.setAttribute("usuario", usuario);
        session.setAttribute("avalia", avalia);
        session.setAttribute("nota_final", nota_final);

        session.setAttribute("media", media);
        session.setAttribute("avaliacao", avaliacao);

        RequestDispatcher id = request.getRequestDispatcher("index.jsp");
        id.forward(request, response);
    }
}
