package net.videmantay.server.rest;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static net.videmantay.server.DB.db;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import net.videmantay.server.entity.StudentIncident;
import net.videmantay.server.entity.StudentWork;

@Path("/roster")
public class RosterService {

	private final Logger log = Logger.getLogger("Roster Service");


	

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoster(Roster roster) throws Exception {
		
		Roster response = db().load().type(Roster.class).first().now();
		if(response == null){
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
			Credential cred = GoogleUtils.cred(user.getUserId());
			Tasks tasks = GoogleUtils.task(cred);
			Drive drive = GoogleUtils.drive(cred);
			PeopleService people = GoogleUtils.people(cred);
			com.google.api.services.calendar.Calendar calendar = GoogleUtils.calendar(cred);
				
				roster.ownerId = user.getEmail();
				RosterInfo rosterInfo = new RosterInfo();
				Person me = people.people().get("people/me").execute();
				roster.id = db().save().entity(rosterInfo).now().getId();
				rosterInfo.teacherInfo.name = "Mr. ViDemantay";
				rosterInfo.teacherInfo.grade = "5";
				rosterInfo.teacherInfo.picUrl = me.getPhotos().get(0).getUrl();
				
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
				roster.taskListId  = taskList.getId();
				
				
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
				//save roster at this point
				db().save().entity(roster);
				
				ArrayList<Incident> incidents = new ArrayList<>();
				Incident incident;
				// we also need to create default incidents here too
				// positive list////////
				// 1. turned in hw - 1px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Turned in HW");
				incident.setPoints(1);
				incident.setImageUrl("/img/allicons.svg#yellowBeaker");
				incidents.add(incident);
				// 2. paricipatation -1px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Participation");
				incident.setPoints(1);
				incident.setImageUrl("/img/allicons.svg#redBeaker");
				incidents.add(incident);
				// 3. help others -3px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Helping others");
				incident.setPoints(3);
				incident.setImageUrl("/img/allicons.svg#scienceBoy");
				incidents.add(incident);
				// 4. took responsibility -2px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Taking responsibility");
				incident.setPoints(2);
				incident.setImageUrl("/img/allicons.svg#rocket");
				incidents.add(incident);
				// 5. shared ideas -1px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Shared idea");
				incident.setPoints(1);
				incident.setImageUrl("/img/allicons.svg#doctor");
				incidents.add(incident);
				// 6. listened attentively -5px;
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Listened attentively");
				incident.setPoints(1);
				incident.setImageUrl("/img/allicons.svg#scientist");
				incidents.add(incident);
				// negative list///////////
				// 1. no hw -1
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("No HW");
				incident.setPoints(-1);
				incident.setImageUrl("/img/allicons.svg#noHW");
				incidents.add(incident);
				// 2. interrupted -1
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Interrupted");
				incident.setPoints(-1);
				incident.setImageUrl("/img/allicons.svg#thermometerPlain");
				incidents.add(incident);
				// 3. shouted out -3px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Shouting out");
				incident.setPoints(-3);
				incident.setImageUrl("/img/allicons.svg#yellowBeaker");
				incidents.add(incident);
				// 4. distracted -2px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("New Incident");
				incident.setPoints(-2);
				incident.setImageUrl("/img/allicons.svg#yellowRadioactive");
				incidents.add(incident);
				// 5. bathroom - 1px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Bathroom break");
				incident.setPoints(-1);
				incident.setImageUrl("/img/allicons.svg#clipboard");
				incidents.add(incident);
				// 6. fighting -5px
				incident = new Incident();
				incident.setRosterId(roster.id);
				incident.setName("Fighting");
				incident.setPoints(-5);
				incident.setImageUrl("/img/allicons.svg#brokenglassWarning");
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
				response = roster;
		}
			return Response.status(Status.CREATED).entity(response).build();
		
		}
	
	@POST
	public Response updateRoster(final Roster roster) throws IOException{
			RosterInfo rosInfo = roster.rosterInfo;
			db().save().entities(roster, rosInfo);
			return Response.ok().entity(roster).build();
	}
	
	
	@GET
	@Path("/student/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRosterStudent(@PathParam("studentId") Long studentId, String courseId) throws IOException {
		RosterStudent rosterStudent = ofy().load().key(Key.create(RosterStudent.class, studentId)).now();
		if(rosterStudent == null){
		return Response.status(Status.NOT_FOUND).build();
		}else{
			return Response.ok().entity(rosterStudent).build();
		}
	}

