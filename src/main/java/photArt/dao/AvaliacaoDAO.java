/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photArt.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import photArt.dao.exceptions.NonexistentEntityException;
import photArt.dao.exceptions.RollbackFailureException;
import photArt.modell.Avaliacao;
import photArt.modell.Foto;

/**
 *
 * @author alunoces
 */
public class AvaliacaoDAO implements Serializable {

    public AvaliacaoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Avaliacao avaliacao) throws RollbackFailureException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Foto fotoId = avaliacao.getFotoId();
            if (fotoId != null) {
                fotoId = em.getReference(fotoId.getClass(), fotoId.getId());
                avaliacao.setFotoId(fotoId);
            }
            em.persist(avaliacao);
            if (fotoId != null) {
                fotoId.getAvaliacaoCollection().add(avaliacao);
                fotoId = em.merge(fotoId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw new RuntimeException();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Avaliacao avaliacao) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Avaliacao persistentAvaliacao = em.find(Avaliacao.class, avaliacao.getId());
            Foto fotoIdOld = persistentAvaliacao.getFotoId();
            Foto fotoIdNew = avaliacao.getFotoId();
            if (fotoIdNew != null) {
                fotoIdNew = em.getReference(fotoIdNew.getClass(), fotoIdNew.getId());
                avaliacao.setFotoId(fotoIdNew);
            }
            avaliacao = em.merge(avaliacao);
            if (fotoIdOld != null && !fotoIdOld.equals(fotoIdNew)) {
                fotoIdOld.getAvaliacaoCollection().remove(avaliacao);
                fotoIdOld = em.merge(fotoIdOld);
            }
            if (fotoIdNew != null && !fotoIdNew.equals(fotoIdOld)) {
                fotoIdNew.getAvaliacaoCollection().add(avaliacao);
                fotoIdNew = em.merge(fotoIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = avaliacao.getId();
                if (findAvaliacao(id) == null) {
                    throw new NonexistentEntityException("The avaliacao with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Avaliacao avaliacao;
            try {
                avaliacao = em.getReference(Avaliacao.class, id);
                avaliacao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The avaliacao with id " + id + " no longer exists.", enfe);
            }
            Foto fotoId = avaliacao.getFotoId();
            if (fotoId != null) {
                fotoId.getAvaliacaoCollection().remove(avaliacao);
                fotoId = em.merge(fotoId);
            }
            em.remove(avaliacao);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Avaliacao> findAvaliacaoEntities() {
        return findAvaliacaoEntities(true, -1, -1);
    }

    public List<Avaliacao> findAvaliacaoEntities(int maxResults, int firstResult) {
        return findAvaliacaoEntities(false, maxResults, firstResult);
    }

    private List<Avaliacao> findAvaliacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Avaliacao.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Avaliacao findAvaliacao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Avaliacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getAvaliacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Avaliacao> rt = cq.from(Avaliacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
