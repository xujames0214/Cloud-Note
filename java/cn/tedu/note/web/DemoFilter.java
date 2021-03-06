package cn.tedu.note.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class DemoFilter
 */
public class DemoFilter implements Filter {
	
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//从请求中获取请求的URL
		HttpServletRequest req = (HttpServletRequest)request;
		String url = req.getRequestURI();
	//	System.out.println("filter:" + url);
		//调用后续的WEB请求
		chain.doFilter(request, response);
	}

	public void destroy() {
		
	}

}
