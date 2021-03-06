package net.videmantay.server.rest;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static net.videmantay.server.DB.db;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.google.common.collect.ImmutableList;

import net.videmantay.server.GoogleUtils;
import net.videmantay.server.entity.Attendance;
import net.videmantay.server.entity.FullRoutine;
import net.videmantay.server.entity.GradedWork;
import net.videmantay.server.entity.Routine;
import net.videmantay.server.entity.RoutineConfig;
import net.videmantay.server.entity.Incident;
import net.videmantay.server.entity.Roster;
import net.videmantay.server.entity.RosterInfo;
import net.videmantay.server.entity.RosterStudent;
import net.videmantay.server.entity.Schedule;
import net.videmantay.server.entity.ScheduleItem;
import net.videmantay.server.entity.SeatingChart;
import net.videmantay.server.entity.StudentAttendance;
import net.videmantay.server.entity.StudentConfig;
import net.videmantay.server.entity.StudentGoal;
import net.videmantay.server.entity.StudentGroup;
import net.videmantay.server.entity.StudentIncident;
import net.videmantay.server.entity.StudentJob;
import net.videmantay.server.entity.StudentWork;

@Path("/roster")
public class RosterService {

	private final Logger log = Logger.getLogger("Roster Service");


	
/* Here is where we expect to get roster list*/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRosters() throws Exception {
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		
		
		List<RosterInfo> response = db().load().type(RosterInfo.class).filter("ownerId", user.getEmail()).list();
		/*if(response == null || response.size() <= 0){
		
			
				//create a demo if null
				Roster roster = new Roster();
				RosterInfo rosterInfo = new RosterInfo();
				
				rosterInfo.description = "Demo Roster for the 2017 - 2018 school year.";
				rosterInfo.name = "Demo Roster";
				rosterInfo.start="2017-08-14";
				rosterInfo.end = "2018-06-11";
				rosterInfo.roomNum = "230";
				
				
				//response issued its id here /////////
				 rosterInfo.id = db().save().entity(rosterInfo).now().getId();
				 roster.id = rosterInfo.id;
				 log.info("roster id is " + roster.id);
			
				
				//save the roster async
				db().save().entity(roster).now();
				response.add(rosterInfo);
		}*/
			return Response.status(Status.CREATED).entity(response).build();
		
		}
	
