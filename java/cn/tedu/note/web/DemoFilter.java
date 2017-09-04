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
		//�������л�ȡ�����URL
		HttpServletRequest req = (HttpServletRequest)request;
		String url = req.getRequestURI();
	//	System.out.println("filter:" + url);
		//���ú�����WEB����
		chain.doFilter(request, response);
	}

	public void destroy() {
		
	}

}
