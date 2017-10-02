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
        List<Avaliacao> avaliacoes = (List<Avaliacao>) session.getAttribute("avaliacao");
        
        if (avaliacoes == null) {
            avaliacoes = new ArrayList<>();
        }
        
        String titulo = request.getParameter("titulo");
        String usuario = request.getParameter("usuario");
        String avaliacao = request.getParameter("avaliacao");
        int nota = Integer.parseInt(request.getParameter("inlineRadioOptions"));
        
        Avaliacao aval = new Avaliacao(titulo, usuario, avaliacao, nota);
        avaliacoes.add(aval);
        
        double media = 0;
        for (Avaliacao a : avaliacoes) {
            media += a.getNota_final() / ((double) avaliacoes.size());
        }
        request.setAttribute("media", media);
        
        session.setAttribute("avaliacao", avaliacoes);
        
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/index.jsp");
        rd.forward(request, response);
    }

    
}
