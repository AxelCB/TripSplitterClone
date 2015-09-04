package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.kairos.tripSplitterClone.controller.DestinationCtrl;
import org.kairos.tripSplitterClone.controller.TripCtrl;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.destination.I_CityDao;
import org.kairos.tripSplitterClone.dao.destination.I_CountryDao;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.kairos.tripSplitterClone.vo.destination.DestinationVo;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/mainContext.xml","classpath:spring/servletContext.xml"})
@TestExecutionListeners({
		ServletTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class
})
public class DestinationTest extends AbstractTestNGSpringContextTests {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(DestinationTest.class);

	@Autowired
	private DestinationCtrl destinationCtrl;

	@Autowired
	private Gson gson;

	@Autowired
	private EntityManagerHolder entityManagerHolder;

	@Autowired
	private I_CountryDao countryDao;

	@Autowired
	private I_CityDao cityDao;

	@BeforeClass(dependsOnMethods={"springTestContextPrepareTestInstance"})
	private void initialize() {
		EntityManager em=null,testEm = null;
		try{
			testEm = this.getEntityManagerHolder().getTestEntityManager();
			em = this.getEntityManagerHolder().getEntityManager();

			List<CityVo> cities = this.getCityDao().listAll(testEm);
			List<CountryVo> countries = this.getCountryDao().listAll(testEm);

			for(CityVo cityVo : cities){
				if(StringUtils.isNotBlank(cityVo.getName())){
					CityVo city = this.getCityDao().findByName(em,cityVo.getName());
					if(city!=null){
						this.getCityDao().delete(em,city);
					}
				}
			}

			for(CountryVo countryVo : countries){
				if(StringUtils.isNotBlank(countryVo.getName())) {
					CountryVo country = this.getCountryDao().findByName(em, countryVo.getName());
					if (country != null) {
						this.getCountryDao().delete(em, country);
					}
				}
			}

			//TODO que hago acá?

		}catch(Exception ex){
			this.logger.debug("Destination test could not be initialized",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
			this.getEntityManagerHolder().closeEntityManager(testEm);
		}
	}

	@Test(groups = {"destination"})
	public void createCityTest(){
		EntityManager em=null,testEm = null;
		try{
			testEm = this.getEntityManagerHolder().getTestEntityManager();
			em = this.getEntityManagerHolder().getEntityManager();

			List<CityVo> cityVoList = this.getCityDao().listAll(testEm);

			Long amountOfCities,lastAmountOfCities = this.getCityDao().countAll(em);

			for(CityVo cityVo : cityVoList){
				cityVo.setId(null);
				JsonResponse response = this.getGson().fromJson(this.getDestinationCtrl().createCity(this.getGson().toJson(cityVo)), JsonResponse.class);
				if(response.getOk()){
					amountOfCities = this.getCityDao().countAll(em);
					CityVo persistedCityVo = this.getCityDao().listAll(em).get(amountOfCities.intValue()-1);
					assert lastAmountOfCities.equals(amountOfCities):"City not persisted correctly id:"+cityVo.getId();
                    if(persistedCityVo!=null){
                        assert persistedCityVo.getName().equals(cityVo.getName()):"City name not persisted correctly";
                    }
					lastAmountOfCities = amountOfCities;
				}else{
                    assert (cityVo.getName()==null || cityVo.getName().equals(""))||response.getOk():"City name shouldn't be null or persisted";
				}
			}
			if(cityVoList.size()>0){
				JsonResponse response = this.getGson().fromJson(this.getDestinationCtrl().createCity(this.getGson().toJson(cityVoList.get(0))), JsonResponse.class);
				if(response.getOk()){
					assert !this.getCityDao().checkNameUniqueness(em,cityVoList.get(0).getName(),null):"Shouldn't persist this city; not unique name";
				}
			}

		}catch(Exception ex){
			this.logger.debug("Destination test failed running create city test",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
			this.getEntityManagerHolder().closeEntityManager(testEm);
		}
	}

    @Test(groups = {"destination"})
    public void createCountryTest(){
        EntityManager em=null,testEm = null;
        try{
            testEm = this.getEntityManagerHolder().getTestEntityManager();
            em = this.getEntityManagerHolder().getEntityManager();

            List<CountryVo> countryVoList = this.getCountryDao().listAll(testEm);

            Long amountOfCountries,lastAmountOfCountries = this.getCityDao().countAll(em);

            for(CountryVo countryVo :  countryVoList){
	            countryVo.setId(null);
                JsonResponse response = this.getGson().fromJson(this.getDestinationCtrl().createCountry(this.getGson().toJson(countryVo)), JsonResponse.class);
                if(response.getOk()){
                    amountOfCountries = this.getCountryDao().countAll(em);
                    CountryVo persistedCountryVo = this.getCountryDao().listAll(em).get(amountOfCountries.intValue()-1);
                    assert lastAmountOfCountries.equals(amountOfCountries):"Country not persisted correctly id:"+ countryVo.getId();
                    if(persistedCountryVo!=null){
                        assert persistedCountryVo.getName().equals( countryVo.getName()):"Country name not persisted correctly";
                    }
                    lastAmountOfCountries = amountOfCountries;
                }else{
                    assert ( countryVo.getName()==null ||  countryVo.getName().equals(""))||response.getOk():"Country name shouldn't be null or persisted";
                }
            }
			if(countryVoList.size()>0){
				JsonResponse response = this.getGson().fromJson(this.getDestinationCtrl().createCity(this.getGson().toJson(countryVoList.get(0))), JsonResponse.class);
				if(response.getOk()){
					assert !this.getCountryDao().checkNameUniqueness(em,countryVoList.get(0).getName(),null):"Shouldn't persist this country; not unique name";
				}
			}
        }catch(Exception ex){
	        this.logger.debug("Destination test failed running create country test",ex);
	        throw ex;
        }finally{
            this.getEntityManagerHolder().closeEntityManager(em);
	        this.getEntityManagerHolder().closeEntityManager(testEm);
        }
    }

//	@Test(groups = {"destination"})
//	public void listDestinationsTest(){
//		EntityManager em = null;
//		try{
//			em = this.getEntityManagerHolder().getEntityManager();
//
//			Type type = new TypeToken<DestinationVo>() {}.getType();
//
//			List<DestinationVo> destinations = this.getGson().fromJson(this.getDestinationCtrl().listDestinations(""), type);
//
//			List<CityVo> cities = this.getCityDao().listAll(em);
//			List<CountryVo> countries = this.getCountryDao().listAll(em);
//
//			Long amountOfCities = this.getCityDao().countAll(em);
//			Long amountOfCountries = this.getCountryDao().countAll(em);
//			Long amountOfListedCities=0l;
//			Long amountOfListedCountries=0l;
//
//			for(DestinationVo destinationVo : destinations){
//				for (CityVo cityVo : cities){
//					if(cityVo.getName().equals(destinationVo.getCityName())){
//						amountOfListedCities++;
//					}
//				}
//				for (CountryVo countryVo : countries){
//					if(countryVo.getName().equals(destinationVo.getCountryName())){
//						amountOfListedCities++;
//					}
//				}
//			}
//			assert amountOfCities.equals(amountOfListedCities):"Amount of listed cities isn't the same as the ones in the DB";
//			assert amountOfCountries.equals(amountOfListedCountries):"Amount of listed cities isn't the same as the ones in the DB";
//
//		}catch(Exception ex){
//			this.logger.debug("Destination test failed running list destinations test",ex);
//			throw ex;
//		}finally{
//			this.getEntityManagerHolder().closeEntityManager(em);
//		}
//	}

	@Test(groups = {"destination"})
	public void listCitiesTest(){
		EntityManager em = null;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			Type type = new TypeToken<List<CityVo>>() {}.getType();

			JsonResponse response = this.getGson().fromJson(this.getDestinationCtrl().listCities(""),JsonResponse.class);

			List<CityVo> citiesVo = this.getGson().fromJson(response.getData(), type);

			List<CityVo> cities = this.getCityDao().listAll(em);

			Long amountOfCities = this.getCityDao().countAll(em);
			Long amountOfListedCities=0l;

			for(CityVo cityVo : citiesVo){
				for (CityVo city : cities){
					if(cityVo.getName().equals(city.getName())&& cityVo.equals(city)){
						amountOfListedCities++;
					}
				}
			}
			assert amountOfCities.equals(amountOfListedCities):"Amount of listed cities isn't the same as the ones in the DB";
		}catch(Exception ex){
			this.logger.debug("Destination test failed running list cities test",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}
	@Test(groups = {"destination"})
	public void listCountriesTest(){
		EntityManager em = null;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			Type type = new TypeToken<List<CountryVo>>() {}.getType();

			JsonResponse response = this.getGson().fromJson(this.getDestinationCtrl().listCountries(""), JsonResponse.class);

			List<CountryVo> countriesVo = this.getGson().fromJson(response.getData(), type);

			List<CountryVo> countries = this.getCountryDao().listAll(em);

			Long amountOfCountries = this.getCountryDao().countAll(em);
			Long amountOfListedCountries=0l;

			for(CountryVo countryVo : countriesVo){
				for (CountryVo country : countries){
					if(countryVo.getName().equals(country.getName())&& countryVo.equals(country)){
						amountOfListedCountries++;
					}
				}
			}
			assert amountOfCountries.equals(amountOfListedCountries):"Amount of listed countries isn't the same as the ones in the DB";

		}catch(Exception ex){
			this.logger.debug("Destination test failed running list countries test",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public EntityManagerHolder getEntityManagerHolder() {
		return entityManagerHolder;
	}

	public void setEntityManagerHolder(EntityManagerHolder entityManagerHolder) {
		this.entityManagerHolder = entityManagerHolder;
	}

    public DestinationCtrl getDestinationCtrl() {
        return destinationCtrl;
    }

    public void setDestinationCtrl(DestinationCtrl destinationCtrl) {
        this.destinationCtrl = destinationCtrl;
    }

    public I_CountryDao getCountryDao() {
        return countryDao;
    }

    public void setCountryDao(I_CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    public I_CityDao getCityDao() {
        return cityDao;
    }

    public void setCityDao(I_CityDao cityDao) {
        this.cityDao = cityDao;
    }
}
