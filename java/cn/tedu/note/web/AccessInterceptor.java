package cn.tedu.note.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.tedu.note.entity.User;
import cn.tedu.note.util.JsonResult;

@Component
public class AccessInterceptor implements HandlerInterceptor {

	public AccessInterceptor() {
		
	}

	public boolean preHandle(HttpServletRequest req,
			HttpServletResponse res, Object handler)
			throws Exception {
		String path = req.getRequestURI();
		//System.out.println("Interceptor:" + path);
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("loginUser");
		if(user == null){
			JsonResult result = new JsonResult("需要重新登录！");
			//利用response返回结果
			res.setContentType("application/json;charset=UTF-8");
			res.setCharacterEncoding("UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(result);
			res.getWriter().println(json);
			res.flushBuffer();
			return false;
		}
		
		return true;//放过请求
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
