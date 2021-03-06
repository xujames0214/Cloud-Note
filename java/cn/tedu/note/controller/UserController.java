package cn.tedu.note.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.note.entity.User;
import cn.tedu.note.service.PasswordException;
import cn.tedu.note.service.UserNameException;
import cn.tedu.note.service.UserNotFoundException;
import cn.tedu.note.service.UserService;
import cn.tedu.note.util.JsonResult;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController{
	@Resource
	private UserService userService;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public Object login(
			String name,
			String password,
			HttpSession session){
		User user = userService.login(name, password);
		//登录成功的时候，将user信息保存到session
		//用于在过滤器中检查登录情况
		session.setAttribute("loginUser", user);
		return new JsonResult(user);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	public JsonResult handleUserNotFound(UserNotFoundException e){
		e.printStackTrace();
		return new JsonResult(2,e);
	}
	
	@ExceptionHandler(PasswordException.class)
	@ResponseBody
	public JsonResult handlePassword(
			PasswordException e){
		e.printStackTrace();
		return new JsonResult(3,e);
	}
	
	@ExceptionHandler(UserNameException.class)
	@ResponseBody
	public JsonResult handleUserName(UserNameException e){
		e.printStackTrace();
		return new JsonResult(4,e);
	}
	
	
	@RequestMapping("/regist.do")
	@ResponseBody
	public JsonResult regist(String name,String nick,String password,String confirm){
		User user = userService.regist(name.trim(), nick, password, confirm);
		return new JsonResult(user);
	}
	
	@RequestMapping(value="image.do",produces="image/png")
	@ResponseBody
	public byte[] image() throws IOException{
		return createPng();
	}
	
	@RequestMapping(value="/downloadimg.do",
			produces="application/octet-stream")
		@ResponseBody
	public byte[] downloadimg(HttpServletResponse res) 
		throws IOException{
		//参考: 19.5.1 Content-Disposition
		//Content-Disposition: attachment; filename="fname.ext"
		res.setHeader("Content-Disposition",
			"attachment; filename=\"demo.png\"");
		return createPng();
	}

	private byte[] createPng() throws IOException{
		BufferedImage img = new BufferedImage(200,80,BufferedImage.TYPE_3BYTE_BGR);
		img.setRGB(100, 40, 0xffffff);
		//将图片编码为PNG
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "png", out);
		out.close();
		byte[] png = out.toByteArray();
		return png;
	}
	
	
	@RequestMapping(value="/downloadexcel.do",
			produces="application/octet-stream")
		@ResponseBody
	public byte[] excel(HttpServletResponse res) 
		throws IOException{
		//参考: 19.5.1 Content-Disposition
		//Content-Disposition: attachment; filename="fname.ext"
		res.setHeader("Content-Disposition",
			"attachment; filename=\"demo.xls\"");
		return createExcel();
	}
	
	
	private byte[] createExcel() throws IOException{
		//创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建工作表
		HSSFSheet sheet = workbook.createSheet("Demo");
		//在工作表中创建数据行
		HSSFRow row = sheet.createRow(0);
		//创建行中的格子
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("Hello World!");
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		out.close();
		return out.toByteArray();
	}
	
	@RequestMapping("/upload.do")
	@ResponseBody
	public JsonResult upload(
			MultipartFile userfile1,
			MultipartFile userfile2) throws IllegalStateException, IOException{
		//Spring MVC中可以利用MultipartFile接收上载的文件！
		//文件中的一切数据都可以从Multipart对象中找到
		String file1 = userfile1.getOriginalFilename();
		String file2 = userfile2.getOriginalFilename();
		
		System.out.println(file1);
		System.out.println(file2);
		
		//保存文件的3种方法
		//1. transferTo(目标文件),将文件直接保存到目标文件,可以处理大文件
		//2. userfile1.getBytes(),获取文件的全部内容,适合处理小文件！！！
		//3. userfile1.getInputStream(),获取上载文件的流
		
		//保存的目标文件夹
		File dir = new File("D:/demo");
		dir.mkdir();
		File f1 = new File(dir,file1);
		File f2 = new File(dir,file2);
		//第一种保存方式
		//userfile1.transferTo(f1);
		//userfile2.transferTo(f2);
		
		//第三种
		/*InputStream in1 = userfile1.getInputStream();
		FileOutputStream out1 = new FileOutputStream(f1);
		int b;
		while((b = in1.read()) != -1){
			out1.write(b);
		}*/
		return new JsonResult(true);
	}
}
