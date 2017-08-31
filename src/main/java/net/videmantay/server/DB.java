package net.videmantay.server;

import static com.googlecode.objectify.ObjectifyService.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Work;

import net.videmantay.server.entity.*;


public class DB<T> implements Serializable {
	
	static {
		
		//Roster and Deps
		factory().register(RosterInfo.class);
		factory().register(RosterStudent.class);
		factory().register(SeatingChart.class);
		factory().register(Routine.class);
		factory().register(RoutineConfig.class);
		factory().register(Incident.class);
		factory().register(GoogleCalendarEvent.class);
		factory().register(GradedWork.class);
		factory().register(Schedule.class);
		factory().register(Attendance.class);
		factory().register(StudentAttendance.class);
		factory().register(StudentWork.class);
		factory().register(StudentJob.class);
		factory().register(StudentIncident.class);
		//schoology stuff here
	}
	

	
	private DB(){
		
	}
	
	
	public static Objectify db(){
		return ofy();
	}
	//Revist using Generics at a later time for now just use objectify	
	private Class<T> clazz;
 
  
  public DB(Class<T> clazz){
		this.clazz = clazz;
	}
  
  
  public  Key<T> save(final T clazz){
	
		return ofy().transactNew(new Work<Key<T>>(){

			@Override
			public Key<T> run() {
				// TODO Auto-generated method stub
				return db().save().entity(clazz).now();
			}});
	}
	
	public  void delete(T entity){
		DB.db().delete().entity(entity).now();
	}
	
	public  List<T> list(){
		return DB.db().transactNew(new Work<List<T>>(){

			@Override
			public List<T> run() {
				return DB.db().load().type(clazz).list();
			}});
		
	}
	
	public  List<T> query(String condition, Object value){
		
	return	DB.db().load().type(clazz).filter(condition, value).list();
	}
	
	public  T getById(Long id){
		
	 return	DB.db().load().type(clazz).id(id).now();
	}
	
	public void deleteById(Long id){
		DB.db().delete().type(clazz).id(id);
		
	}
	
	public Collection<T> listByKeys(List<Key<T>> keys){
	
		return DB.db().load().keys(keys).values();
	}
	
	public List<T> search(String field, String query){
		return    DB.db().load().type(clazz).filter(field +" >=", query).filter(field + " <=", query + "\ufffd").list();
		}
	
}
		


