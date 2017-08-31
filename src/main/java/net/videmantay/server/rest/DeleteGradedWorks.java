package net.videmantay.server.rest;

import com.google.appengine.api.taskqueue.DeferredTask;
import java.util.List;
import com.googlecode.objectify.Key;

import net.videmantay.server.entity.GradedWork;
import net.videmantay.server.entity.StudentWork;

import static net.videmantay.server.DB.db;

public class DeleteGradedWorks implements DeferredTask {

	 private final Long rosterId;
	 
	 public DeleteGradedWorks(Long rosterId){
		 this.rosterId = rosterId;
		 this.run();
	 }
	@Override
	public void run() {
		List<Key<GradedWork>>keys = db().load().type(GradedWork.class).filter("rosterId", rosterId).keys().list();
		
		if(keys != null){
		for(Key<GradedWork> key: keys){
			db().delete().keys(db().load().type(StudentWork.class).filter("gradedWorkId",key.getId()).keys());
		}
		db().delete().keys(keys);
	}
	}

}
