package net.videmantay.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class Auth extends AbstractAppEngineAuthorizationCodeServlet {

	private final Logger log = Logger.getLogger(Auth.class.getName());
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException , ServletException{
		checkCreds(req,res);
		log.info("supposed to checking creds...");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException , ServletException{
		checkCreds(req,res);
		log.info("supposed to checking creds...");
	}
	
	private void checkCreds(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		log.info("Checking creds...");
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		if(user == null){
			res.sendRedirect("/login");
			return;
		}
		Credential cred = GoogleUtils.cred(user.getUserId());
		if(cred == null|| cred.getExpiresInSeconds() < 1800){
			log.info("initializing the auth flow");
			initializeFlow();
		}else{
				res.sendRedirect(req.getRequestURI());
				return;
		}
	}
	
	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
			return GoogleUtils.redirectUri(req);
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		String userId = user.getUserId();
		return GoogleUtils.authFlow(userId);
	}

}
