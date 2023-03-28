<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="notice" value="${noticeMap.noticeVO}" />

<%
  request.setCharacterEncoding("UTF-8");
%>

<head>
<meta charset="UTF-8">
<title>공지상세</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	
	function fn_update_notice(obj){
		obj.action="${contextPath}/admin/notice/updateNotice.do";
		obj.submit();
	}
	
	function fn_enable(obj){
		document.getElementById("i_title").disabled=false;
		document.getElementById("i_content").disabled=false;
	}
	
	function backToList(obj){
    	obj.action="${contextPath}/admin/notice/listNotice.do";
    	obj.submit();
    }
	
</script>

</head>
<body>

	
	<main class="mt-4 pt-2">
		<div class="container-fluid px-4" style="width:800px">
			<div class="title_underline">
				<h3 class="mt-4">공지사항</h3>
			</div>
			<div class="card mb-4">
				<div class="card-body">
					<form name="formNotice" method="post" action="${contextPath}" enctype="multipart/form-data">
						<div class="mb-3 mt-3">
							<label for="bno" class="form-label"><span>글번호</span></label> 
							<input type="text" class="form-control" value="${notice.notice_id}" disabled>
							<input type="hidden" name="notice_id" value="${notice.notice_id}"/>
						</div>
						<div class="mb-3">
							<label for="title" class="form-label"><span>제목</span></label> 
							<input type="text" class="form-control" id="i_title" name="title" value="${notice.title}" disabled>
						</div>
						<div class="mb-3">
							<label for="content" class="form-label"><span>내용</span></label>
							<textarea rows="10" cols="40" class="form-control" id="i_content" name="content" disabled>${notice.content}</textarea>
							<c:if test="${notice.imageFileName!=null}">
								<br>
								<img src="${contextPath}/download1.do?notice_id=${notice.notice_id}&imageFileName=${notice.imageFileName}" id="preview" style="max-width:718.67px; border-radius:10px;" />
							</c:if>
						</div>
						<div class="mb-3">
							<label for="regDate" class="form-label"><span>작성일</span></label>
							<input type="text" class="form-control" id="regDate" name="write_date" value="${notice.write_date}" disabled>
						</div>
						<div class="mb-3">
							<label for="writer" class="form-label"><span>아이디</span></label>
							<input type="text" class="form-control" name="member_id" value="${notice.member_id}" disabled>
						</div>
						<div class="mb-3">
							<label for="writer" class="form-label"><span>조회수</span></label>
							<input type="text" class="form-control" id="cnt" name="cnt" value="${notice.cnt}" disabled>
						</div>
						
						<a href="${contextPath}/admin/notice/listNotice.do">
							<input type="button" value="목록" class="btn btn-secondary btn-sm" />
						</a>
						
						<c:if test="${isLogOn==true and memberInfo.member_id == 'admin'}">
							<input type="button" value="수정" onClick="fn_enable(this.form)" class="btn btn-secondary btn-sm" />
							<input type="button" value="수정반영" onClick="fn_update_notice(formNotice)" class="btn btn-secondary btn-sm" />
							<input type="button" value="취소" onClick="backToList(formNotice)" class="btn btn-secondary btn-sm" />
						</c:if>
						
					</form>
				</div>
			</div>
		</div>
	</main>
	
</body>
</html>