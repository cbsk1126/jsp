package webMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import admin.complaint.ComplaintVO;
import admin.review.ReviewVO;
import common.GetConn;
import member.MemberVO;

public class WebMessageDAO {
	private Connection conn = GetConn.getConn();
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private String sql = "";
	private WebMessageVO vo = null;
	
	public void pstmtClose() {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {}
		}
	}
	
	public void rsClose() {
		if(rs != null) {
			try {
				rs.close();
			} catch (Exception e) {} 
			finally {
				pstmtClose();
			}
		}
	}

	// 메세지 전체 리스트보기
	public ArrayList<WebMessageVO> getMessagList(String mid, int mSw) {
		ArrayList<WebMessageVO> vos = new ArrayList<WebMessageVO>();
		try {
			if(mSw == 1 ) {	// 받은 메세지(처음에는 전체메세지(새메세지+읽은메세지)
				sql = "select *,timestampdiff(hour, sendDate, now()) as hour_diff from webMessage where receiveId=? and (receiveSw='n' or receiveSw='r') order by idx desc";
			}
			else if(mSw == 2 ) {	// 신규메세지
				sql = "select *,timestampdiff(hour, sendDate, now()) as hour_diff from webMessage where receiveId=? and receiveSw='n' order by idx desc";
			}
			else if(mSw == 3 ) {	// 보낸메세지
				sql = "select *,timestampdiff(hour, sendDate, now()) as hour_diff from webMessage where sendId=? and sendSw='s' order by idx desc";
			}
			else if(mSw == 4 ) {	// 보낸메세지
				sql = "select *,timestampdiff(hour, sendDate, now()) as hour_diff from webMessage where sendId=? and receiveSw='n' order by idx desc";
			}
			else if(mSw == 5 ) {	// 휴지통
				sql = "select *,timestampdiff(hour, sendDate, now()) as hour_diff from webMessage where (receiveId=? and receiveSw='g') or (sendId=? and sendSw='g') order by idx desc";
			}
			else {	// mSW가 0(메세지작성), 6(내용보기)
				return vos;
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mid);
			if(mSw == 5) {
				pstmt.setString(2, mid);
			}
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				vo = new WebMessageVO();
				vo.setIdx(rs.getInt("idx"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setSendId(rs.getString("sendId"));
				vo.setSendSw(rs.getString("sendSw"));
				vo.setSendDate(rs.getString("sendDate"));
				vo.setReceiveId(rs.getString("receiveId"));
				vo.setReceiveSw(rs.getString("receiveSw"));
				vo.setReceiveDate(rs.getString("receiveDate"));
				
				vo.setHour_diff(rs.getInt("hour_diff"));
				
				vos.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("SQL오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		return vos;
	}

	// 메세지 작성처리
	public int setWmInputOk(WebMessageVO vo) {
		int res = 0;
		try {
			sql = "insert into webMessage values (default,?,?,?,default,default,?,default,default)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getSendId());
			pstmt.setString(4, vo.getReceiveId());
			res = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL오류 : " + e.getMessage());
		} finally {
			pstmtClose();
		}
		return res;
	}

	// 1건의 메세지 내용보기
	public WebMessageVO getWebMessageContent(int idx, int mFlag) {
		WebMessageVO vo = new WebMessageVO();
		try {
			if(mFlag == 11) {	// 받은메세지에서 신규메세지 클릭하면 n 을 r으로 변경하기
				sql = "update webMessage set receiveSw='r', receiveDate=now() where idx=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, idx);
				pstmt.executeUpdate();
				pstmtClose();
			}
			sql = "select * from webMessage where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			rs.next();
			
			vo.setIdx(rs.getInt("idx"));
			vo.setTitle(rs.getString("title"));
			vo.setContent(rs.getString("content"));
			vo.setSendId(rs.getString("sendId"));
			vo.setSendSw(rs.getString("sendSw"));
			vo.setSendDate(rs.getString("sendDate"));
			vo.setReceiveId(rs.getString("receiveId"));
			vo.setReceiveSw(rs.getString("receiveSw"));
			vo.setReceiveDate(rs.getString("receiveDate"));
		} catch (SQLException e) {
			System.out.println("SQL오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		return vo;
	}

	public int wmDeleteCheck(int idx, int mSw) {
		int res = 0;
		try {
			if(mSw == 11) {	// mSw(=mFlag)값이 11은 받은편지함에서 휴지통으로 보낸경우
			  sql = "update webMessage set receiveSw='g' where idx = ?";
			}
			else {
				sql = "update webMessage set sendSw='x' where idx = ?";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			res = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL오류 : " + e.getMessage());
		} finally {
			pstmtClose();
		}
		return res;
	}

	// 휴지통에 들어있는 모든 자료들을 삭제(update)처리한다. 이때 receiveSw와 sendSw가 모두 'x'이면 실제 자료를 삭제(delete)처리한다.
	public int wmDeleteAll(String mid) {
		int res = 0;
		try {
			// 받은메세지에서의 휴지통처리시에 수행할 sql문
			sql = "update webMessage set receiveSw = 'x' where receiveId = ? and receiveSw = 'g'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mid);
			pstmt.executeUpdate();
			pstmt.close();
			
			// 보낸메세지에서의 휴지통처리시에 수행할 sql문
			sql = "update webMessage set sendSw = 'x' where sendId = ? and sendSw = 'g'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mid);
			pstmt.executeUpdate();
			pstmt.close();
			
			sql = "delete from webMessage where sendSw = 'x' and receiveSw = 'x'";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			
			res = 1;
		} catch (SQLException e) {
			System.out.println("SQL오류 : " + e.getMessage());
		} finally {
			pstmtClose();
		}
		return res;
	}

}
