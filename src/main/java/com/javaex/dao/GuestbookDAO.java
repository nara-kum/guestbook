package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestVO;

public class GuestbookDAO {

	// 필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/guestbook_db";
	private String id = "guestbook";
	private String pw = "guestbook";
	
	//생성자
	public void GuestbookDAO() {}
	
	//메소드-gs
	
	//메소드일반
	private void connect() {

		try {
			// 1. JDBC 드라이버 로딩
			Class.forName(driver);
			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

	}

	// 종료-클리어
	private void close() {
		// 5. 자원정리
		try {
			if(rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	//전체 리스트 가져오기
	public List<GuestVO> guestSelect() {
		
		List<GuestVO> guestList = new ArrayList<GuestVO>();
		
		connect();
		
		try {
			
			// 3. SQL준비 / 바인딩 / 실행
			
			String query= "";
			query+="select no ";
			query+="	, name ";
			query+="	, password ";
			query+="	, content "; 
			query+="	, date_format(reg_date,'%Y-%m-%d %H:%i:%s') reg_date "; 
			query+="from guestbook ";
			
			//바인딩
			pstmt = conn.prepareStatement(query);
			
			//실행
			rs = pstmt.executeQuery();
			
			// 4. 결과처리
			while(rs.next()) {
				//resultSet에서 각각의 값을 꺼내서 변수에 담는다
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");
				
				//1개의 VO로 묶는다
				GuestVO guestVO = new GuestVO(no,name,password,content,regDate);
				//VO를 리스트에 추가한다
				guestList.add(guestVO);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		
		return guestList;
	}

	public int guestInsert(GuestVO guestVO) {
		
		int count = -1;
		
		connect();
		
		try {
			
			// 3. SQL준비 / 바인딩 / 실행
			
			String query= "";
			query+="insert into guestbook ";
			query+="values(null,?,?,?,now()) ";
						
			//바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,guestVO.getName());
			pstmt.setString(2,guestVO.getPassword());
			pstmt.setString(3,guestVO.getContent());
			
			//실행하고 처리
			count = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		
		return count;
	}

	public int guestDelete(String password) {
		
		int count = -1;
		
		connect();
		
		try {
			
			// 3. SQL준비 / 바인딩 / 실행
			
			String query= "";
			query+="delete ";
			query+="from guestbook ";
			query+="where password = ? ";
						
			//바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,password);
			
			//실행하고 처리
			count = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		
		return count;
	}
	
	
}
