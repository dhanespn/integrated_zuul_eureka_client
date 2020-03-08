package com.dpn.tryjava.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;

public class PreFilter extends ZuulFilter {

	Logger log = LoggerFactory.getLogger(PreFilter.class);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		StringBuffer strLog = new StringBuffer();
		strLog.append("\n------ NUEVA PETICION ------\n");
		strLog.append(String.format("Server: %s Metodo: %s Path: %s \n", ctx.getRequest().getServerName(), ctx.getRequest().getMethod(),
				ctx.getRequest().getRequestURI()));
		Enumeration<String> enume = ctx.getRequest().getHeaderNames();
		String header;
		while (enume.hasMoreElements()) {
			header = enume.nextElement();
			strLog.append(String.format("Headers: %s = %s \n", header, ctx.getRequest().getHeader(header)));
		}
		;
		log.info(strLog.toString());
		return null;

	}
}