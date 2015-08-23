package org.kairos.tripSplitterClone.dao;

import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.PaginatedListVo;
import org.kairos.tripSplitterClone.vo.PaginatedRequestVo;
import org.kairos.tripSplitterClone.vo.PaginatedSearchRequestVo;

import javax.persistence.EntityManager;
import java.util.List;



/**
 * General Interfaces for all Daos.
 * 
 * @author fgonzalez
 * 
 * @param <E>
 *            the VO class that this DAO returns
 */
public interface I_Dao<E extends AbstractVo> {

	/**
	 * Gets the class of the VO that this DAO has as it parameterized type.
	 * 
	 * @return
	 */
	public Class<E> getVoClazz();

	/**
	 * Flag to set the policy that the DAO will take upon complex objects.
	 * 
	 * If setted to true (default) it will map using the non-classified default
	 * mappings on the dozer configuration file, i.e, it will bring all the
	 * complex object that the root object references.
	 * 
	 * If setted to false, it will use the "non-deep" mapping ID and rely on it
	 * to get only the object's primitive types mapped. (This will be subject to
	 * the user's mapping in the file).
	 */
	public void setDeepMapping(Boolean deepMapping);

	/**
	 * This one indicates a subclass of the specified VO to use as the mapping
	 * classificator.
	 * 
	 * @param deepMapping
	 * @param clazz
	 */
	public void setDeepMapping(Boolean deepMapping, Class<? extends E> clazz);

	/**
	 * Returns the current status of the flag.
	 */
	public Boolean getDeepMapping();
	
	/**
	 * Lists all entities including deleted ones.
	 * 
	 * @param em
	 *            the entity manager
	 * 
	 * @return a list of VOs
	 */
	public List<E> listAllIncludingDeleted(EntityManager em);

	/**
	 * Lists all entities
	 * 
	 * @param em
	 *            the entity manager
	 * @param orderVo
	 *            the order to add
	 * 
	 * @return a list of VOs
	 */
//	public List<E> listAll(EntityManager em, OrderVo orderVo);

	/**
	 * Lists all entities (using the subclassed-DAO defined order)
	 * 
	 * @param em
	 *            the entity manager
	 * 
	 * @return a list of VOs
	 */
	public List<E> listAll(EntityManager em);

	/**
	 * Counts all entities
	 * 
	 * @param em
	 *            the entity manager
	 * 
	 * @return total count of elements
	 */
	public Long countAll(EntityManager em);

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
	public Long filteredCountAll(EntityManager em, E vo);

	/**
	 * Lists a page
	 * 
	 * @param em
	 *            the entity manager
	 * @param paginatedRequestVo
	 *            a list request with the pagination info
	 * @params itemsPerPage the total items to show per page
	 * @params orderVo the order to use
	 * 
	 * @return a list of VOs
	 */
//	public PaginatedListVo<E> listPage(EntityManager em,
//	                                   PaginatedRequestVo paginatedRequestVo, Long itemsPerPage,
//	                                   OrderVo orderVo);

	/**
	 * Lists a page (using the subclassed-DAO defined order)
	 * 
	 * @param em
	 *            the entity manager
	 * @param paginatedRequestVo
	 *            a list request with the pagination info
	 * @params itemsPerPage the total items to show per page
	 * @params orderVo the order to use
	 * 
	 * @return a list of VOs
	 */
	public PaginatedListVo<E> listPage(EntityManager em,
	                                   PaginatedRequestVo paginatedRequestVo, Long itemsPerPage);

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
	public E getById(EntityManager em, Long id);

	/**
	 * Persists a new entity.
	 * 
	 * @param entityVo
	 *            the entity to persist
	 * @param em
	 *            the entity manager
	 */
	public E persist(EntityManager em, E entityVo);

	/**
	 * Deletes an entity.
	 * 
	 * @param entityVo
	 *            the entity to persist
	 * @param em
	 *            the entity manager
	 */
	public void delete(EntityManager em, E entityVo);

	/**
	 * Searches the entities using the data passed in the VO as filters.
	 * 
	 * @param em
	 *            the entity manager
	 * @param vo
	 *            vo with filter data
	 * @param orderVo
	 *            the order to use
	 * 
	 * @return the list of matching entities
	 */
//	public List<E> search(EntityManager em, E vo, OrderVo orderVo);

	/**
	 * Searches the entities using the data passed in the VO as filters. (using
	 * the subclassed-DAO defined order)
	 * 
	 * @param em
	 *            the entity manager
	 * @param vo
	 *            vo with filter data
	 * 
	 * @return the list of matching entities
	 */
	public List<E> search(EntityManager em, E vo);

	/**
	 * Searches the entities using the data passed in the VO as filters and
	 * returns the requested page.
	 * 
	 * @param em
	 *            the entity manager
	 * @param paginatedSearchRequestVo
	 *            paginated request with filter and page data
	 * @param itemsPerPage
	 *            the total items to show per page
	 * @param orderVo
	 *            the order to use
	 * 
	 * @return the list of matching entities
	 */
//	public PaginatedListVo<E> searchPage(EntityManager em,
//	                                     PaginatedSearchRequestVo<E> paginatedSearchRequestVo,
//	                                     Long itemsPerPage, OrderVo orderVo);

	/**
	 * Searches the entities using the data passed in the VO as filters and
	 * returns the requested page. (using the subclassed-DAO defined order)
	 * 
	 * @param em
	 *            the entity manager
	 * @param paginatedSearchRequestVo
	 *            paginated request with filter and page data
	 * @param itemsPerPage
	 *            the total items to show per page
	 * 
	 * @return the list of matching entities
	 */
	public PaginatedListVo<E> searchPage(EntityManager em,
	                                     PaginatedSearchRequestVo<E> paginatedSearchRequestVo,
	                                     Long itemsPerPage);

	/**
	 * Sets all the searches to add a distinct clause.
	 * 
	 * Should be setted as false, by default.
	 * 
	 * @param distinct
	 *            flag indicating to add or not a distinct clause
	 * 
	 */
	public void searchDistinct(Boolean distinct);

	/**
	 * Sets a custom map.
	 * 
	 * @param customMap
	 */
	public void setCustomMap(String customMap);

	/**
	 * Executes a native update on the BBDD.
	 * 
	 * @param em
	 *            the entity manager
	 * @param update
	 *            the update to execute
	 * 
	 * @return true iif was successful
	 */
	public Boolean nativeUpdate(EntityManager em, String update);

}
