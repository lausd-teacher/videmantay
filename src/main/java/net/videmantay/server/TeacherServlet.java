package net.videmantay.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.videmantay.server.entity.Schedule;

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
		PeopleService people = GoogleUtils.people(cred);
		Person person = people.people().get("people/me").setPersonFields("names,emailAddresses,photos").execute();
		info.email = person.getEmailAddresses().get(0).getValue();
		info.firstName = person.getNames().get(0).getGivenName();
		info.lastName = person.getNames().get(0).getFamilyName();
		info.img = person.getPhotos().get(0).getUrl();
		info.token = cred.getAccessToken();
		info.logout = UserServiceFactory.getUserService().createLogoutURL("/");
		
		//make info a json sting
		ObjectMapper mapper = new ObjectMapper();
		String infoJson = mapper.writeValueAsString(info);
		log.info("loginfo is " + infoJson);
		
		//teacher need access to default events lets push it on them
		Schedule schoolEventsDB = db().load().type(Schedule.class).filter("name","schoolevents").first().now();
		String schoolEvents = mapper.writeValueAsString(schoolEventsDB);
		log.info("school events are: " + schoolEvents);
		
		TemplateGen template = (TemplateGen) this.getServletContext().getAttribute("template");
		//Could use freemarker as well, but it's not needed for now
		//Not protected yet
		res.setContentType("text/html");
		res.setStatus(HttpServletResponse.SC_OK);
		log.log(Level.INFO, "redenring teacher");
		//populate the root with user profile
		Map<String, Object> root = new HashMap<>();
		root.put("info", infoJson);
		root.put("schoolEvents", schoolEvents);
		Template teacherPage = template.getTeacherPage();
		
			try {
				teacherPage.process(root, res.getWriter());
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
