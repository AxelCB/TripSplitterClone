package org.kairos.tripSplitterClone.fx;

import javax.persistence.EntityManager;

import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.web.I_MessageSolver;
import org.kairos.tripSplitterClone.web.MessageSolver;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;

/**
 * Abstract FX implementation of the I_Fx interface
 * 
 * @author fgonzalez
 * 
 */
public abstract class AbstractFxImpl implements I_Fx {

	/**
	 * Logger.
	 */
	public Logger logger = LoggerFactory.getLogger(AbstractFxImpl.class);

	/**
	 * Entity manager.
	 */
	private EntityManager em;

	/**
	 * Gson formatter
	 */
	@Autowired
	private Gson gson;

	/**
	 * Generic VO.
	 */
	private AbstractVo vo;

	/**
	 * Web Context Holder.
	 */
	@Autowired
	private WebContextHolder webContextHolder;

	/**
	 * Message solver (to use if the web context holder will not be available).
	 */
	@Autowired
	private MessageSolver messageSolver;

	/**
	 * Active entity transaction flag.
	 */
	private Boolean wasActive = Boolean.FALSE;

	/**
	 * Checks if the function can be executed.
	 * 
	 * @return an FxValidationResponse
	 */
	protected abstract FxValidationResponse validate();

	/**
	 * Internal execution method.
	 * 
	 * @return
	 */
	protected abstract JsonResponse _execute();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.I_Fx#execute()
	 */
	@Override
	public JsonResponse execute() {
		this.logger.debug("executing fx");

		this.logger.debug("executing fx - validation phase");
		// template method
		FxValidationResponse fxValidationResponse = this.validate();

		if (fxValidationResponse.getOk()) {
			this.logger.debug("executing fx - execution phase");

			return this._execute();
		} else {
			this.logger.debug("executing fx - validation failed");
			return JsonResponse.error(fxValidationResponse.getData(),
					fxValidationResponse.getMessages());
		}
	}

	/**
	 * Starts transaction if it was not active.
	 */
	public void beginTransaction() {
		this.setWasActive(this.getEm().getTransaction().isActive());
		if (!this.getWasActive()) {
			this.getEm().getTransaction().begin();
		}
	}

	/**
	 * Commits transaction if it was not active when fx was called.
	 */
	public void commitTransaction() {
		if (!this.getWasActive()) {
			this.getEm().getTransaction().commit();
		}
	}

	/**
	 * Rollback transaction if it was active.
	 */
	public void rollbackTransaction() {
		if (this.getEm().getTransaction().isActive()) {
			try {
				this.getEm().getTransaction().rollback();
			} catch (Exception e) {
				this.logger.error("error rollbacking transaction", e);
			}
		}
	}

	/**
	 * Gets a new error message with the specified code.
	 * 
	 * @param errorCode
	 *            the error code
	 * 
	 * @return string
	 */
	protected String errorMessage(String errorCode) {
		return this.getWebContextHolder().errorMessage(errorCode);
	}

	/**
	 * Generates a standard error response with a generic message.
	 * 
	 * @return jsonResponse
	 */
	protected JsonResponse unexpectedErrorResponse() {
		return this.getRealMessageSolver().unexpectedErrorResponse();
	}

	/**
	 * 
	 * Generates a standard error response with a generic message.
	 * 
	 * @param errorCode
	 *            the error code to set
	 * 
	 * @return jsonResponse
	 */
	protected JsonResponse unexpectedErrorResponse(String errorCode) {
		return this.getRealMessageSolver().unexpectedErrorResponse(errorCode);
	}

	/**
	 * Gets the real message solver.
	 * 
	 * @return message solver
	 */
	protected I_MessageSolver getRealMessageSolver() {
		try {
			// we try to get something from the context holder (to use the
			// proxy)
			this.getWebContextHolder().getLocale();

			// we got up to here, we can use this as the message solver
			return this.getWebContextHolder();
		} catch (BeanCreationException e) {
			if (e.getBeanName().equals(
					WebContextHolder.WEB_CONTEXT_HOLDER_PROXY_BEAN_NAME)) {
				// the exception was thrown, we use the non threat-bound message
				// solver
				return this.getMessageSolver();
			} else {
				// this is another kind of error, move along!
				throw e;
			}
		}
	}

	/**
	 * @return the em
	 */
	@Override
	public EntityManager getEm() {
		return this.em;
	}

	/**
	 * @param em
	 *            the em to set
	 */
	@Override
	public void setEm(EntityManager em) {
		this.em = em;
	}

	/**
	 * @return the gson
	 */
	public Gson getGson() {
		return this.gson;
	}

	/**
	 * @param gson
	 *            the gson to set
	 */
	public void setGson(Gson gson) {
		this.gson = gson;
	}

	/**
	 * @return the vo
	 */
	public AbstractVo getVo() {
		return this.vo;
	}

	/**
	 * @param vo
	 *            the vo to set
	 */
	@Override
	public void setVo(AbstractVo vo) {
		this.vo = vo;
	}

	/**
	 * @return the webContextHolder
	 */
	public WebContextHolder getWebContextHolder() {
		return this.webContextHolder;
	}

	/**
	 * @param webContextHolder
	 *            the webContextHolder to set
	 */
	public void setWebContextHolder(WebContextHolder webContextHolder) {
		this.webContextHolder = webContextHolder;
	}

	/**
	 * @return the messageSolver
	 */
	public MessageSolver getMessageSolver() {
		return this.messageSolver;
	}

	/**
	 * @param messageSolver
	 *            the messageSolver to set
	 */
	public void setMessageSolver(MessageSolver messageSolver) {
		this.messageSolver = messageSolver;
	}

	/**
	 * @return the wasActive
	 */
	public Boolean getWasActive() {
		return this.wasActive;
	}

	/**
	 * @param wasActive the wasActive to set
	 */
	public void setWasActive(Boolean wasActive) {
		this.wasActive = wasActive;
	}

}
