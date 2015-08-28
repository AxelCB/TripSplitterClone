package org.kairos.tripSplitterClone.vo;

/**
 * Value Object for a request that is used with pagination.
 *
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 *
 * @param <E> the type of objects to be listed
 */
public class PaginatedRequestVo {
	
	/**
	 * The page to load
	 */
	private Long page;
	
	/**
	 * Flag that indicates if an aditional query is used to get the total number of items.
	 */
	private Boolean fetchTotal;

	/**
	 * @return the page
	 */
	public Long getPage() {
		return this.page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Long page) {
		this.page = page;
	}

	/**
	 * @return the fetchTotal
	 */
	public Boolean getFetchTotal() {
		return this.fetchTotal;
	}

	/**
	 * @param fetchTotal the fetchTotal to set
	 */
	public void setFetchTotal(Boolean fetchTotal) {
		this.fetchTotal = fetchTotal;
	}
	
}
