<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지 작성</title>

<script type="text/javascript">
  function readURL(input) {
	  if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#preview').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
	  }
  }
 </script>

</head>
<body>
	
	<main class="mt-2 pt-2">
		<div class="container-fluid px-4" style="width:800px">
			<div class="title_underline">
				<h3 class="mt-4">공지사항</h3>
			</div>
			<div class="card mb-4">
				<div class="card-body">
					<form action="${contextPath}/admin/notice/insertNotice.do" method="post" enctype="multipart/form-data">
						<div class="mb-3">
							<label class="form-label">제목</label> 
							<input type="text" class="form-control" name="title" />
						</div>
						<div class="mb-3">
							<label class="form-label">내용</label>
							<textarea class="form-control" name="content"></textarea>
						</div>
						<div class="mb-3 mt-3">
							<label class="form-label">작성자</label> 
							<input type="text" class="form-control" name="member_id" value="${memberInfo.member_id}" readonly/>
						</div>
						<div class="mb-3 mt-3">
							<label class="form-label">이미지파일 첨부</label><br> 
							<input type="file" name="imageFileName" onchange="readURL(this);" /><br>
							<img id="preview" src="#" width=200 height=200 />
						</div>
						
						<input type="submit" value="작성" class="btn btn-secondary btn-sm">
						<a href="${contextPath}/admin/notice/listNotice.do">
							<input type="button" value="취소" class="btn btn-secondary btn-sm" />
						</a>
						
					</form>
				</div>
			</div>
		</div>
	</main>
	
</body>
</html>