package org.kairos.tripSplitterClone.dao.user;

import org.apache.commons.lang.StringUtils;
import org.kairos.tripSplitterClone.dao.AbstractDao;
import org.kairos.tripSplitterClone.model.user.User;
import org.kairos.tripSplitterClone.model.user.User_;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created on 8/27/15 by
 *
 * @author AxelCollardBovy.
 */
public class UserDaoJPAImpl extends AbstractDao<User, UserVo> implements I_UserDao {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(UserDaoJPAImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.AbstractDao#getClazz()
	 */
	@Override
	protected Class<User> getClazz() {
		return User.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.AbstractDao#getVoClazz()
	 */
	@Override
	public Class<UserVo> getVoClazz() {
		return UserVo.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.universe.core.dao.AbstractDao#addFilters(javax.persistence.criteria
	 * .Root, javax.persistence.criteria.CriteriaBuilder,
	 * javax.persistence.criteria.Predicate, org.universe.core.vo.AbstractVo)
	 */
	@Override
	protected Predicate addFilters(Root<User> root, CriteriaBuilder builder,
	                               Predicate filters, UserVo vo) {

		if (StringUtils.isNotBlank(vo.getUsername())) {
			filters = builder.and(filters, builder.like(
					builder.lower(root.get(User_.email).as(String.class)),
					("%" + vo.getUsername() + "%").toLowerCase()));
		}
		return filters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.universe.core.dao.user.I_UserDao#getByUsername(javax.persistence.
	 * EntityManager, java.lang.String)
	 */
	@Override
	public UserVo getByUsername(EntityManager em, String username) {
		this.logger.debug("attempting login for username: {}", username);

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(this.getClazz());

		Predicate filters = builder.conjunction();

		// filters by name
		filters = builder.and(filters, builder.equal(root.get(User_.email)
				.as(String.class), username));
		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(root.get(User_.deleted)
				.as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		try {
			// fetch the user
			User user = em.createQuery(query).getSingleResult();

			return this.map(user);
		} catch (NoResultException e) {
			// there was no user with required name
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.user.I_UserDao#checkUsernameUniqueness(javax.
	 * persistence.EntityManager, java.lang.String, java.lang.Long)
	 */
	@Override
	public Boolean checkUsernameUniqueness(EntityManager em, String username,
	                                       Long excludeId) {
		this.logger.debug("searching User by username: {}, id !=: {}",
				username, excludeId);

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(this.getClazz());

		Predicate filters = builder.conjunction();

		// filters by name
		filters = builder.and(
				filters,
				builder.like(builder.lower(root.get(User_.email).as(
						String.class)), username.toLowerCase()));

		if (excludeId != null) {
			// filters for ID different than the excluded ID
			filters = builder.and(filters, builder.notEqual(root.get(User_.id)
					.as(Long.class), excludeId));
		}

		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(root.get(User_.deleted)
				.as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		// fetch the role types
		List<User> users = em.createQuery(query).getResultList();

		return users.size() == 0;
	}

}

