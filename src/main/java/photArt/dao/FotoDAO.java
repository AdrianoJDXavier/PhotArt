package photArt.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import photArt.modell.Avaliacao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import photArt.dao.exceptions.NonexistentEntityException;
import photArt.dao.exceptions.RollbackFailureException;
import photArt.modell.Foto;

public class FotoDAO implements Serializable {

    public FotoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Foto foto) throws RollbackFailureException, Exception {
        if (foto.getAvaliacaoCollection() == null) {
            foto.setAvaliacaoCollection(new ArrayList<Avaliacao>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Avaliacao> attachedAvaliacaoCollection = new ArrayList<Avaliacao>();
            for (Avaliacao avaliacaoCollectionAvaliacaoToAttach : foto.getAvaliacaoCollection()) {
                avaliacaoCollectionAvaliacaoToAttach = em.getReference(avaliacaoCollectionAvaliacaoToAttach.getClass(), avaliacaoCollectionAvaliacaoToAttach.getId());
                attachedAvaliacaoCollection.add(avaliacaoCollectionAvaliacaoToAttach);
            }
            foto.setAvaliacaoCollection(attachedAvaliacaoCollection);
            em.persist(foto);
            for (Avaliacao avaliacaoCollectionAvaliacao : foto.getAvaliacaoCollection()) {
                Foto oldFotoIdOfAvaliacaoCollectionAvaliacao = avaliacaoCollectionAvaliacao.getFotoId();
                avaliacaoCollectionAvaliacao.setFotoId(foto);
                avaliacaoCollectionAvaliacao = em.merge(avaliacaoCollectionAvaliacao);
                if (oldFotoIdOfAvaliacaoCollectionAvaliacao != null) {
                    oldFotoIdOfAvaliacaoCollectionAvaliacao.getAvaliacaoCollection().remove(avaliacaoCollectionAvaliacao);
                    oldFotoIdOfAvaliacaoCollectionAvaliacao = em.merge(oldFotoIdOfAvaliacaoCollectionAvaliacao);
                }
            }
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

    public void edit(Foto foto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Foto persistentFoto = em.find(Foto.class, foto.getId());
            Collection<Avaliacao> avaliacaoCollectionOld = persistentFoto.getAvaliacaoCollection();
            Collection<Avaliacao> avaliacaoCollectionNew = foto.getAvaliacaoCollection();
            Collection<Avaliacao> attachedAvaliacaoCollectionNew = new ArrayList<Avaliacao>();
            for (Avaliacao avaliacaoCollectionNewAvaliacaoToAttach : avaliacaoCollectionNew) {
                avaliacaoCollectionNewAvaliacaoToAttach = em.getReference(avaliacaoCollectionNewAvaliacaoToAttach.getClass(), avaliacaoCollectionNewAvaliacaoToAttach.getId());
                attachedAvaliacaoCollectionNew.add(avaliacaoCollectionNewAvaliacaoToAttach);
            }
            avaliacaoCollectionNew = attachedAvaliacaoCollectionNew;
            foto.setAvaliacaoCollection(avaliacaoCollectionNew);
            foto = em.merge(foto);
            for (Avaliacao avaliacaoCollectionOldAvaliacao : avaliacaoCollectionOld) {
                if (!avaliacaoCollectionNew.contains(avaliacaoCollectionOldAvaliacao)) {
                    avaliacaoCollectionOldAvaliacao.setFotoId(null);
                    avaliacaoCollectionOldAvaliacao = em.merge(avaliacaoCollectionOldAvaliacao);
                }
            }
            for (Avaliacao avaliacaoCollectionNewAvaliacao : avaliacaoCollectionNew) {
                if (!avaliacaoCollectionOld.contains(avaliacaoCollectionNewAvaliacao)) {
                    Foto oldFotoIdOfAvaliacaoCollectionNewAvaliacao = avaliacaoCollectionNewAvaliacao.getFotoId();
                    avaliacaoCollectionNewAvaliacao.setFotoId(foto);
                    avaliacaoCollectionNewAvaliacao = em.merge(avaliacaoCollectionNewAvaliacao);
                    if (oldFotoIdOfAvaliacaoCollectionNewAvaliacao != null && !oldFotoIdOfAvaliacaoCollectionNewAvaliacao.equals(foto)) {
                        oldFotoIdOfAvaliacaoCollectionNewAvaliacao.getAvaliacaoCollection().remove(avaliacaoCollectionNewAvaliacao);
                        oldFotoIdOfAvaliacaoCollectionNewAvaliacao = em.merge(oldFotoIdOfAvaliacaoCollectionNewAvaliacao);
                    }
                }
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
                Long id = foto.getId();
                if (findFoto(id) == null) {
                    throw new NonexistentEntityException("The foto with id " + id + " no longer exists.");
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
            Foto foto;
            try {
                foto = em.getReference(Foto.class, id);
                foto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The foto with id " + id + " no longer exists.", enfe);
            }
            Collection<Avaliacao> avaliacaoCollection = foto.getAvaliacaoCollection();
            for (Avaliacao avaliacaoCollectionAvaliacao : avaliacaoCollection) {
                avaliacaoCollectionAvaliacao.setFotoId(null);
                avaliacaoCollectionAvaliacao = em.merge(avaliacaoCollectionAvaliacao);
            }
            em.remove(foto);
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

    public List<Foto> findFotoEntities() {
        return findFotoEntities(true, -1, -1);
    }

    public List<Foto> findFotoEntities(int maxResults, int firstResult) {
        return findFotoEntities(false, maxResults, firstResult);
    }

    private List<Foto> findFotoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Foto.class));
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

    public Foto findFoto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Foto.class, id);
        } finally {
            em.close();
        }
    }

    public int getFotoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Foto> rt = cq.from(Foto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
