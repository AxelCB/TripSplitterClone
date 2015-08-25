package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.controller.UserCtrl;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.user.I_UserDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public class UserTest {

	@Autowired
	private UserCtrl userCtrl;

	@Autowired
	private Gson gson;

	@Autowired
	private EntityManagerHolder entityManagerHolder;

	@Autowired
	private I_UserDao userDao;

	@BeforeTest
	private void initialize() {
		EntityManager em = null,testEm = null;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			List<UserVo> userVoList = this.getUserDao().listAll(testEm);

			for(UserVo userVo : userVoList){
				this.getUserDao().delete(em,userVo);
			}
			//TODO que hago acá?

		}catch(Exception ex){
			//TODO log this exception
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	@Test(groups = {"user"})
	public void registerUserTest(){
		EntityManager em=null,testEm = null;
		try{
			testEm = this.getEntityManagerHolder().getTestEntityManager();
			em = this.getEntityManagerHolder().getEntityManager();

			List<UserVo> userVoList = this.getUserDao().listAll(testEm);

			Long amountOfUsers,lastAmountOfUsers = this.getUserDao().countAll(em);

			for(UserVo userVo : userVoList){
				JsonResponse response = this.getGson().fromJson(this.getUserCtrl().register(this.getGson().toJson(userVo)), JsonResponse.class);
				if(response.getOk()){
					amountOfUsers = this.getUserDao().countAll(em);
					UserVo persistedUserVo = this.getUserDao().listAll(em).get(amountOfUsers.intValue()-1);
					assert lastAmountOfUsers.equals(amountOfUsers):"User not persisted correctly id:"+userVo.getId();
					if(persistedUserVo!=null){
						assert persistedUserVo.getName().equals(userVo.getName()):"User's name wasn't persisted correctly id:"+userVo.getId();
						assert persistedUserVo.getEmail().equals(userVo.getEmail()):"User's email wasn't persisted correctly id:"+userVo.getId();
						lastAmountOfUsers = amountOfUsers;
						assert persistedUserVo.getPassword().equals(userVo.getPassword()):"User's password wasn't persisted correctly id:"+userVo.getId();
					}
				}else{
					Boolean ok = userVo.getName()!=null && !userVo.getName().equals("");
					ok = ok && userVo.getEmail()!=null && !userVo.getEmail().equals("");
					ok = ok && userVo.getPassword()!=null && !userVo.getPassword().equals("");
					assert ok:"User not persisted and none null nor blank fields";
				}
			}
		}catch(Exception ex){
			//TODO log this exception
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
			this.getEntityManagerHolder().closeEntityManager(testEm);
		}
	}

	@Test(groups = {"trip"})
	public void loginUser(){
		EntityManager em=null;
		try {
			em = this.getEntityManagerHolder().getEntityManager();

			List<UserVo> persistedUsers = this.getUserDao().listAll(em);

			UserVo persistedUserVo = persistedUsers.get(persistedUsers.size()-1);

			//Empty user
			UserVo userVo = new UserVo();
			JsonResponse response = this.getGson().fromJson(this.getUserCtrl().login(this.getGson().toJson(userVo)), JsonResponse.class);
			assert (!response.getOk()):"Returned as if logged in correctly when data was incomplete. User:"+userVo.toString();

			//Only email
			userVo.setEmail(persistedUserVo.getEmail());
			response = this.getGson().fromJson(this.getUserCtrl().login(this.getGson().toJson(userVo)), JsonResponse.class);
			assert (!response.getOk()):"Returned as if logged in correctly when data was incomplete. User:"+userVo.toString();

			//Only password
			userVo = new UserVo();
			userVo.setPassword(persistedUserVo.getPassword());
			response = this.getGson().fromJson(this.getUserCtrl().login(this.getGson().toJson(userVo)), JsonResponse.class);
			assert (!response.getOk()):"Returned as if logged in correctly when data was incomplete. User:"+userVo.toString();

			//Email and password (name doesn't really matter)
			userVo = persistedUserVo;
			response = this.getGson().fromJson(this.getUserCtrl().login(this.getGson().toJson(userVo)), JsonResponse.class);
			assert (response.getOk()):"Couldn't login user correctly. User:"+userVo.toString();

		}catch(Exception ex){
			//TODO log this exception
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	public UserCtrl getUserCtrl() {
		return userCtrl;
	}

	public void setUserCtrl(UserCtrl userCtrl) {
		this.userCtrl = userCtrl;
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

	public I_UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(I_UserDao userDao) {
		this.userDao = userDao;
	}
}
