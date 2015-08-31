package org.kairos.tripSplitterClone.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.kairos.tripSplitterClone.vo.trip.UserTripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory Bean for Gson Serializer/Deserializer
 *
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class GsonSpringFactoryBean implements FactoryBean<Gson> {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory
			.getLogger(GsonSpringFactoryBean.class);

	/**
	 * Singleton Instance.
	 */
	private Gson singleton = null;

	/**
	 * Big Decimal Utils.
	 */
//	@Autowired
//	private I_BigDecimalUtils bigDecimalUtils;

	/**
	 * Date Utils.
	 */
//	@Autowired
//	private I_DateUtils dateUtils;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Gson getObject() throws Exception {
		if (this.singleton == null) {
			// we create the GsonBuilder
			GsonBuilder gsb = new GsonBuilder();

			// sets the date format
			gsb.setDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

			// type adapters registration
//			gsb.registerTypeAdapter(BigDecimal.class,
//					new BigDecimalTypeAdapter(this.bigDecimalUtils));

			// exclude password from being exposed to the client
			gsb.addSerializationExclusionStrategy(new CustomExclusionStrategy(
					UserVo.class, false, "password"));

			// user vo trips circular reference avoidance
			gsb.addSerializationExclusionStrategy(new CustomExclusionStrategy(
					UserVo.class, false, "trips"));

			// user vo trips circular reference avoidance
			gsb.addSerializationExclusionStrategy(new CustomExclusionStrategy(
					UserTripVo.class, false, "trip"));

			// serializes complex map keys
			gsb.enableComplexMapKeySerialization();

			// returns the created builder
			this.singleton = gsb.create();
		}

		// returns the singleton
		return this.singleton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return Gson.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @return the bigDecimalUtils
	 */
//	public I_BigDecimalUtils getBigDecimalUtils() {
//		return this.bigDecimalUtils;
//	}

	/**
	 * @param bigDecimalUtils
	 *            the bigDecimalUtils to set
	 */
//	public void setBigDecimalUtils(I_BigDecimalUtils bigDecimalUtils) {
//		this.bigDecimalUtils = bigDecimalUtils;
//	}

	/**
	 * @return the dateUtils
	 */
//	public I_DateUtils getDateUtils() {
//		return this.dateUtils;
//	}

	/**
	 * @param dateUtils
	 *            the dateUtils to set
	 */
//	public void setDateUtils(I_DateUtils dateUtils) {
//		this.dateUtils = dateUtils;
//	}

}
