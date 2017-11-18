package models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	public String username;
	public ArrayList<Album> albums;
	
	public User(String username) {
		this.username = username;
		albums = new ArrayList<Album>();
	}
	
	public String toString() {
		return username;
	}
	
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	public void renameAlbum(String name, String newName) {
		for(int i = 0; i < albums.size(); i++) {
			if(name.equals(albums.get(i).name)) {
				albums.get(i).name = newName;
				albums.get(i).location.replace(name, newName);
			}
		}
	}
	
	public void deleteAlbum(String name) {
		for(int i = 0; i < albums.size(); i++) {
			if(name.equals(albums.get(i).name)) {
				albums.remove(i);
				return;
			}
		}
	}
	
	public Album getAlbum(String name) {
		for(int i = 0; i < albums.size(); i++) {
			if(name.equals(albums.get(i).name)) {
				return albums.get(i);
			}
		}
		return null;
	}
}
