package org.kairos.tripSplitterClone.dao.trip;

import org.kairos.tripSplitterClone.dao.AbstractDao;
import org.kairos.tripSplitterClone.model.destination.City;
import org.kairos.tripSplitterClone.model.destination.City_;
import org.kairos.tripSplitterClone.model.trip.Trip;
import org.kairos.tripSplitterClone.model.trip.Trip_;
import org.kairos.tripSplitterClone.model.trip.UserTrip;
import org.kairos.tripSplitterClone.model.user.User;
import org.kairos.tripSplitterClone.utils.DozerUtils;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/29/15 by
 *
 * @author AxelCollardBovy.
 */
public class TripDaoJPAImpl extends AbstractDao<Trip, TripVo> implements I_TripDao {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(TripDaoJPAImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.AbstractDao#getClazz()
	 */
	@Override
	protected Class<Trip> getClazz() {
		return Trip.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.AbstractDao#getVoClazz()
	 */
	@Override
	public Class<TripVo> getVoClazz() {
		return TripVo.class;
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
	protected Predicate addFilters(Root<Trip> root, CriteriaBuilder builder,
	                               Predicate filters, TripVo vo) {

		return filters;
	}

	@Override
	public List<TripVo> usersTrip(EntityManager em, UserVo userVo) throws Exception {
		this.logger.debug("getting all trips for user: {}", userVo.getEmail());

		// builds the query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Trip> query = builder.createQuery(this.getClazz());
		Root<Trip> tripRoot = query.from(this.getClazz());
		Join<Trip,UserTrip> userTripJoin = tripRoot.join(Trip_.travelers);

		Predicate filters = builder.conjunction();

		// filters by name
		filters = builder.and(filters, builder.);
		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(tripRoot.get(Trip_.deleted)
				.as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		try {
			// fetch the document type
			City city = em.createQuery(query).getSingleResult();

			return this.map(city);
		} catch (NoResultException e) {
			// there was no city with required name
			return null;
		}

		List<TripVo> tripVoList = new ArrayList<>();
		try{
			User user = em.find(User.class, userVo.getId());
			tripVoList = DozerUtils.map(this.getMapper(), user.getTrips(), TripVo.class, this.getMapId());

		}catch (NoResultException nre) {
			this.logger.debug("there was no result, we return 0");
		}
		return tripVoList;
	}
}

