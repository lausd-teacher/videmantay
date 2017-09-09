package net.videmantay.server.rest;

import com.google.appengine.api.taskqueue.DeferredTask;
import java.util.List;
import net.videmantay.server.entity.Attendance;

import static  net.videmantay.server.DB.db;
public class DeleteAttendance implements DeferredTask {
	
	private final Long rosterId;
	
	public DeleteAttendance(Long rosterId){
		this.rosterId = rosterId;
		this.run();
	}

	@Override
	public void run() {
		List<Attendance>attendance = db().load().type(Attendance.class).filter("rosterId", rosterId).list();
		if(attendance != null){
		for(Attendance key:attendance){
			//TODO: fix this delete -- db().delete().keys(key.attendanceKeys);
		}
		db().delete().entities(attendance);
	}
	}//end if

}
