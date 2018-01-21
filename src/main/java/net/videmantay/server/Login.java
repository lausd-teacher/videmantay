package net.videmantay.server;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import static net.videmantay.server.DB.db;

import com.google.api.client.auth.oauth2.Credential;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import net.videmantay.server.entity.RosterStudent;
import net.videmantay.server.entity.SchoologyInfo;


public class Login extends HttpServlet{
	private final Logger log = Logger.getLogger(Login.class);
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException, ServletException{
		
		log.info("Login servlet called");
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		if(user == null){
			String url = us.createLoginURL("/login");
			res.sendRedirect(url);
			return;
		}
		Credential cred = GoogleUtils.cred(user.getUserId());
		
    	//possible null for cred if this is the case have user auth
    	//then send back here
    	if(cred == null || cred.getAccessToken() == null){
    		log.info("cred is null in first log in should redirect");
    		res.sendRedirect("/auth");
    		return;
    	}
    	
    	if(cred.getExpiresInSeconds()< 600){
			cred.refreshToken();
		}
    	
    	//TODO:consider changing to check user role
		if(user.getEmail().equals("tdd6623@lausd.net") || 
				user.getEmail().equals("youssef.elias@lausd.net") ||
				user.getEmail().equals("robert.rodriguez@lausd.net")||
				user.getEmail().equals("videmantay@gmail.com") ||
				user.getEmail().equals("tanilo@videmantay.net")||
				user.getEmail().equals("dedguy21@gmail.com")){
			res.sendRedirect("/teacher");
			return;
		}else{
		//see is he has an account
		RosterStudent student = db().load().type(RosterStudent.class).id(user.getEmail()).now();
		//check for credentials
		if(student != null){
    	res.sendRedirect("/student/");
    	return;
		}else{
			res.sendRedirect("/error.html");
			return;
		}
		
		}//end else
		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException, ServletException{
				doGet(req, res);
	}
	
}
