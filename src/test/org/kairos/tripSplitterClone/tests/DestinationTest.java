package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import org.springframework.beans.factory.annotation.Autowired;
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
public class DestinationTest {

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

	private HttpSession session;

	@BeforeTest
	private void initialize() {
		EntityManager em=null,testEm = null;
		try{
			testEm = this.getEntityManagerHolder().getTestEntityManager();
			em = this.getEntityManagerHolder().getEntityManager();

			//TODO list all cities and countries from test and delete them from the database

			//TODO que hago acá?

		}catch(Exception ex){
			//TODO log this exception
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
		}catch(Exception ex){
			//TODO log this exception
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
        }catch(Exception ex){
            //TODO log this exception
        }finally{
            this.getEntityManagerHolder().closeEntityManager(em);
	        this.getEntityManagerHolder().closeEntityManager(testEm);
        }
    }

	@Test(groups = {"destination"})
	public void listDestinationsTest(){
		EntityManager em = null;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			Type type = new TypeToken<DestinationVo>() {}.getType();

			List<DestinationVo> destinations = this.getGson().fromJson(this.getDestinationCtrl().listDestinations(""), type);

			List<CityVo> cities = this.getCityDao().listAll(em);
			List<CountryVo> countries = this.getCountryDao().listAll(em);

			Long amountOfCities = this.getCityDao().countAll(em);
			Long amountOfCountries = this.getCountryDao().countAll(em);
			Long amountOfListedCities=0l;
			Long amountOfListedCountries=0l;

			for(DestinationVo destinationVo : destinations){
				for (CityVo cityVo : cities){
					if(cityVo.getName().equals(destinationVo.getCityName())){
						amountOfListedCities++;
					}
				}
				for (CountryVo countryVo : countries){
					if(countryVo.getName().equals(destinationVo.getCountryName())){
						amountOfListedCities++;
					}
				}
			}
			assert amountOfCities.equals(amountOfListedCities):"Amount of listed cities isn't the same as the ones in the DB";
			assert amountOfCountries.equals(amountOfListedCountries):"Amount of listed cities isn't the same as the ones in the DB";

		}catch(Exception ex){
			//TODO log this exception
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
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
