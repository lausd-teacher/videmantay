package net.videmantay.server.rest;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static net.videmantay.server.DB.db;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import org.codehaus.jackson.map.ObjectMapper;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import net.videmantay.server.entity.FullRoutine;
import net.videmantay.server.entity.Routine;
import net.videmantay.server.entity.RoutineConfig;
import net.videmantay.server.entity.Incident;
import net.videmantay.server.entity.Roster;
import net.videmantay.server.entity.RosterInfo;
import net.videmantay.server.entity.RosterStudent;
import net.videmantay.server.entity.SchoologyInfo;
import net.videmantay.server.entity.SeatingChart;
import net.videmantay.server.entity.StudentConfig;
import net.videmantay.server.entity.StudentIncident;
import net.videmantay.server.entity.StudentWork;
import net.videmantay.server.schoology.Enrollment;
import net.videmantay.server.schoology.EnrollmentList;
import net.videmantay.server.schoology.SchoologyApi;

@Path("/roster")
public class RosterService {

	private final Logger log = Logger.getLogger("Roster Service");


	
/* Roster list is now loaded directly in to page
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRosters() throws Exception {
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		SchoologyInfo info = db().load().type(SchoologyInfo.class).id(user.getUserId()).now();
		
		List<RosterInfo> response = RosterApi.rosterList(user, info);
			return Response.status(Status.CREATED).entity(response).build();
		
		}
	
	private boolean ownsRoster(Long id, String userId){
		RosterInfo rosInfo = db().load().type(RosterInfo.class).id(id).now();
		return rosInfo.ownerId.equalsIgnoreCase(userId);
	}*/
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response  getRoster(@PathParam("id")String id) throws IOException{
		
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		SchoologyInfo info = db().load().type(SchoologyInfo.class).id(user.getUserId()).now();
		
			Roster response = db().load().type(Roster.class).id(id).now();
			
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
			if(info != null ) {
				//get enrollment
				EnrollmentList enrollList = SchoologyApi.getEnrollmentList(info, id);
				List<RosterStudent> rosStudentList = db().load().type(RosterStudent.class).filter("rosterId", id).list();
				
				ArrayList<String> enrollIds = new ArrayList<>();
				ArrayList<String> rosStuIds = new ArrayList<>();
				for(Enrollment e: enrollList.enrollment) {
					enrollIds.add(e.id);
				}
				//check for out of sync students
				for(RosterStudent rs: rosStudentList) {
					if(!enrollIds.contains(rs.id)) {
						rs.synced = false;
					}
					rosStuIds.add(rs.id);
				}
				//now check for new students
				ArrayList<RosterStudent> newStudentList = new ArrayList<>();
				for(Enrollment enroll: enrollList.enrollment) {
					if(enroll.admin == 1) {
						continue;
					}
					if(!rosStuIds.contains(enroll.id)) {
						RosterStudent student = new RosterStudent();
						student.rosterId = response.id;
						student.id = enroll.id;
						student.firstName = enroll.name_first;
						student.lastName = enroll.name_last;
						student.picUrl = enroll.picture_url;
						
						newStudentList.add(student);
					}
				}//end for enroll 
				
				if(newStudentList.size() > 0) {
					db().save().entities(newStudentList);
					rosStudentList.addAll(newStudentList);
				}
				
				response.students = rosStudentList;
			}
			
		
			ObjectMapper mapper = new ObjectMapper();
			String repJson = mapper.writeValueAsString(response);
		log.info("response in json is " + repJson );
		return Response.ok().entity(response).build();
			
	}
	

	
	
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRoster(@PathParam("id") Long id, final RosterInfo roster){
		//figure out how to delete all the roster
		db().delete().type(RosterInfo.class).id(id);
		db().delete().type(Roster.class).id(id);
		return Response.ok().entity(id).build();
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
	public Response getRosterStudent(@PathParam("id")Long id, @PathParam("uid") String uid) throws IOException {
		//here we need everything that the student page needs to get started
		StudentConfig config = new StudentConfig();
	
		config.assignments = db().load().type(StudentWork.class).filter("studentId", uid).limit(20).list();
	
		config.incidents=db().load().type(StudentIncident.class).filter("studentId", uid).limit(20).list();
		
		return Response.ok().entity(config).build();
		
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
	
//schoology will handle all assignments
		
/*	
	
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
	}*/
		
}
