package net.videmantay.server.rest;

import java.util.List;
import com.googlecode.objectify.Key;
import static net.videmantay.server.DB.db;
import com.google.appengine.api.taskqueue.DeferredTask;

import net.videmantay.server.entity.RosterStudent;
import net.videmantay.server.entity.StudentGoal;

public class DeleteStudents implements DeferredTask {
	
	private final Long rosterId;
	
	public DeleteStudents(Long rosterId){
		this.rosterId = rosterId;
		this.run();
	}

	@Override
	public void run() {
		List<Key<RosterStudent>> keys =db().load().type(RosterStudent.class).filter("rosterId",rosterId).keys().list();
		if(keys != null){
		for(Key<RosterStudent>student:keys){
			//delete all student goals
			db().delete().keys(db().load().type(StudentGoal.class).filter("studentId", student.getName()).keys());
			//delete student and incidents
			db().delete().keys(db().load().ancestor(student).keys());
		}
	}
	}

}
