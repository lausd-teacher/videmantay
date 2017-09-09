package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;


public class Students implements Serializable{
	public ArrayList<RosterStudent> roster = new ArrayList<>();
	public String message = "";
}
