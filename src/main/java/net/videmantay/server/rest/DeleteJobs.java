package net.videmantay.server.rest;

import com.google.appengine.api.taskqueue.DeferredTask;

import net.videmantay.server.entity.JobBoard;

import static net.videmantay.server.DB.db;

import java.util.List;

public class DeleteJobs implements DeferredTask {

	private final Long rosterId;
	
	public DeleteJobs(Long id){
		rosterId = id;
	}
	@Override
	public void run() {
	List<JobBoard> jobs =	db().load().type(JobBoard.class).filter("rosterId", rosterId).list();
	if(jobs != null){
	for(JobBoard jb:jobs){
		db().delete().keys(jb.jobKeys);
	}
		db().delete().entities(jobs);
	}
	}
}
