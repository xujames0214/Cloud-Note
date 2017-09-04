package cn.tedu.note.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.tedu.note.entity.User;

/**
 * Servlet Filter implementation class AccessFilter
 */
public class AccessFilter implements Filter {

	public void destroy() {
		
	}

	private String login = "/log_in.html";
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		String path = req.getRequestURI();
		//�Ź�log_in.html
		if(path.endsWith(login)){
			chain.doFilter(request, response);
			return;
		}
		
		//�Ź�alert_error.html
		if(path.endsWith("alert_error.html")){
			chain.doFilter(request, response);
			return;
		}
		
		//����û��Ƿ��¼
		User user = (User)session.getAttribute("loginUser");
		//���û�е�¼���ض��򵽵�¼ҳ
		if(user ==  null){//û��¼
			//�ض��򵽵�¼ҳ
			res.sendRedirect(req.getContextPath() + login);
			return;
		}
		//�����¼�ͷŹ�
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
