package net.videmantay.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.videmantay.server.entity.RosterInfo;
import net.videmantay.server.entity.SchoologyInfo;
import net.videmantay.server.schoology.*;

import static net.videmantay.server.DB.db;

public class TeacherServlet extends HttpServlet { 
	private final Logger log = Logger.getLogger("logger");
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException{
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if(user == null){
			res.sendRedirect("/login");
			return;
		}
		LoginInfo info = new LoginInfo();
		Credential cred = GoogleUtils.cred(user.getUserId());
		if(cred == null || cred.getAccessToken() == null){
			log.warning("cred or access token is null");
			res.sendRedirect("/auth");
			return;
		}
		SchoologyInfo sInfo = db().load().type(SchoologyInfo.class).id(user.getUserId()).now();
		if(sInfo != null){
			SchoologyUser sUser = SchoologyApi.user(sInfo);	
			info.firstName = sUser.name_first;
			info.lastName = sUser.name_last;
			info.img = sUser.picture_url;
		}
		
		info.email = user.getEmail();
		info.token = cred.getAccessToken();
		info.logout = UserServiceFactory.getUserService().createLogoutURL("/");
	
		//make info a json sting
		ObjectMapper mapper = new ObjectMapper();
		String infoJson = mapper.writeValueAsString(info);
		log.info("loginfo is " + infoJson);
		
		List<RosterInfo> rosList = RosterApi.rosterList(user, sInfo);
	
		String rostList = mapper.writeValueAsString(rosList);
		
		TemplateGen template = (TemplateGen) this.getServletContext().getAttribute("template");
		res.setContentType("text/html");
		res.setStatus(HttpServletResponse.SC_OK);
		log.log(Level.INFO, "redenring teacher");
		//populate the root with user profile
		Map<String, Object> root = new HashMap<>();
		root.put("info", infoJson);
		root.put("rosterList", rostList);
		Template teacherPage = template.getTeacherPage();
		
		
			try {
				teacherPage.process(root, res.getWriter());
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}