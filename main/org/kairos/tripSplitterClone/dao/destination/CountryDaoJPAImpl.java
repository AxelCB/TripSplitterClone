package org.kairos.tripSplitterClone.dao.destination;

import org.kairos.tripSplitterClone.dao.AbstractDao;
import org.kairos.tripSplitterClone.model.destination.Country;
import org.kairos.tripSplitterClone.model.destination.Country_;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
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
 * Created on 8/29/15 by
 *
 * @author AxelCollardBovy.
 */
public class CountryDaoJPAImpl extends AbstractDao<Country, CountryVo> implements I_CountryDao {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(CountryDaoJPAImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.AbstractDao#getClazz()
	 */
	@Override
	protected Class<Country> getClazz() {
		return Country.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.AbstractDao#getVoClazz()
	 */
	@Override
	public Class<CountryVo> getVoClazz() {
		return CountryVo.class;
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
	protected Predicate addFilters(Root<Country> root, CriteriaBuilder builder,
	                               Predicate filters, CountryVo vo) {

		return filters;
	}

	@Override
	public CountryVo findByName(EntityManager em, String name) {
		this.logger.debug("getting country by name: {}", name);

		// builds the query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Country> query = builder.createQuery(this.getClazz());
		Root<Country> root = query.from(this.getClazz());

		Predicate filters = builder.conjunction();

		// filters by name
		filters = builder.and(filters, builder.like(
				builder.lower(root.get(Country_.name).as(String.class)),
				name.toLowerCase()));
		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(root.get(Country_.deleted)
				.as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		try {
			// fetch the document type
			Country country = em.createQuery(query).getSingleResult();

			return this.map(country);
		} catch (NoResultException e) {
			// there was no country with required name
			return null;
		}

	}

	@Override
	public Boolean checkNameUniqueness(EntityManager em, String name, Long excludeId) {
		this.logger.debug("searching country by name: {}, id !=: {}", name,
				excludeId);

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Country> query = builder.createQuery(Country.class);
		Root<Country> root = query.from(this.getClazz());

		Predicate filters = builder.conjunction();

		// filters by acronym
		filters = builder.and(filters, builder.like(
				builder.lower(root.get(Country_.name).as(String.class)),
				name.toLowerCase()));

		if (excludeId != null) {
			// filters for ID different than the excluded ID
			filters = builder.and(filters, builder.notEqual(root.get(Country_.id)
					.as(Long.class), excludeId));
		}

		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(root.get(Country_.deleted)
				.as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		// fetch the document types
		List<Country> countries
				= em.createQuery(query).getResultList();

		return countries.size() == 0;

	}
}

