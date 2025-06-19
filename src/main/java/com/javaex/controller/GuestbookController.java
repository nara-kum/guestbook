package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDAO;
import com.javaex.vo.GuestVO;

@WebServlet("/gbc")
public class GuestbookController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    public GuestbookController() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//로직
		System.out.println("GuestbookController");
		
		//action 파라미터의 값을 알아야 함
		
		String action = request.getParameter("action");
		System.out.println(action); //업무구분
		
		if("addlist".equals(action)) {//리스트
			
			System.out.println("리스트");
			
			//db데이터 가져오기
			GuestbookDAO guestbookDAO = new GuestbookDAO();
			List<GuestVO> guestList = guestbookDAO.guestSelect();
			
			System.out.println(guestList);
			
			//html + List
			
			//jsp에게 넘긴다
			//(1)request객체에 데이터를 넣어준다
			request.setAttribute("guestList",guestList);
			
			//(2)list.jsp에 request객체와 response객체를 보낸다
			//포워드
			RequestDispatcher rd = request.getRequestDispatcher("/addlist.jsp");
			rd.forward(request, response);
			
		} else if("write".equals(action)) {//리스트
			
			System.out.println("등록");
			
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");
			
			GuestVO guestVO  = new GuestVO(name,password,content);
			
			GuestbookDAO guestbookDAO = new GuestbookDAO();
			guestbookDAO.guestInsert(guestVO);
			
			//리다이렉트
			response.sendRedirect("http://localhost:8080/guestbook/gbc?action=addlist");
			
		}else if("dForm".equals(action)) {//삭제폼으로
			
			System.out.println("삭제폼");
						
			RequestDispatcher rd = request.getRequestDispatcher("/deleteForm.jsp");
			rd.forward(request, response);
			
		}else if("delete".equals(action)) {//삭제폼으로
			
			System.out.println("삭제");

			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("password");
			
			GuestbookDAO guestbookDAO = new GuestbookDAO();
			guestbookDAO.guestDelete(no,password);
			
			//리다이렉트
			response.sendRedirect("http://localhost:8080/guestbook/gbc?action=addlist");
			
			
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
