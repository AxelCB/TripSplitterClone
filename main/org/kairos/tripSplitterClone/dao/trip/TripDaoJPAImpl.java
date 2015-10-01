package org.kairos.tripSplitterClone.dao.trip;

import org.kairos.tripSplitterClone.dao.AbstractDao;
import org.kairos.tripSplitterClone.model.account.Movement;
import org.kairos.tripSplitterClone.model.expense.Expense;
import org.kairos.tripSplitterClone.model.expense.ExpenseMovement;
import org.kairos.tripSplitterClone.model.trip.Trip;
import org.kairos.tripSplitterClone.model.trip.Trip_;
import org.kairos.tripSplitterClone.model.trip.UserTrip;
import org.kairos.tripSplitterClone.model.trip.UserTrip_;
import org.kairos.tripSplitterClone.model.user.User;
import org.kairos.tripSplitterClone.model.user.User_;
import org.kairos.tripSplitterClone.utils.DozerUtils;
import org.kairos.tripSplitterClone.vo.expense.ExpenseMovementVo;
import org.kairos.tripSplitterClone.vo.expense.ExpenseVo;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.trip.UserTripVo;
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

		List<TripVo> tripVoList = new ArrayList<>();

		// builds the query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Trip> query = builder.createQuery(this.getClazz());
		Root<Trip> tripRoot = query.from(this.getClazz());
		Join<Trip,UserTrip> userTripJoin = tripRoot.join(Trip_.travelers);
		Join<UserTrip,User> userJoin = userTripJoin.join(UserTrip_.user);

		Predicate filters = builder.conjunction();

		// filters by user
		filters = builder.and(filters, builder.equal(userJoin.get(User_.id),userVo.getId()));
		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(tripRoot.get(Trip_.deleted)
				.as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		try {
			// fetch the document type
			List<Trip> trips = em.createQuery(query).getResultList();

			return this.map(trips);
		} catch (NoResultException e) {
			// there was no city with required name
			return null;
		}
	}

	@Override
	public Boolean isTraveler(EntityManager em, TripVo tripVo, UserVo userVo) {
		this.logger.debug("checking if user: {} is traveler of trip: {}", userVo.getEmail(),tripVo);

		// builds the query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Trip> query = builder.createQuery(this.getClazz());
		Root<Trip> tripRoot = query.from(this.getClazz());
		Join<Trip,UserTrip> userTripJoin = tripRoot.join(Trip_.travelers);
		Join<UserTrip,User> userJoin = userTripJoin.join(UserTrip_.user);

		Predicate filters = builder.conjunction();

		// filters by trip
		filters = builder.and(filters, builder.equal(tripRoot.get(Trip_.id),tripVo.getId()));
		// filters by user
		filters = builder.and(filters, builder.equal(userJoin.get(User_.id),userVo.getId()));
		// filters by the trip deleted flag
		filters = builder.and(filters, builder.equal(tripRoot.get(Trip_.deleted).as(Boolean.class), Boolean.FALSE));
		// filters by the user trip deleted flag
		filters = builder.and(filters, builder.equal(userTripJoin.get(UserTrip_.deleted).as(Boolean.class), Boolean.FALSE));
		// filters by the user deleted flag
		filters = builder.and(filters, builder.equal(userJoin.get(User_.deleted).as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		try {
			// fetch the document type
			Trip trip = em.createQuery(query).getSingleResult();

			return trip!=null;
		} catch (NoResultException e) {
			// there was no city with required name
			return Boolean.FALSE;
		}
	}

	@Override
	public TripVo persist(EntityManager em, TripVo entityVo) {
		this.logger.debug("persisting entity");

		Trip trip = null;

		if (entityVo.getId() == null) {
			trip = this.map(entityVo);

		} else {
			trip = this.getEntityById(em, entityVo.getId());
			for(ExpenseVo expenseVo : entityVo.getExpenses()){
				Expense expense = null;
				for(Expense auxExpense : trip.getExpenses()){
					if(auxExpense.getId().equals(expense.getId())){
						expense = auxExpense;
					}
				}
				if(expense == null){
					expense = new Expense();
					expense.setDeleted(Boolean.FALSE);
					expense.setDescription(expenseVo.getDescription());
					expense.setPaymentMovement(this.getMapper().map(expenseVo.getPaymentMovement(), Movement.class));
					expense.setTrip(trip);
					for(ExpenseMovementVo expenseMovementVo : expenseVo.getExpenseMovements()){
						expense.getExpenseMovements().add(new ExpenseMovement(this.getMapper().map(expenseMovementVo.getMovement(), Movement.class), expense));

					}
					trip.addExpense(expense);
				}
			}
			for(UserTripVo userTripVo : entityVo.getTravelers()){
				UserTrip userTrip = null;
				for(UserTrip auxUserTrip : trip.getTravelers()){
					if(auxUserTrip.getId().equals(userTripVo.getId())){
						userTrip = auxUserTrip;
						userTrip.getAccount().setBalance(userTripVo.getAccount().getBalance());
						userTrip.getAccount().setInMovements(DozerUtils.map(this.getMapper(), userTripVo.getAccount().getOutMovements(), Movement.class));
						userTrip.getAccount().setOutMovements(DozerUtils.map(this.getMapper(), userTripVo.getAccount().getOutMovements(), Movement.class));
					}

				}
				if(userTrip==null){
					trip.addTraveler(this.getMapper().map(userTripVo,UserTrip.class));
				}
			}
//			this.map(entityVo, trip);
		}

		trip.setDeleted(Boolean.FALSE);
		trip = em.merge(trip);

		return this.map(trip);
	}
}