	@POST
	@Path("/student")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createStudents(RosterStudent student) throws IOException {
		
		//first things first
		String errorMsg = "";
		List<RosterStudent>rosStudents = db().load().type(RosterStudent.class).list();
		for(RosterStudent rs2:rosStudents){
			if(student.acct == rs2.acct){
				errorMsg += student.acct+ " is already registered\n";
				return Response.status(Status.BAD_REQUEST).entity(errorMsg).build();
			}
		}//inner for
		
		
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
			perm.setType("user");
			perm.setEmailAddress(student.acct);
			studentFolder = drive.files().create(studentFolder).execute();
			student.driveFolder = studentFolder.getId();
			drive.permissions().create(studentFolder.getId(), perm).execute();
			
		//create the drive folders
			
		db().save().entity(student);

		return Response.ok().entity(errorMsg).build();
	}

@GET
@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
public Response listStudents() throws IOException{
		List<RosterStudent> rosStudents = db().load().type(RosterStudent.class).list();
		return Response.ok().entity(rosStudents).build();

}
	
	

	@POST
	@Path("/student/{studentId}/incident")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createIncidentForStudent(StudentIncident stuIncident,
			@PathParam("studentId") String studentId) {

		Roster result = ofy().load().type(Roster.class).first().now();

		if (result != null) {
			stuIncident.parent = Key.create(RosterStudent.class, studentId);

			RosterStudent student = ofy().load().key(Key.create(RosterStudent.class, studentId)).now();

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

	@POST
	@Path("/batch/student/incident")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response batchCreateStudentIncident(StudentIncident[] stuIncidents) {
		
		ArrayList<Key<RosterStudent>> keys = new ArrayList<Key<RosterStudent>>();

		// save for batch save
		ArrayList<RosterStudent> students = new ArrayList<RosterStudent>();
		// obviously we need to serious validation
		for (StudentIncident si : stuIncidents) {
			keys.add(Key.create(RosterStudent.class, si.studentId));
		}
		students.addAll(db().load().keys(keys).values());
		// complicated iteration ///
		for (RosterStudent rs : students) {
			// cycle through incidents and match
			for (StudentIncident si : stuIncidents) {
				if (rs.acct.equals(si.studentId)) {
					if (si.points < 0) {
						//null check 
						if(rs.negPoints == null){
							rs.negPoints = new Integer(0);
						}
						rs.negPoints += si.points;
					} else {
						if(rs.posPoints == null){
							rs.posPoints = new Integer(0);
						}
						rs.posPoints += si.points;
					}
					break;
				} // end if student match incident
			} // end for incidents
		} // end for students

		// update the student points
		db().save().entities(students);
		ArrayList<StudentIncident> newIncidents = new ArrayList<>();
		newIncidents.addAll(db().save().entities(stuIncidents).now().values());
		return Response.ok().entity(newIncidents).build();
	}


	
	@GET
	@Path("/student/{studentId}/incident")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentIncidents(@PathParam("studentId") Long studentId) {

				List<StudentIncident> studentIncidents = db().load().type(StudentIncident.class)
						.filter("studentId", studentId).list();

				return Response.ok().entity(studentIncidents).build();

	}

	@GET
	@Path("/routine")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClasstimeList() {

		Roster result = ofy().load().type(Roster.class).first().now();

		if (result != null) {

			List<Routine> routineList = ofy().load().type(Routine.class).list();
			
			
			return Response.ok().entity(routineList).build();
		}

		return Response.status(Status.NOT_FOUND).build();

	}

	@GET
	@Path("/routine/{subId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClassTime(@PathParam("subId") Long classtimeId) {

		Roster result = ofy().load().type(Roster.class).first().now();

		if (result != null) {
				Routine routine = ofy().load().key(Key.create(Routine.class, classtimeId)).now();
				RoutineConfig config = ofy().load().key(Key.create(RoutineConfig.class,classtimeId)).now();
			
				return Response.ok().entity(new FullRoutine(routine,config)).build();
		}else{

		return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST
	@Path("/routine/{subId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createClassTime(FullRoutine fullRoutine) {

		Roster result = ofy().load().type(Roster.class).first().now();

		if (result != null) {

			Long newId = db().save().entity(fullRoutine.routine).now().getId();
			fullRoutine.routineConfig.id = newId;
			db().save().entity(fullRoutine.routineConfig);
			return Response.ok().entity(newId).build();
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	@GET
	@Path("/routine/{classtimeId}/seatingchart")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeatingChart(@PathParam("classtimeId") Long classtimeId) {
		// TODO:check for roster blah blah
		SeatingChart seatingChart = db().load().key(Key.create(SeatingChart.class, classtimeId)).now();
		if (seatingChart == null) {
			seatingChart = new SeatingChart();
			seatingChart.id = classtimeId;
			db().save().entity(seatingChart);
		}
		return Response.ok().entity(seatingChart).build();
	}

	@POST
	@Path("/routine/{classtimeId}/seatingchart")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSeatingChart(RoutineConfig config,
			SeatingChart seatingChart) {
		// TODO: set up checks
		db().save().entity(seatingChart);
		return Response.ok().build();
	}

	@GET
	@Path("/schedule")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchedule() {

	
		Schedule scheduleDB = db().load().type(Schedule.class).first().now();
		// in case of null create new
		if (scheduleDB == null) {
			scheduleDB = new Schedule();
			RosterInfo roster = db().load().type(RosterInfo.class).first().now();
			scheduleDB.name = roster.name.equalsIgnoreCase("schoolevents")?"rosterEvents":roster.name;
			//init with school schedule
			UserService us = UserServiceFactory.getUserService();
			User user = us.getCurrentUser();
			
			ScheduleItem recess = new ScheduleItem();
			recess.title = "Recess" ; 
			recess.color = "gray";
			scheduleDB.items.put("Recess",recess);
			ScheduleItem lunch = new ScheduleItem();
			lunch.title = "Lunch" ;
			lunch.color = "gray";
			scheduleDB.items.put("Lunch",lunch);
			
			 recess.start = "10:15"; recess.end="10:35";
			lunch.start = "12:10"; lunch.end="12:55";
			
			db().save().entity(scheduleDB).now().getId();
		}
		
		return Response.ok().entity(scheduleDB).build();

	}

	@POST
	@Path("/schedule")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveSchedule(Schedule schedule) {
			db().save().entity(schedule).now().getId();
			return Response.ok().build();
	}
	/*
	 * Every roster will always have one schedule so it can never be deleted
	 * just changed
	 */
	
	@GET
	@Path("incident")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listIncidents(){
		List<Incident> incidents = db().load().type(Incident.class).list();
		return Response.ok().entity(incidents).build();
	}
	
	@POST
	@Path("incident/")
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
	@Path("/incident/{incidentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateIncident(Incident incident){
		db().save().entity(incident);
		return Response.ok().entity(incident).build();
	}
	
	@DELETE
	@Path("/incident/{incidentId}")
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
	@Path("/attendance/{attendanceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response takeAttendance(@PathParam("attendanceId") String attendanceId){
		Attendance attendance = db().load().type(Attendance.class).id(attendanceId).now();
		
		
		if(attendance == null){
			attendance = new Attendance();
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
	@Path("/attendance/{attendanceId}")
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
	@Path("attendance/{attendanceId}")
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
	@Path("/{id}/work/{workId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createGradedWork(@PathParam("id")Long id, GradedWork work) throws IOException, ParseException{
		
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
	
	@GET
	@Path("/googlecal")
	@Produces(MediaType.APPLICATION_JSON)
	public Response calendarList() throws IOException{
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		Credential cred = GoogleUtils.cred(user.getUserId());
		com.google.api.services.calendar.Calendar calendar = GoogleUtils.calendar(cred);
		
		return Response.ok().entity(calendar.calendarList().list().setFields("items/id,items/summary,items/description").execute()).build();
	}
	
}