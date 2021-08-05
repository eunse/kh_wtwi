<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>
/* 후기작성 폼 시작 */
#write-review-wrapper {
	right: 290px;
	position: absolute;
	z-index: 2;
	margin: auto;
	border-radius: 5px;
	opacity: 90%;
}

/* 별점 영역 시작 */
#star {
	margin-left: 25px;
}

.star-rating {
	display: flex;
	flex-direction: row-reverse;
	font-size: 2.25rem;
	line-height: 2.5rem;
	justify-content: space-around;
	padding: 0 0.2em;
	text-align: center;
	width: 5em;
}

.star-rating input {
	display: none;
}

.star-rating label {
	-webkit-text-fill-color: transparent;
	/* Will override color (regardless of order) */
	-webkit-text-stroke-width: 2.3px;
	-webkit-text-stroke-color: darkg;
	cursor: pointer;
}

.star-rating :checked ~label {
	-webkit-text-fill-color: gold;
}

.star-rating label:hover, .star-rating label:hover ~label {
	-webkit-text-fill-color: #fff58c;
}

/* 별점 영역 끝 */
#image-wrapper {
	margin: auto;
	width: 380px;
	height: 150px;
}

.image {
	width: 100px;
	height: 100px;
	float: left;
	margin-left: 12px;
	margin-right: 12px;
	margin-top: 10px;
	margin-bottom: 10px;
	background-color: lightgray;
}

#upload-image-btn {
	float: right;
	margin-top: 10px;
	margin-right: 20px;
}

#text-area {
	resize: none;
}

#text-area-wrapper {
	margin: auto;
	width: 370px;
}

#wirte-btn {
	width: 100px;
	float: left;
	margin-bottom: 10px;
}

#cancel-btn {
	width: 100px;
	float: right;
	margin-bottom: 10px;
}

/* 후기작성 폼 끝 */
</style>
</head>
<body>
	<div id="write-review-wrapper">
		<div class="card" id="write-review">
			<span style="line-height: 50px; font-size: 20px;">후기 작성</span>
			<hr>
			<!-- 별점 영역 시작 -->
			<div class="star-rating space-x-4 mx-auto">
				<input type="radio" id="5-stars" name="rating" value="5"
					v-model="ratings" /> <label for="5-stars" class="star pr-4">★</label>
				<input type="radio" id="4-stars" name="rating" value="4"
					v-model="ratings" /> <label for="4-stars" class="star">★</label> <input
					type="radio" id="3-stars" name="rating" value="3" v-model="ratings" />
				<label for="3-stars" class="star">★</label> <input type="radio"
					id="2-stars" name="rating" value="2" v-model="ratings" /> <label
					for="2-stars" class="star">★</label> <input type="radio"
					id="1-star" name="rating" value="1" v-model="ratings" /> <label
					for="1-star" class="star" id="star">★</label>
			</div>
			<!-- 별점 영역 끝 -->
			<hr>
			<div id="image-wrapper">
				<div class="image"></div>
				<div class="image"></div>
				<div class="image"></div>
				<a href="#" class="badge badge-pill badge-primary"
					id="upload-image-btn">사진첨부</a>
			</div>
			<hr>
			<div id="text-area-wrapper">
				<textarea class="form-control" aria-label="With textarea"
					id="text-area" rows="15"></textarea>
			</div>
			<hr>
			<button type="button" class="btn btn-primary" style="float: right;">작성</button>
			<button type="button" class="btn btn-secondary" id="cancel-btn"
				style="float: right;">취소</button>
		</div>
	</div>
	
</body>
</html>