package net.videmantay.server.rest;

import static net.videmantay.server.DB.db;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.googlecode.objectify.Key;

import net.videmantay.server.entity.Routine;
import net.videmantay.server.entity.RoutineConfig;
import net.videmantay.server.entity.SeatingChart;

public class DeleteRoutines implements DeferredTask {

	private final Long rosterId;
	
	public DeleteRoutines(Long rosterId){
		this.rosterId =rosterId;
		run();
	}
	@Override
	public void run() {
		List<Key<Routine>> keys = db().load().type(Routine.class).filter("rosterId",rosterId).keys().list();
		if(keys != null){
		ArrayList<Long>ids = new ArrayList<Long>();
		for(Key<Routine> key:keys){
			ids.add(key.getId());
		}
		db().delete().type(RoutineConfig.class).ids(ids);
		db().delete().type(SeatingChart.class).ids(ids);
		db().delete().keys(keys);
	}
	}

}
