package photArt.controller;

import photArt.modell.Avaliacao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import photArt.dao.AvaliacaoDAO;

/**
 *
 * @author Adriano Xavier
 */
public class CriaAvaliacao extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Avaliacao> avaliacoes;
        AvaliacaoDAO dao = new AvaliacaoDAO(utx,emf);
        
        
        String titulo = request.getParameter("titulo");
        String usuario = request.getParameter("usuario");
        String avaliacao = request.getParameter("avaliacao");
        int nota = Integer.parseInt(request.getParameter("inlineRadioOptions"));
        
        Avaliacao aval = new Avaliacao(titulo, usuario, avaliacao, (short) nota);
        dao.create(aval);
        avaliacoes = dao.findAvaliacaoEntities();
        
        double media = 0;
        for (Avaliacao a : avaliacoes) {
            media += a.getNota() / ((double) avaliacoes.size());
        }
        request.setAttribute("media", media);
        
        request.setAttribute("avaliacao", avaliacoes);
        
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/index.jsp");
        rd.forward(request, response);
    }

    
}
