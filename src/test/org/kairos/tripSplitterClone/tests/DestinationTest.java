package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.controller.DestinationCtrl;
import org.kairos.tripSplitterClone.controller.TripCtrl;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.destination.I_CityDao;
import org.kairos.tripSplitterClone.dao.destination.I_CountryDao;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
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
		EntityManager em = null;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			UserVo sessionUser = (UserVo)this.getSession().getAttribute("loggedUser");

			//TODO que hago acá?

		}catch(Exception ex){
			//TODO log this exception
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	@Test(groups = {"destination"})
	public void createCityTest(){
		EntityManager em=null,testEm = null;
		try{
			testEm = this.getEntityManagerHolder().getTestEntityManager();
			em = this.getEntityManagerHolder().getEntityManager();

			UserVo sessionUser = (UserVo)this.getSession().getAttribute("loggedUser");

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
		}
	}

    @Test(groups = {"destination"})
    public void createCountryTest(){
        EntityManager em=null,testEm = null;
        try{
            testEm = this.getEntityManagerHolder().getTestEntityManager();
            em = this.getEntityManagerHolder().getEntityManager();

            UserVo sessionUser = (UserVo)this.getSession().getAttribute("loggedUser");

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
