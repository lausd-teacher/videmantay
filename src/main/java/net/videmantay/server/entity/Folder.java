package net.videmantay.server.entity;

import java.io.Serializable;

public class Folder implements Serializable {
	public String name;
	public String url;
	
	public Folder(){}
	public Folder(String name, String url){
		this.name = name;
		this.url = url;
	}
}
