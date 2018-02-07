package net.videmantay.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.appengine.api.users.*;

import net.videmantay.server.entity.SchoologyInfo;
import net.videmantay.server.schoology.SchoologyApi;
import net.videmantay.server.schoology.SchoologyHeader;
import net.videmantay.server.schoology.SchoologyUser;

import static net.videmantay.server.util.DB.db;

/**
 * Servlet implementation class SchoologyCallback
 */
@WebServlet("/SchoologyCallback")
public class SchoologyCallback extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private Logger log = Logger.getAnonymousLogger();
    public SchoologyCallback() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("schoology call  back init");
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
	    SchoologyInfo info = db().load().type(SchoologyInfo.class).id(user.getUserId()).now();
	    //do check next time
		
		OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = "468fd6a0405c9b7f33593864ea62256f";
		signer.tokenSharedSecret = info.requestToken.tokenSecret;
		 OAuthGetAccessToken getAccessToken = new OAuthGetAccessToken("https://api.schoologytest.com/v1/oauth/access_token");
		    getAccessToken.signer = signer;
		    getAccessToken.temporaryToken= info.requestToken.token;
		    getAccessToken.transport = new NetHttpTransport();
		    getAccessToken.verifier= "";
		    getAccessToken.consumerKey = "2c38b6df3cb6d7bea0573a649170b6d005a1c6515";
		    OAuthCredentialsResponse accessTokenResponse = getAccessToken.execute();
		    info.accessToken.token = accessTokenResponse.token;
		    info.accessToken.tokenSecret = accessTokenResponse.tokenSecret;
		    db().save().entity(info).now();
		    log.info("seems like it work");
		    log.info("token is " + accessTokenResponse.token);
		    //check call to schoology here
		    SchoologyUser sUser = SchoologyApi.user(info);
		    if(info.uId == null){
		    	info.uId = sUser.uid;
			    info.schoolId = sUser.school_id;
			    info.buildingId = sUser.building_id;
			    
		    }
		    else{
		    	if(!info.uId.equals(sUser.uid)){
		    		//this is an error send to error page
		    	}
		    }
		    //either way save info
		    db().save().entity(info).now();
		    
		    response.sendRedirect("/teacher");
		
		//compare access token
		
		//send to schoology and trade for permanent
		
		//send back to main page
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
