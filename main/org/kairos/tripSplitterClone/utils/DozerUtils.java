package org.kairos.tripSplitterClone.utils;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.kairos.tripSplitterClone.dao.AbstractDao;
import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.vo.AbstractVo;

/**
 * Dozer Utils and Helper methods.
 * 
 * @author fgonzalez
 * 
 */
public class DozerUtils {

	/**
	 * Maps an ArrayList of objects to an ArrayList of other type objects.
	 * 
	 * @param mapper
	 *            the dozer mapper
	 * @param source
	 *            the list source
	 * @param destinationType
	 *            the type for the destination list
	 * 
	 * @return a list containing all elements of the source mapped to the
	 *         destinationType
	 */
	public static <T, U> ArrayList<U> map(Mapper mapper, List<T> source,
			Class<U> destinationType) {

		// creates the destination list
		ArrayList<U> destinationList = new ArrayList<>();

		// map every element in the source and add it to the list
		for (T element : source) {
			destinationList.add(mapper.map(element, destinationType));
		}

		return destinationList;
	}

	/**
	 * Maps an ArrayList of objects to an ArrayList of other type objects using
	 * the mappingId.
	 * 
	 * @param mapper
	 *            the dozer mapper
	 * @param source
	 *            the list source
	 * @param destinationType
	 *            the type for the destination list
	 * @param mappingId
	 *            mappingId to use with dozer
	 * 
	 * @return a list containing all elements of the source mapped to the
	 *         destinationType
	 */
	public static <T, U> ArrayList<U> map(Mapper mapper, List<T> source,
			Class<U> destinationType, String mappingId) {

		// creates the destination list
		ArrayList<U> destinationList = new ArrayList<>();

		// map every element in the source and add it to the list
		for (T element : source) {
			if (mappingId == null) {
				destinationList.add(mapper.map(element, destinationType));
			} else {
				destinationList.add(mapper.map(element, destinationType,
						mappingId));
			}
		}

		return destinationList;
	}

	/**
	 * Maps an ArrayList of objects given by a DAO.
	 * 
	 * @param source
	 *            source list
	 * @param mapMethod
	 *            mapMethod setted by the DAO
	 * 
	 * @return mapped the mapped list
	 */
	public static <T extends I_Model, U extends AbstractVo> ArrayList<U> map(
			List<? extends T> source, AbstractDao<T, U>.MapMethod mapMethod) {

		// creates the destination list
		ArrayList<U> destinationList = new ArrayList<>();

		// map every element in the source and add it to the list
		for (T element : source) {
			destinationList.add(mapMethod.map(element));
		}

		return destinationList;
	}
}