	private boolean ownsRoster(Long id, String userId){
		RosterInfo rosInfo = db().load().type(RosterInfo.class).id(id).now();
		return rosInfo.ownerId.equalsIgnoreCase(userId);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response  getRoster(@PathParam("id")Long id) throws IOException{
		
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		
			Roster response = db().load().type(Roster.class).id(id).now();
			if(ownsRoster(id, user.getEmail())){
			response.routines.addAll(db().load().type(Routine.class).filter("rosterId", id).list());
			for(Routine r:response.routines){
				if(r.isDefault()){
					response.defaultRoutine = db().load().type(RoutineConfig.class).id(r.id).now();
					response.defaultRoutine.routine = r;
					break;
				}
			}//end for
			response.incidents.addAll(db().load().type(Incident.class).filter("rosterId", id).list());
			//response.tasks = tasks.tasklists().get(response.taskListId).execute();
			//response.rosterInfo = db().load().type(RosterInfo.class).first().now();
			//load students if there are any
			
		
		
			Attendance attendance = db().load().type(Attendance.class).id(response.id + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).now();
			if(attendance == null){
				attendance = new Attendance(response.id);
				db().save().entity(attendance);
			}
			response.attendance = attendance; 
			
			//load students
			response.students = db().load().type(RosterStudent.class).filter("rosterId", id).list();
			if(response.students != null && response.students.size() > 0){
				for(RosterStudent r:response.students){
					attendance.allStudents.add(r.acct);
				}
			}
			ObjectMapper mapper = new ObjectMapper();
			String repJson = mapper.writeValueAsString(response);
		log.info("response in json is " + repJson );
		return Response.ok().entity(response).build();
			}
			return Response.status(Status.BAD_REQUEST).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRoster(final RosterInfo rosterInfo)throws IOException{
		
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		
		Credential cred = GoogleUtils.cred(user.getUserId());
		Tasks tasks = GoogleUtils.task(cred);
		Drive drive = GoogleUtils.drive(cred);
		PeopleService people = GoogleUtils.people(cred);
		com.google.api.services.calendar.Calendar calendar = GoogleUtils.calendar(cred);
		Person me = people.people().get("people/me").setPersonFields("names,emailAddresses,photos").execute();
		
		rosterInfo.ownerId = user.getEmail();
		rosterInfo.teacherInfo.name =  me.getNames().get(0).getHonorificPrefix()+ " " + me.getNames().get(0).getFamilyName();
		//rosterInfo.teacherInfo.grade = "5";
		rosterInfo.teacherInfo.picUrl = me.getPhotos().get(0).getUrl();
		rosterInfo.id = db().save().entity(rosterInfo).now().getId();
		Roster roster = new Roster();
		roster.id = rosterInfo.id;
		
		if(roster.calendarId == null || roster.calendarId.isEmpty()){
			Calendar cal = new Calendar();
			cal.setSummary(rosterInfo.name);
			cal.setDescription(rosterInfo.description);
			cal = calendar.calendars().insert(cal).execute();
			roster.calendarId = cal.getId();
		}
		TaskList taskList = new TaskList();
		taskList.setTitle(rosterInfo.name);
		taskList = tasks.tasklists().insert(taskList).execute();
		Task task = new Task();
		task.setTitle("Add Students");
		task.setNotes("add students to roster");
		tasks.tasks().insert(taskList.getId(), task).execute();
		roster.taskListId  = taskList.getId();
		
		if(roster.teacherFolderId == null||roster.teacherFolderId.isEmpty()){
			File teacherFolder = GoogleUtils.folder("Routinely");
			teacherFolder = drive.files().create(teacherFolder).execute();
			roster.teacherFolderId = teacherFolder.getId();
		}
		File studentFolder = GoogleUtils.folder("Students");
		studentFolder.setParents(ImmutableList.of(roster.teacherFolderId));
		studentFolder = drive.files().create(studentFolder).execute();
		
		File sharedFolder = GoogleUtils.folder("Public");
		sharedFolder.setParents(ImmutableList.of(roster.teacherFolderId));
		sharedFolder = drive.files().create(sharedFolder).execute();
		Permission publicPerms =new Permission();
		publicPerms.setType("anyone");
		publicPerms.setRole("reader");
		drive.permissions().create(sharedFolder.getId(), publicPerms).execute();
		
		
		
		roster.folders.put("public",sharedFolder.getId());
		roster.folders.put("students" ,studentFolder.getId());
		Schedule schedule = new Schedule();
		ScheduleItem recess = new ScheduleItem();
		recess.title = "Recess";
		recess.backgroundColor = "Gray";
		recess.dow.addAll(ImmutableList.of(1, 2, 3, 4, 5));
		switch(rosterInfo.teacherInfo.grade){
		case "K": 
		case "1":
		case "2": recess.start = "9:50am"; recess.end = "10:10am";break;
		default: recess.start = "10:15am"; recess.end = "10:35am";
		}
		schedule.items.add(recess);
		
		ScheduleItem lunch = new ScheduleItem();
		lunch.title = "Lunch";
		lunch.backgroundColor = "Gray";
		lunch.dow.addAll(ImmutableList.of(1, 2, 3, 4, 5));
		switch(rosterInfo.teacherInfo.grade){
		case "K": 
		case "1":lunch.start = ""; lunch.end = "";break;
		case "3":
		case "2": lunch.start = ""; lunch.end = "";break;
		default: lunch.start = ""; lunch.end = "";
		}
		schedule.items.add(lunch);
		
		
		ArrayList<Incident> incidents = new ArrayList<>();
		Incident incident;
		// we also need to create default incidents here too
		// positive list////////
		// 1. turned in hw - 1px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Turned in HW");
		incident.setPoints(1);
		incident.setImageUrl("yellowBeaker");
		incidents.add(incident);
		// 2. paricipatation -1px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Participation");
		incident.setPoints(1);
		incident.setImageUrl("redBeaker");
		incidents.add(incident);
		// 3. help others -3px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Helping others");
		incident.setPoints(3);
		incident.setImageUrl("scienceBoy");
		incidents.add(incident);
		// 4. took responsibility -2px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Taking responsibility");
		incident.setPoints(2);
		incident.setImageUrl("rocket");
		incidents.add(incident);
		// 5. shared ideas -1px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Shared idea");
		incident.setPoints(1);
		incident.setImageUrl("doctor");
		incidents.add(incident);
		// 6. listened attentively -5px;
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Listened attentively");
		incident.setPoints(1);
		incident.setImageUrl("scientist");
		incidents.add(incident);
		// negative list///////////
		// 1. no hw -1
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("No HW");
		incident.setPoints(-1);
		incident.setImageUrl("noHW");
		incidents.add(incident);
		// 2. interrupted -1
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Interrupted");
		incident.setPoints(-1);
		incident.setImageUrl("thermometerPlain");
		incidents.add(incident);
		// 3. shouted out -3px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Shouting out");
		incident.setPoints(-3);
		incident.setImageUrl("yellowBeaker");
		incidents.add(incident);
		// 4. distracted -2px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("New Incident");
		incident.setPoints(-2);
		incident.setImageUrl("yellowRadioactive");
		incidents.add(incident);
		// 5. bathroom - 1px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Bathroom break");
		incident.setPoints(-1);
		incident.setImageUrl("clipboard");
		incidents.add(incident);
		// 6. fighting -5px
		incident = new Incident();
		incident.rosterId = roster.id;
		incident.setName("Fighting");
		incident.setPoints(-5);
		incident.setImageUrl("brokenglassWarning");
		incidents.add(incident);
		
		roster.incidents.addAll(db().save().entities(incidents).now().values());
		
		Routine defaultTime = new Routine();
		defaultTime.rosterId = roster.id;
		defaultTime.setDefault(true);
		defaultTime.setDescript("Routines refer to a set of procedures, groups, and stations that help students transition form one task to the next." +
					" \" Carpet Time\" , \"Author's Chair\", \"Gallery Walks\" are all example of routines.");
		defaultTime.title = "Class Routine";
		defaultTime.setDefault(true);
		defaultTime.id = db().save().entity(defaultTime).now().getId();
		RoutineConfig ctConfig = new RoutineConfig();
		ctConfig.id = defaultTime.id;
		SeatingChart seatingChart = new SeatingChart();
		seatingChart.id = defaultTime.id;
		db().save().entities(seatingChart, ctConfig);
		roster.defaultRoutine = ctConfig;
		
		db().save().entity(roster);
		//return rosterInfo  and populate client grid
		return Response.ok().entity(rosterInfo).build();
		
	}
	
	@POST
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRoster(final RosterInfo roster) throws IOException{
			//TODO: Validate the RosterInfo before persisting.
			roster.ownerId = UserServiceFactory.getUserService().getCurrentUser().getEmail();
			db().save().entity(roster).now().getId();
			return Response.ok().entity(roster).build();
	}
	
	
	@GET
	@Path("/{id}/student/{acct}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRosterStudent(@PathParam("id")Long id, @PathParam("acct") String acct) throws IOException {
		//here we need everything that the student page needs to get started
		StudentConfig config = new StudentConfig();
		String studentId = id + acct;
		config.assignments = db().load().type(StudentWork.class).filter("studentId", studentId).limit(20).list();
		config.attendance = db().load().type(StudentAttendance.class).filter("studentId", studentId)
							.order("-date").limit(20).list();
		config.goals = db().load().type(StudentGoal.class).filter("studentId", studentId)
					.order("-dueDate").limit(20).list();
		config.incidents=db().load().type(StudentIncident.class).filter("studentId", studentId).limit(20).list();
		
		return Response.ok().entity(config).build();
		
	}

	@POST
	@Path("{id}/student/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudent(@PathParam("id")Long id, RosterStudent student) throws IOException {
		String stuId = id + student.acct;
		//first things first
		RosterStudent rosStudent = db().load().type(RosterStudent.class).id(stuId).now();
		
			if(stuId== rosStudent.id){
				student.id = stuId;
				db().save().entity(student);
				return Response.ok().entity(student).build();
			}
		
		
		
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		Credential cred = GoogleUtils.cred(user.getUserId());
		Drive drive = GoogleUtils.drive(cred);
		
		Roster roster = db().load().type(Roster.class).first().now();
	
		//register students
			File studentFolder = GoogleUtils.folder(student.acct);
			studentFolder.setParents(ImmutableList.of(roster.folders.get("students")));
			Permission perm = new Permission();
			perm.setRole("reader");
			perm.setEmailAddress(student.acct);
			perm.setType("user");
			studentFolder = drive.files().create(studentFolder).execute();
			student.driveFolder = studentFolder.getId();
			drive.permissions().create(studentFolder.getId(), perm).execute();
			
		//create the drive folders
		student.id = id + student.acct;
		db().save().entity(student);

		return Response.ok().entity(student).build();
	}

@GET
@Path("{id}/student")
@Produces(MediaType.APPLICATION_JSON)
public Response listStudents(@PathParam("id")Long id) throws IOException{
		List<RosterStudent> rosStudents = db().load().type(RosterStudent.class).filter("rosterId", id).list();
		return Response.ok().entity(rosStudents).build();

}
	
	

	@POST
	@Path("{id}/student/{acct}/incident")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createIncidentForStudent(@PathParam("id")Long id,
			@PathParam("acct") String studentAcct, StudentIncident stuIncident) {

		Roster result = ofy().load().type(Roster.class).id(id).now();

		if (result != null) {
			String stuId = id + studentAcct;
			RosterStudent student = ofy().load().key(Key.create(RosterStudent.class, stuId)).now();

			if (student != null) {

				if (stuIncident.points < 0) {
					student.negPoints += stuIncident.points;
				} else {
					student.posPoints += stuIncident.points;
				}

				// update points aggregate
				db().save().entity(student);
				stuIncident.id = db().save().entity(stuIncident).now().getId();

				return Response.ok().entity(stuIncident).build();
			}
		}

		return Response.status(Status.NOT_FOUND).build();
	}


	
	@GET
	@Path("/{id}/student/{acct}/incident")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentIncidents(@PathParam("id")Long id, @PathParam("acct") String studentAcct) {
			String stuId = id + studentAcct;
				List<StudentIncident> studentIncidents = db().load().type(StudentIncident.class)
						.filter("studentId", stuId).list();

				return Response.ok().entity(studentIncidents).build();

	}

	@GET
	@Path("{id}/routine")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClasstimeList(@PathParam("id")Long id) {

		Roster result = ofy().load().type(Roster.class).id(id).now();

		if (result != null) {

			List<Routine> routineList = ofy().load().type(Routine.class).filter("rosterId", id).list();
			
			
			return Response.ok().entity(routineList).build();
		}

		return Response.status(Status.NOT_FOUND).build();

	}
	
	
	@GET
	@Path("{id}/routine/{subId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClassTime(@PathParam("id")Long id, Routine routine) {

		Roster result = ofy().load().type(Roster.class).id(id).now();

		if (result != null) {
				RoutineConfig config = ofy().load().key(Key.create(RoutineConfig.class,routine.id)).now();
			
				return Response.ok().entity(new FullRoutine(routine,config)).build();
		}else{

		return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST
	@Path("{id}/routine/{subId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createClassTime(@PathParam("id")Long id,FullRoutine fullRoutine) {

		Roster result = ofy().load().type(Roster.class).id(id).now();

		if (result != null) {

			Long newId = db().save().entity(fullRoutine.routine).now().getId();
			fullRoutine.routineConfig.id = newId;
			db().save().entity(fullRoutine.routineConfig);
			return Response.ok().entity(newId).build();
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	@GET
	@Path("{id}/routine/{routineId}/seatingchart")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeatingChart(@PathParam("routineId") Long routineId) {
		// TODO:check for roster blah blah
		SeatingChart seatingChart = db().load().key(Key.create(SeatingChart.class, routineId)).now();
		if (seatingChart == null) {
			seatingChart = new SeatingChart();
			seatingChart.id = routineId;
			db().save().entity(seatingChart);
		}
		return Response.ok().entity(seatingChart).build();
	}

	@POST
	@Path("{id}/routine/{routineId}/seatingchart")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSeatingChart(RoutineConfig config,
			SeatingChart seatingChart) {
		// TODO: set up checks
		db().save().entity(seatingChart);
		return Response.ok().build();
	}

	
	
	@GET
	@Path("/{id}/incident")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listIncidents(@PathParam("id") Long id){
		List<Incident> incidents = db().load().type(Incident.class).filter("rosterId",id).list();
		return Response.ok().entity(incidents).build();
	}
	
	@POST
	@Path("/{id}/incident/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createIncident(Incident incident){
		System.out.print(incident.getId() == null);
		if(incident.getRosterId() == null){
		}
		incident.setId(db().save().entity(incident).now().getId());
		return Response.ok().entity(incident).build();
	}
	
	@POST
	@Path("/{id}/incident/{incidentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateIncident(Incident incident){
		db().save().entity(incident);
		return Response.ok().entity(incident).build();
	}
	
	@DELETE
	@Path("/{id}/incident/{incidentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response deleteIncident(@PathParam("incidentId") Long incidentId){
		log.log(Level.INFO, "Delete Incident called");
		Key<Incident> incidentKey = Key.create(Incident.class, incidentId);
		Incident incident = db().load().key(incidentKey).now();
		log.log(Level.INFO, "Incident id from entity is " + incident.getId() + " and roster id is " + incident.getRosterId());
		
		db().delete().entity(incident);
		return Response.ok().entity(incident).build();
		
	}
	
	@POST
	@Path("/incident/reset")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetIncidents(){
		//set a reset date for the roster
		Roster roster = db().load().type(Roster.class).first().now();
		//roster should have a resetdate to refer to
		//TODO: change the reset date for today
		
		//get the students and send points back to zero
		List<RosterStudent> students = db().load().type(RosterStudent.class).ancestor(roster).list();
		for(RosterStudent rs : students){
			//set pos and neg points to zero
			rs.posPoints = 0;
			rs.negPoints = 0;
		}
		//what to send here? the list of students?
		db().save().entities(students);
		db().save().entity(roster);
		
		return Response.ok().entity("Student points were reset").build();
	}
	
	@GET
	@Path("{id}/attendance/{attendanceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response takeAttendance(@PathParam("id")Long id, @PathParam("attendanceId") String attendanceId){
		Attendance attendance = db().load().type(Attendance.class).id(attendanceId).now();
		
		
		if(attendance == null){
			attendance = new Attendance(id);
			 
		}
		
		System.out.println("Attendance: id=" + attendance.id );
		if(attendance.completed){
			List<StudentAttendance> dbStuAtt = db().load().type(StudentAttendance.class).ancestor(attendance).list();
			attendance.studentAttendance.addAll(dbStuAtt);
		}
		System.out.println("send entity" + attendance);
		return Response.ok().entity(attendance).build();
	}
	
	@POST
	@Path("/{id}/attendance/{attendanceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response submitAttendance(Attendance attendance){
		ObjectMapper mapper = new ObjectMapper();
		try {
			String attStr = mapper.writeValueAsString(attendance);
			log.log(Level.INFO, attStr);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		attendance.completed = true;
		db().save().entity(attendance);
		db().save().entities(attendance.studentAttendance);
		return Response.ok().entity(attendance).build();
	}
	
	@DELETE
	@Path("/{id}/attendance/{attendanceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	public Response deleteAttendance(Attendance attendance){
		
		db().delete().entities(db().load().ancestor(attendance).keys());
		return Response.ok().entity("Attendance Deleted Successfully").build();
	}
	
	@GET
	@Path("/work/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listGradedWork() throws IOException{
		List<GradedWork> gradedWorks = db().load().type(GradedWork.class).list();
		return  Response.ok().entity(gradedWorks).build();	
	}
	
	@GET
	@Path("/work/{workId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGradedWork(@PathParam("workId") Long workId) throws IOException{
		List<StudentWork> studentWork = db().load().type(StudentWork.class).filter("gradedWorkId",""+workId).list();
		return Response.ok().entity(studentWork).build();
	}
	@POST
	@Path("/work/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createGradedWork(GradedWork work) throws IOException, ParseException{
		
		db().save().entity(work);
		return Response.ok().entity(work).build();
		
	}
	
	@POST
	@Path("/work/{workId}/studentwork")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response gradeStudentWork(@PathParam("workId") Long workId,
			List<StudentWork> studentWork) throws IOException{
		db().save().entity(studentWork);
		return Response.ok().entity(studentWork).build();
	}//end method
	
	@DELETE
	@Path("/work/{workId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteGradedWork(String courseCode,GradedWork work) throws IOException{
		db().delete().keys(db().load().type(StudentWork.class).filter("gradedWorkId", work.id).keys());
		return Response.ok().entity("Graded work successfully deleted").build();
	}
		
}
