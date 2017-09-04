package cn.tedu.note.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.note.service.NotebookService;
import cn.tedu.note.util.JsonResult;

@Controller
@RequestMapping("/notebook")
public class NotebookController extends AbstractController{

	@Resource
	private NotebookService notebookService;
	
	@RequestMapping("/list.do")
	@ResponseBody
	public JsonResult list(String userId){
		List<Map<String, Object>> list = notebookService.listNotebooks(userId);
		return new JsonResult(list);
	}
	
	@RequestMapping("/page.do")
	@ResponseBody
	public JsonResult page(String userId,int page){
		List<Map<String, Object>> list = notebookService.listNotebooks(userId,page);
		return new JsonResult(list);
	}
}
