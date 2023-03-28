<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:set var="noticeList" value="${noticeMap.noticeList}" />
<c:set var="section" value="${noticeMap.section}" />
<c:set var="pageNum" value="${noticeMap.pageNum}" />
<c:set var="totalNotice" value="${noticeMap.totalNotice}" />

<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지 목록</title>

<script>
	/* 공지 삭제 */
	function deleteNotice(notice_id) {
		$.ajax({
		    type : "post",
		    async : true, //false인 경우 동기식으로 처리한다.
		    url : "${contextPath}/admin/notice/deleteNotice.do",
		    data: {notice_id:notice_id},
		    success : function(data, textStatus) {
		    	location.reload();
		    	alert("공지를 삭제했습니다.");
		        tr.style.display = 'none';
		   	},
		    error : function(data, textStatus) {
		    	alert("에러가 발생했습니다."+textStatus);
		    },
		    complete : function(data, textStatus) {
		    	//alert("작업을완료 했습니다");
		    }
		    
		}); //end ajax	
	}
	
	function backToList(obj) {
			obj.action = "${contextPath}/admin/notice/listNotice.do";
			obj.submit();
	}
	
</script>

</head>
<body>
	
	
	<div class="notice-div" >
	
		<div class="title_underline">
			<h3><b>공지사항</b></h3>
		</div>
		
		<table class="table table-striped table-hover">
			<thead>
			<tr align="center" style="background:#00BFFE">
				<td width="5%"><b>번호</b></td>
				<td width="65%"><b>제목</b></td>
				<td width="20%"><b>날짜</b></td>
				<td width="10%" align="left"><b>조회수</b></td>
			</tr>
			</thead>
			
			<c:choose>
				<c:when test="${noticeList == null}">
					<tr>
						<td>
							<p>
								<b><span style="font-size: 9pt;">등록된 글이 없습니다.</span></b>
							</p>
						</td>
					</tr>
				</c:when>
		
				<c:when test="${noticeList != null}">
					<c:forEach var="notice" items="${noticeList}" varStatus="noticeNum" >
						<tr>
							<td align="center">
								${notice.rnum}
							</td>
							<td align="center">
								<a href="${contextPath}/admin/notice/viewNotice.do?notice_id=${notice.notice_id}">
									${notice.title}
								</a>
							</td>
							<td align="center">
								${notice.write_date}
							</td>
							<td align="center">
								${notice.cnt}
								<c:if test="${isLogOn==true and memberInfo.member_id == 'admin'}">
									&nbsp;&nbsp;<input type="button" value="X" onClick="deleteNotice('${notice.notice_id}')">
								</c:if>
							</td>
							
						</tr>
					</c:forEach>
				</c:when>
			</c:choose>
		</table>
		
	</div>

	<div class="paging-div">
		<c:if test="${totalNotice != null }">
			<c:choose>
				<c:when test="${totalNotice >100 }">
					<!-- 글 개수가 100 초과인경우 -->
					<c:forEach var="page" begin="1" end="10" step="1">
						<c:if test="${section >1 && page==1 }">
							<a class="no-uline"
								href="${contextPath }/badmin/notice/listNotice.do?section=${section-1}&pageNum=${(section-1)*10 +1 }">&nbsp;
								pre </a>
						</c:if>
						<a class="no-uline"
							href="${contextPath }/admin/notice/listNotice.do?section=${section}&pageNum=${page}">${(section-1)*10 +page }
						</a>
						<c:if test="${page ==10 }">
							<a class="no-uline"
								href="${contextPath }/admin/notice/listNotice.do?section=${section+1}&pageNum=${section*10+1}">&nbsp;
								next</a>
						</c:if>
					</c:forEach>
				</c:when>
				<c:when test="${totalNotice ==100 }">
					<!--등록된 글 개수가 100개인경우  -->
					<c:forEach var="page" begin="1" end="10" step="1">
						<a class="no-uline" href="#">${page } </a>
					</c:forEach>
				</c:when>

				<c:when test="${totalNotice< 100 }">
					<!--등록된 글 개수가 100개 미만인 경우  -->
					<c:forEach var="page" begin="1" end="${totalNotice/10 +1}" step="1">
						<c:choose>
							<c:when test="${page==pageNum }">
								<a class="sel-page"
									href="${contextPath }/admin/notice/listNotice.do?section=${section}&pageNum=${page}">${page }
								</a>
							</c:when>
							<c:otherwise>
								<a class="no-uline"
									href="${contextPath }/admin/notice/listNotice.do?section=${section}&pageNum=${page}">${page }
								</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:when>
			</c:choose>
		</c:if>
		<br>
		<c:if test="${isLogOn==true and memberInfo.member_id =='admin'}">
			<form action="${contextPath}/admin/notice/insertNotice.do">
				<br>
				<input type="submit" value="공지쓰기" class="btn btn-secondary">
			</form>
		</c:if>
	
	</div>
	
	
	
</body>
</html>
