package net.videmantay.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import net.videmantay.server.entity.SchoologyInfo;
import net.videmantay.server.schoology.SchoologyAuthorize;
import net.videmantay.server.schoology.SchoologyRequestToken;

import static net.videmantay.server.util.DB.*;

/**
 * Servlet implementation class Schoology
 */

public class Schoology extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	private Logger log = Logger.getAnonymousLogger();
	private OAuth10aService service; 

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
		doGet(request, response);
		schoology(request, response);
	}
	
	private void schoology(HttpServletRequest req, HttpServletResponse res) throws IOException{
		//first things first get SchoologyInfo from db if null or null token do
		/*SchoologyFlow flow = new OAuthSchoologyFlow("lausd", 
				"2c38b6df3cb6d7bea0573a649170b6d005a1c6515", 
				"468fd6a0405c9b7f33593864ea62256f", 
				"https://drsammyleemag.appspot.com");*/
		new SchoologyRequestToken(res);
		

		
	}

}
