package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.image.Image;

public class Album implements Serializable{
	private static final long serialVersionUID = 1L;
	public String name;
	public int numPhotos;
	public ArrayList<Photo> photos;
	
	public Album(String name) {
		this.name = name;
		numPhotos = 0;
		photos = new ArrayList<Photo>();
	}
	
	public String earliestDate() {
		if(numPhotos == 0) {
			return "";
		}
		Date earliest = new Date();
		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).captureDate.before(earliest)) {
				earliest = photos.get(i).captureDate;
			}
		}
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		return ft.format(earliest);
	}
	
	public String latestDate() {
		if(numPhotos == 0) {
			return "";
		}
		Date latest = new Date(Long.MIN_VALUE);
		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).captureDate.after(latest)) {
				latest = photos.get(i).captureDate;
			}
		}
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		return ft.format(latest);
	}
	
	public String toString() {
		return name + ": (" + numPhotos + ")\n" + earliestDate() + "-" + latestDate();
	}
	
	public Image getThumb() {
		if(numPhotos == 0) {
			return new Image("/resources/albumthumb.png");
		}
		return new Image(photos.get(0).location);
	}
	
	public void addPhoto(String location) {
		photos.add(new Photo(location));
		numPhotos++;
	}
	
	public void deletePhoto(String location) {
		for(int i = 0; i < photos.size(); i++) {
			if(location.equals(photos.get(i).location)) {
				photos.remove(i);
				numPhotos--;
				return;
			}
		}
	}
	
	public Photo getPhoto(String location) {
		for(int i = 0; i < photos.size(); i++) {
			if(location.equals(photos.get(i).location)) {
				return photos.get(i);
			}
		}
		return null;
	}
}
