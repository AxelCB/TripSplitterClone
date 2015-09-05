package org.kairos.tripSplitterClone.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.dozer.Mapper;
import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.utils.DozerUtils;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.PaginatedListVo;
import org.kairos.tripSplitterClone.vo.PaginatedRequestVo;
import org.kairos.tripSplitterClone.vo.PaginatedSearchRequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Typed Abstract DAO with common functionality.
 *
 * Created on 8/27/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public abstract class AbstractDao<T extends I_Model, E extends AbstractVo>
		implements I_Dao<E> {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(AbstractDao.class);

	/**
	 * Dozer Mapper.
	 */
	@Autowired
	private Mapper mapper;

	/**
	 * Flag to set the policy that the DAO will take upon complex objects.
	 * 
	 * If setted to true (default) it will map using the non-classified default
	 * mappings on the dozer configuration file, i.e, it will bring all the
	 * complex objects that the root object references.
	 * 
	 * If setted to false, it will use the "non-deep" mapping ID and rely on it
	 * to get only the object's primitive types mapped. (This will be subject to
	 * the user's mapping in the file).
	 */
	private Boolean deepMapping = Boolean.TRUE;

	/**
	 * The mapID to use when mapping objects to VOs with Dozer.
	 */
	private String mapId;

	/**
	 * Map Method exporter of this DAO.
	 */
	private MapMethod mapMethod;

	/**
	 * Search distinct flag.
	 */
	private Boolean searchDistinct = Boolean.FALSE;

	/**
	 * Get's the actual class of the DAO.
	 * 
	 * @return class
	 */
	protected abstract Class<T> getClazz();

	/**
	 * Default constructor.
	 */
	public AbstractDao() {
		this.mapMethod = new MapMethod(this);
	}

	/**
	 * Maps an object to the DAO VO Class.
	 * 
	 * @param object
	 *            the object to map
	 * 
	 * @return mapped object
	 */
	protected E map(T object) {
		Class<? extends E> clazz = this.getHierarchyVoClass(object.getClass());

		if (this.getMapId() != null) {
			return this.getMapper().map(object, clazz, this.getMapId());
		} else {
			return this.getMapper().map(object, clazz);
		}
	}

	/**
	 * Maps a list of entity objects into a list of VO objects
	 * 
	 * @param objects
	 *            entity objects list
	 * 
	 * @return VO list
	 */
	protected List<E> map(List<T> objects) {
		return DozerUtils.map(objects, this.getMapMethod());
	}

	/**
	 * Gets the hierarchy VO class for a hierarchy entity.
	 * 
	 * @param clazz
	 *            the actual class
	 * 
	 * @return the VO class to use to map to
	 */
	public Class<? extends E> getHierarchyVoClass(Class<? extends I_Model> clazz) {
		// for default, returns the configured DAO class
		return this.getVoClazz();
	}

	/**
	 * Gets the hierarchy class for a hierarchy entity.
	 * 
	 * @param clazz
	 *            the actual class
	 * 
	 * @return the class to use to map to
	 */
	public Class<? extends T> getHierarchyClass(
			Class<? extends AbstractVo> clazz) {
		// for default, returns the configured DAO class
		return this.getClazz();
	}

	/**
	 * Maps an object to the DAO Class.
	 * 
	 * @param object
	 *            the object to map
	 * 
	 * @return mapped object
	 */
	protected T map(E object) {
		if (this.getMapId() != null) {
			return this.getMapper().map(object,
					this.getHierarchyClass(object.getClass()), this.getMapId());
		} else {
			return this.getMapper().map(object,
					this.getHierarchyClass(object.getClass()));
		}
	}

	/**
	 * Maps an VO object to am entity object
	 * 
	 * @param voObject
	 *            the object to map from
	 * @param entityObject
	 *            the object to map to
	 * 
	 */
	protected void map(E voObject, T entityObject) {
		if (this.getMapId() != null) {
			this.getMapper().map(voObject, entityObject, this.getMapId());
		} else {
			this.getMapper().map(voObject, entityObject);
		}
	}

	/**
	 * Computes the first result for a page.
	 * 
	 * @param page
	 *            the page to fetch
	 * @param itemsPerPage
	 *            the total items per page
	 * @return
	 */
	protected Integer getPageFirstResult(Long page, Long itemsPerPage) {
		return ((Long) ((page - 1) * itemsPerPage)).intValue();
	}

	/**
	 * Adds order to all the queries
	 * 
	 * @param root
	 *            the root element
	 * @param builder
	 *            the criteria builder
	 * @param query
	 *            the query
	 */
	public void addDefaultOrder(Root<T> root, CriteriaBuilder builder,
			CriteriaQuery<T> query) {
		// sub classes should override this method
		// default default-order is no-order
	}

		/**
	 * Gets the query for listing all non-deleted entities.
	 * 
	 * @param em
	 *            the entity manager
	 * @param includeDeleted
	 *            wether to include deleted entities or not
	 * 
	 * @return a query
	 */
	protected CriteriaQuery<T> getListAllQuery(EntityManager em, Boolean includeDeleted) {
		this.logger.debug("generating list all query");

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(this.getClazz());
		Root<T> root = query.from(this.getClazz());

		Predicate filters = builder.conjunction();
		if (!includeDeleted) {
			// filters by the deleted flag
			filters = builder.and(filters, builder.equal(root.get("deleted")
					.as(Boolean.class), Boolean.FALSE));
		}
		// global filters
		filters = this.addGlobalFilters(root, builder, filters);

		query.where(filters);

		return query;
	}

	/**
	 * Gets the query for counting all non-deleted entities.
	 * 
	 * @param em
	 *            the entity manager
	 * @return a query
	 */
	protected CriteriaQuery<Long> getCountAllQuery(EntityManager em) {
		this.logger.debug("generating list all query");

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<T> root = query.from(this.getClazz());
		query.select(builder.count(root));

		Predicate filters = builder.conjunction();
		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(
				root.get("deleted").as(Boolean.class), Boolean.FALSE));
		// global filters
		filters = this.addGlobalFilters(root, builder, filters);

		query.where(filters);

		return query;
	}

	/**
	 * Makes the query for a filtered search.
	 * 
	 * @param em
	 *            the entity manager
	 * @param vo
	 *            the vo with the data to filter with
	 * 
	 * @return a CriteriaQuery
	 */
	protected CriteriaQuery<T> getSearchAllQuery(EntityManager em, E vo) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(this.getClazz());
		Root<T> root = query.from(this.getClazz());

		// conjunction to set the deleted filter and then the specific filters
		Predicate filters = builder.conjunction();

		// global filters
		filters = this.addGlobalFilters(root, builder, filters);

		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(
				root.get("deleted").as(Boolean.class), Boolean.FALSE));

		// adds the subclass specific filters
		filters = this.addFilters(root, builder, filters, vo);

		query.where(filters);

		// adds the distinct clause if necessary
		if (this.getSearchDistinct()) {
			query.select(root).distinct(Boolean.TRUE);
		}

		return query;
	}

	/**
	 * Makes the query for counting the results of a filtered search.
	 * 
	 * @param em
	 *            the entity manager
	 * @param vo
	 *            the vo with the data to filter with
	 * 
	 * @return a CriteriaQuery
	 */
	protected CriteriaQuery<Long> getFilteredCountAllQuery(EntityManager em,
			E vo) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<T> root = query.from(this.getClazz());

		// conjunction to set the deleted filter and then the specific filters
		Predicate filters = builder.conjunction();

		// global filters
		filters = this.addGlobalFilters(root, builder, filters);

		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(
				root.get("deleted").as(Boolean.class), Boolean.FALSE));
		// adds the subclass specific filters
		filters = this.addFilters(root, builder, filters, vo);
		query.select(builder.count(root));
		query.where(filters);

		return query;
	}

	/**
	 * Sets the filters to the query using the data in the VO.
	 * 
	 * @param root
	 *            the query root
	 * @param builder
	 *            the criteria builder
	 * @param filters
	 *            the predicate to add filters to
	 * @param vo
	 *            the VO with the data
	 * @return the modified predicate
	 */
	protected abstract Predicate addFilters(Root<T> root,
			CriteriaBuilder builder, Predicate filters, E vo);

	/**
	 * Sets the global filters to use always.
	 * 
	 * @param root
	 *            the query root
	 * @param builder
	 *            the criteria builder
	 * @param filters
	 *            the predicate to add filters to
	 * 
	 * @return the modified predicate
	 */
	protected Predicate addGlobalFilters(Root<T> root, CriteriaBuilder builder,
			Predicate filters) {
		return filters;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.dao.I_Dao#listAll(javax.persistence.EntityManager,
	 * org.universe.core.vo.OrderVo)
	 */
	public List<E> listAll(EntityManager em,Boolean includeDeleted) {

		// gets the entities with the subclass query
		List<T> entities = em.createQuery(
				this.getListAllQuery(em,includeDeleted))
				.getResultList();

		// transforms them to the VO entities
		List<E> entitiesVo = DozerUtils.map(entities, this.getMapMethod());

		return entitiesVo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.universe.core.dao.I_Dao#countAll(javax.persistence.EntityManager)
	 */
	@Override
	public Long countAll(EntityManager em) {
		// gets the entities with the subclass query
		em.clear();
		Long total = em.createQuery(this.getCountAllQuery(em))
				.getSingleResult();

		return total;
	}

	/**
	 * Counts all entities filtered for a search
	 * 
	 * @param em
	 *            the entity manager
	 * @param vo
	 *            the data to filter the entities with
	 * 
	 * @return total count of elements
	 */
	@Override
	public Long filteredCountAll(EntityManager em, E vo) {
		em.clear();
		Long total = em.createQuery(this.getFilteredCountAllQuery(em, vo))
				.getSingleResult();

		return total;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.universe.core.dao.I_Dao#listAllIncludingDeleted(javax.persistence
	 * .EntityManager)
	 */
	@Override
	public List<E> listAllIncludingDeleted(EntityManager em) {
		return this.listAll(em, Boolean.TRUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.dao.I_Dao#listAll(javax.persistence.EntityManager)
	 */
	@Override
	public List<E> listAll(EntityManager em) {
		return this.listAll(em, Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.universe.core.dao.I_Dao#listPage(javax.persistence.EntityManager,
	 * org.universe.core.vo.PaginatedRequestVo, java.lang.Long)
	 */
	@Override
	public PaginatedListVo<E> listPage(EntityManager em,
			PaginatedRequestVo paginatedRequestVo, Long itemsPerPage) {
		return this.listPage(em, paginatedRequestVo, itemsPerPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.dao.I_Dao#search(javax.persistence.EntityManager,
	 * org.universe.core.vo.AbstractVo)
	 */
	@Override
	public List<E> search(EntityManager em, E vo) {
		return this.search(em, vo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.universe.core.dao.I_Dao#searchPage(javax.persistence.EntityManager,
	 * org.universe.core.vo.PaginatedSearchRequestVo, java.lang.Long)
	 */
	@Override
	public PaginatedListVo<E> searchPage(EntityManager em,
			PaginatedSearchRequestVo<E> paginatedSearchRequestVo,
			Long itemsPerPage) {
		return this.searchPage(em, paginatedSearchRequestVo, itemsPerPage);
	}

	/**
	 * Gets an entity by its ID.
	 * 
	 * @param id
	 *            to get
	 * @param em
	 *            the entity manager
	 * 
	 * @return entity VO or null
	 */
	@Override
	public E getById(EntityManager em, Long id) {
		this.logger.debug("getting entity VO by id");

		T entity = this.getEntityById(em, id);

		if (entity == null) {
			return null;
		} else {
			return this.map(entity);
		}
	}

	/**
	 * Gets an entity by its ID.
	 * 
	 * @param id
	 *            to get
	 * @param em
	 *            the entity manager
	 * 
	 * @return entity VO or null
	 */
	protected T getEntityById(EntityManager em, Long id) {
		this.logger.debug("getting entity by id");

		// builds the query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(this.getClazz());
		Root<T> root = query.from(this.getClazz());

		Predicate filters = builder.conjunction();

		// filters by id
		filters = builder.and(filters,
				builder.equal(root.get("id").as(Long.class), id));
		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(
				root.get("deleted").as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		try {
			// fetch the entity
			T entity = em.createQuery(query).getSingleResult();
			em.refresh(entity);

			return entity;
		} catch (NoResultException e) {
			// there was no entity with required name
			return null;
		}
	}

	/**
	 * Gets an entity by its ID avoiding the deletion flag.
	 * 
	 * @param id
	 *            to get
	 * @param em
	 *            the entity manager
	 * 
	 * @return entity VO or null
	 */
	protected T getEntityByIdAvoidDeletion(EntityManager em, Long id) {
		this.logger.debug("getting entity by id");

		// builds the query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(this.getClazz());
		Root<T> root = query.from(this.getClazz());

		query.where(builder.equal(root.get("id").as(Long.class), id));

		try {
			// fetch the entity
			T entity = em.createQuery(query).getSingleResult();
			em.refresh(entity);

			return entity;
		} catch (NoResultException e) {
			// there was no entity with required name
			return null;
		}
	}

	/**
	 * Persists a new entity.
	 * 
	 * @param entityVo
	 *            the entity to persist
	 * 
	 * @param em
	 *            the entity manager
	 */
	@Override
	public E persist(EntityManager em, E entityVo) {
		this.logger.debug("persisting entity");

		T entity = null;

		if (entityVo.getId() == null) {
			entity = this.map(entityVo);

		} else {
			entity = this.getEntityById(em, entityVo.getId());

			this.map(entityVo, entity);

		}

		entity.setDeleted(Boolean.FALSE);
		entity = em.merge(entity);

		return this.map(entity);
	}

	/**
	 * Deletes an entity.
	 * 
	 * @param entityVo
	 *            the entity to delete
	 * @param em
	 *            the entity manager
	 */
	@Override
	public void delete(EntityManager em, E entityVo) {
		this.logger.debug("deleting entity");

		T entity = this.getEntityById(em, entityVo.getId());
		entity.setDeleted(Boolean.TRUE);
		em.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.universe.core.dao.I_Dao#nativeUpdate(javax.persistence.EntityManager,
	 * java.lang.String)
	 */
	@Override
	public Boolean nativeUpdate(EntityManager em, String update) {
		this.logger.debug("executing a native update");

		try {
			Integer result = em.createNativeQuery(update).executeUpdate();

			return result > 0;
		} catch (Exception e) {
			this.logger.error("error executing a native update", e);

			return Boolean.FALSE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.dao.I_Dao#searchDistinct(java.lang.Boolean)
	 */
	@Override
	public void searchDistinct(Boolean distinct) {
		this.setSearchDistinct(distinct);
	}

	/**
	 * @return the mapper
	 */
	public Mapper getMapper() {
		return this.mapper;
	}

	/**
	 * @param mapper
	 *            the mapper to set
	 */
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * @return the deepMapping
	 */
	@Override
	public Boolean getDeepMapping() {
		return this.deepMapping;
	}

	/**
	 * @param deepMapping
	 *            the deepMapping to set
	 */
	@Override
	public void setDeepMapping(Boolean deepMapping) {
		this.setDeepMapping(deepMapping, this.getVoClazz());
	}

	/**
	 * @param deepMapping
	 *            the deepMapping to set
	 */
	@Override
	public void setDeepMapping(Boolean deepMapping, Class<? extends E> clazz) {
		this.deepMapping = deepMapping;
		if (deepMapping) {
			this.setMapId(null);
		} else {
			this.setMapId("non-deep-" + clazz.getSimpleName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.dao.I_Dao#setCustomMap(java.lang.String)
	 */
	@Override
	public void setCustomMap(String customMap) {
		this.setMapId(customMap);
	}

	/**
	 * @return the mapId
	 */
	public String getMapId() {
		return this.mapId;
	}

	/**
	 * @param mapId
	 *            the mapId to set
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	/**
	 * @return the mapMethod
	 */
	public MapMethod getMapMethod() {
		return this.mapMethod;
	}

	/**
	 * @param mapMethod
	 *            the mapMethod to set
	 */
	public void setMapMethod(MapMethod mapMethod) {
		this.mapMethod = mapMethod;
	}

	/**
	 * @return the searchDistinct
	 */
	private Boolean getSearchDistinct() {
		return this.searchDistinct;
	}

	/**
	 * @param searchDistinct
	 *            the searchDistinct to set
	 */
	private void setSearchDistinct(Boolean searchDistinct) {
		this.searchDistinct = searchDistinct;
	}

	/**
	 * MapMethod to export mapping logic of this DAO.
	 *
	 * Created on 8/27/15 by
	 *
	 * @author AxelCollardBovy.
	 * 
	 */
	public class MapMethod {

		/**
		 * DAO reference.
		 */
		private AbstractDao<T, E> dao;

		/**
		 * Constructor with DAO.
		 * 
		 * @param dao
		 *            DAO
		 */
		public MapMethod(AbstractDao<T, E> dao) {
			this.dao = dao;
		}

		/**
		 * Exported map method.
		 * 
		 * @param element
		 *            element to map
		 * 
		 * @return mapped element
		 */
		public E map(T element) {
			return this.dao.map(element);
		}
	}

}
