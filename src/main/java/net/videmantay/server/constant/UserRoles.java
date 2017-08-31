package net.videmantay.server.constant;

public enum UserRoles {

	ADMIN("ADMIN"),STUDENT("STUDENT"),TEACHER("TEACHER"),FACULTY("FACULTY");
	private String role;
	private UserRoles(String role){
		this.role = role;
	}
	
	@Override 
	public String toString(){
		return role;
	}
}
