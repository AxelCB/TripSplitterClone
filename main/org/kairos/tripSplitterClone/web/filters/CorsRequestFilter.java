package org.kairos.tripSplitterClone.web.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter for allowing Cross-Domain requests.
 * 
 * @author fgonzalez
 *
 */
public class CorsRequestFilter implements Filter {
	
	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(CorsRequestFilter.class);

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String path = ((HttpServletRequest) request).getRequestURI();
		if (!path.contains("/cometd")) {
			this.logger.debug("Allowing Cross-Domain access");
			
			HttpServletResponse res = (HttpServletResponse) response;
			
			res.setHeader("Access-Control-Allow-Origin", "*");
			res.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
			res.setHeader("Access-Control-Max-Age", "5000");
			res.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Content-Type");
			
			// sets utf8 as the default encoding
			res.setCharacterEncoding("UTF-8");
		}
		
		chain.doFilter(request, response);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy () {
		//does nothing
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init (FilterConfig arg0) throws ServletException {
		//does nothing
	}

}
