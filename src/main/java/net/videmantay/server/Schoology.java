package net.videmantay.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import net.videmantay.server.entity.SchoologyInfo;
import net.videmantay.server.schoology.SchoologyApi;
import net.videmantay.server.schoology.SchoologyUser;

import static net.videmantay.server.DB.*;

/**
 * Servlet implementation class Schoology
 */

public class Schoology extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	private Logger log = Logger.getAnonymousLogger();
	private UserService us = UserServiceFactory.getUserService();

    public Schoology() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		schoology(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		schoology(request, response);
	}
	
	private void schoology(HttpServletRequest req, HttpServletResponse res) throws IOException{
		//first things first get SchoologyInfo from db if null or null token do
	//Schoology is based on user id
		String schoologyUserId ="";
		
		log.info("Schoology initiated");
		User user = us.getCurrentUser();
		if(user == null){
			log.info("Google User null send to login and try again");
			res.sendRedirect(us.createLoginURL("/schoology"));
		}
		SchoologyInfo info = db().load().type(SchoologyInfo.class).id(user.getUserId()).now();
		if(info == null){
			log.info("No schoology info in data base ask for schoology request token");
		
			if(req.getParameter("realm") != null && req.getParameter("realm").equals("user")){
				schoologyUserId = req.getParameter("realm_id");
				info = new SchoologyInfo();
				info.acctId = user.getUserId();
				info.uId = schoologyUserId;
				
				
				this.getSchoologyToken(res, info);
			}else{
				//TODO: tell them to login from schoology home page
			}
			
		}
		

		
		SchoologyUser sUser = SchoologyApi.user(info);
		if(sUser == null){
			if(req.getSession().getAttribute("schoologyCall") == null){
				//make call to schoology
				req.getSession().setAttribute("schoologyCall", 1);
			}else{
				//send to error so we dont get endless loop.
				if((Integer)req.getSession().getAttribute("schoologyCall") >= 1){
					//goto schoology login prompt
				}
			}
		}
		log.info("schoology user email " + sUser.primary_email+ " & google user email " + user.getEmail());
		
		//get user info and compare email
		//at this point we have schoology cred
				if(sUser.primary_email.equals(user.getEmail())){
			    	 res.sendRedirect("/teacher");
			    	 res.flushBuffer();}
				else{
					//TODO: send to error page
				}
			   
	
	}
	
	private void getSchoologyToken(HttpServletResponse res, SchoologyInfo info) throws IOException{
		
		OAuthGetTemporaryToken temp = new OAuthGetTemporaryToken("https://api.schoologytest.com/v1/oauth/request_token");
		temp.consumerKey = "2c38b6df3cb6d7bea0573a649170b6d005a1c6515";
		temp.callback = "https://drsammyleemag.appspot.com/schoologycallback";
		OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = "468fd6a0405c9b7f33593864ea62256f";
		signer.tokenSharedSecret = "";
		temp.signer = signer;
		temp.transport = new NetHttpTransport();
		OAuthCredentialsResponse rToken = temp.execute();
		log.info("roken is  null? " + new Boolean(rToken == null));
		log.info("roken is  token is null? " + new Boolean(rToken.token == null));
		log.info("rToken token value is " + rToken.token);
		info.requestToken.token = rToken.token.toString();
		info.requestToken.tokenSecret =rToken.tokenSecret.toString();
		db().save().entity(info).now();
		
		String redirect = "https://lausd.schoologytest.com/oauth/authorize?oauth_token="+
					rToken.token 
				+ "&oauth_callback=https://drsammyleemag.appspot.com/schoologycallback";
		res.sendRedirect(redirect);
		res.flushBuffer();
		    return;
	}

}
