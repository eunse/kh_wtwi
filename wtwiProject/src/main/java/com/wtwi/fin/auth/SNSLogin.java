package com.wtwi.fin.auth;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpConnection;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import com.wtwi.fin.member.model.vo.Member;

public class SNSLogin {
	private OAuth20Service oauthService;
	private SNSValue sns;

	public SNSLogin() {
		// TODO Auto-generated constructor stub
	}

	public SNSLogin(SNSValue sns) {

			this.oauthService = new ServiceBuilder(sns.getClientId()).apiSecret(sns.getClientSecret())
					.callback(sns.getRedirectUrl()).build(sns.getApi20Instance());
			
			this.sns = sns;
			

	}

	public String getSNSAuthURL() {
		return this.oauthService.getAuthorizationUrl();
	}

	public Member getUserProfile(String code) throws Exception {
	
		OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
		OAuthRequest request = new OAuthRequest(Verb.GET, this.sns.getProfileUrl());
		oauthService.signRequest(accessToken, request);
		Response response = oauthService.execute(request);
		return parseJson(response.getBody(), accessToken);

	}
	public void naverLogout(Member loginMember) throws Exception {
		String logoutUrl ="";

		logoutUrl = this.sns.getLogoutUrl() + "&client_id="+sns.getClientId()+"&client_secret="+sns.getClientSecret()+"&access_token="+loginMember.getAccessToken();

		OAuthRequest request = new OAuthRequest(Verb.GET, logoutUrl);
		Response response = oauthService.execute(request);

		
	}
	
	public void googleLogout(Member loginMember) throws Exception {
		
		 HttpURLConnection connection = null;

	    URL url = new URL("https://accounts.google.com/o/oauth2/revoke?token="+loginMember.getAccessToken());
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("POST"); 
	    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    
	    //Send request
	    //위에서 세팅한 정보값을 바탕으로 요청
	    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());

	    //요청 실행후 dataOutputStream을 close
	    wr.close();

	    if (connection != null) {
	      connection.disconnect();
	    }
	}
		  
		
	
	public void kakaoLogout(Member loginMember) throws Exception {
		
		URL url = new URL("https://kapi.kakao.com/v1/user/unlink");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + loginMember.getAccessToken());
        // ↓↓↓↓↓이 친구 중요!!! 얘 없으면 로그아웃 안됌!!!!!
        conn.getResponseMessage();
        conn.disconnect();
		
	}


	private Member parseJson(String body, OAuth2AccessToken accessToken) throws Exception {
		Member member = new Member();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(body);
		System.out.println(rootNode);
		if (this.sns.isGoogle()) {
			if(rootNode.get("name") != null && rootNode.get("email") != null) {
				member.setMemberNick(rootNode.get("name").asText("여행자"));
				member.setMemberPw("socialLogin");
				member.setMemberEmail(rootNode.get("email").asText());
				member.setMemberGrade("G");				
			}
			
		} else if (this.sns.isNaver()) {
			JsonNode resNode = rootNode.get("response");
			if(resNode.get("nickname") != null && resNode.get("email") != null) {					
				member.setMemberNick(resNode.get("nickname").asText("여행자"));
				member.setMemberPw("socialLogin");
				member.setMemberEmail(resNode.get("email").asText());
				member.setMemberGrade("N");
			}
			
		} else if (this.sns.isFacebook()) {
			member.setMemberNick(rootNode.get("name").asText("여행자"));
			member.setMemberPw("socialLogin");
			String id = rootNode.get("id").asText();
			member.setMemberEmail(id+"@facebook.com");
			member.setMemberGrade("F");
		} 

		member.setAccessToken(accessToken.getAccessToken().toString());
		return member;
	}
	
	
	// 카카오
	// 1) code를 이용해 accessToken GET
	public Member getKakaoProfile(String code) throws Exception {

		String accessToken = "";
		RestTemplate rt = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "b87de33b6c8fe6b2977868b55731dae3");
		params.add("redirect_uri", "http://localhost:8080/wtwi/member/auth/kakao/callback");
		params.add("code", code);
		params.add("client_secret", "496Pgfenz4y5ff9FxZtM7o51KEcl4WF0");

		HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(params, headers);

		ResponseEntity<JSONObject> apiResponse = rt.postForEntity("https://kauth.kakao.com/oauth/token", kakaoRequest,
				JSONObject.class);
		JSONObject responseBody = apiResponse.getBody();

		accessToken = (String) responseBody.get("access_token");
		return getKaKaoMember(accessToken);
	}
	
	// 2) accessToken을 이용해 이용자 정보를 GET
	private Member getKaKaoMember(String accessToken) throws Exception {
		RestTemplate rt = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

		ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				kakaoProfileRequest, String.class);
		return parseKakaoJson(response.getBody(), accessToken);
	}

	// 3) 이용자 정보를 파싱하여 Member 객체 반환
	private Member parseKakaoJson(String body, String accessToken) throws Exception {
		Member member = new Member();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(body);
		JsonNode properties = rootNode.path("properties");
		JsonNode kakaoAccount = rootNode.path("kakao_account");
		System.out.println(rootNode);
		System.out.println(properties.get("nickname"));
		System.out.println(kakaoAccount.get("email"));
		if(properties.get("nickname") != null && kakaoAccount.get("email") != null) {	
			member.setMemberNick(properties.get("nickname").asText("여행자"));
			member.setMemberPw("socialLogin");
			
			member.setMemberEmail(kakaoAccount.get("email").asText());
			member.setMemberGrade("K");
		}
		member.setAccessToken(accessToken);
		System.out.println("카카오 로그인할 때 accessToken : " + accessToken);
		System.out.println("카카오 로그인할 때 member : " + member);
		return member;
	}

}
