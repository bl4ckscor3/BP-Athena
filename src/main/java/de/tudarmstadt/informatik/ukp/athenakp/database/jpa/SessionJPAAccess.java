package de.tudarmstadt.informatik.ukp.athenakp.database.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.tudarmstadt.informatik.ukp.athenakp.database.access.SessionCommonAccess;
import de.tudarmstadt.informatik.ukp.athenakp.database.models.Session;

/**
 * @author Daniel Lehmann
 */
public class SessionJPAAccess implements SessionCommonAccess {
	/**
	 * Common code used by all get methods which filter by simple column values.
	 * @param name The name of the column to restrict
	 * @param value The value to restrict the selection to
	 * @return A List of all sessions with the given restriction
	 */
	private List<Session> getBy(String name, Object value) {
		EntityManager entityManager = PersistenceManager.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Session> criteriaQuery = builder.createQuery(Session.class);
		Root<Session> root = criteriaQuery.from(Session.class);
		criteriaQuery
		.select(root)
		.where(builder.equal(root.get(name), value));
		List<Session> result = entityManager.createQuery(criteriaQuery).getResultList();
		entityManager.close();
		return result;
	}

	@Override
	public List<Session> getById(Long id) {
		return getBy("sessionID", id);
	}

	@Override
	public List<Session> getByTitle(String title) {
		return getBy("title", title);
	}

	@Override
	public List<Session> getByDescription(String description) {
		return getBy("description", description);
	}

	@Override
	public List<Session> getByPlace(String place) {
		return getBy("place", place);
	}

	@Override
	public void add(Session data) {
		EntityManager entityManager = PersistenceManager.getEntityManager();

		entityManager.getTransaction().begin();
		entityManager.persist(data);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Override
	public void update(Session data) {
		EntityManager entityManager = PersistenceManager.getEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(data);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Override
	public void delete(Session data) {
		EntityManager entityManager = PersistenceManager.getEntityManager();

		entityManager.getTransaction().begin();
		entityManager.remove(data);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Override
	public List<Session> get() {
		EntityManager entityManager = PersistenceManager.getEntityManager();
		List<Session> result = entityManager.createQuery("FROM Session").getResultList();
		entityManager.close();
		return result;
	}
}