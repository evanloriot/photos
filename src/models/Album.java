package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.image.Image;

/**
 * The album class keeps track of the photos that are added to it, 
 * the album thumbnail, and the range of the dates that the album covers
 * @author Evan Loriot
 * @author Joseph Klaszky
 */

public class Album implements Serializable{
	/**
	 * Need for implementation of Serializable.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Name of the album.
	 */
	public String name;
	/**
	 * Number of photos that the album contains.
	 */
	public int numPhotos;
	/**
	 * List of all the photo objects that the album contains.
	 */
	public ArrayList<Photo> photos;
	
	/**
	 * Constructor
	 * @param name Name of the album
	 */
	public Album(String name) {
		this.name = name;
		numPhotos = 0;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * Iterates over the album and finds the photo that has the 
	 * oldest date.
	 * @return String -- A string that represents date of the oldest photo. 
	 */
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
	
	/**
	 * Iterates over the album and finds the most recent photo.
	 * @return String -- A string that represents the date of the most recent photo.
	 */
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
	
	/**
	 * @returns String -- Returns a string of the form: "Album name: (number of photos) earliest date - latest date. 
	 */
	public String toString() {
		return name + ": (" + numPhotos + ")\n" + earliestDate() + "-" + latestDate();
	}
	
	/**
	 * Gets the first image of an album to use as a thumbnail.
	 * @return Image -- The first photo present in the album.
	 */
	public Image getThumb() {
		if(numPhotos == 0) {
			return new Image("/resources/albumthumb.png");
		}
		return new Image(photos.get(0).location);
	}
	
	/**
	 * Adds a new photo to the album and increments the number of photos.
	 * @param location The location of the photo being added to the album.
	 */
	public Photo addPhoto(String location) {
		int instance = 1;
		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).location.equals(location)) {
				instance++;
			}
		}
		Photo output = new Photo(location, instance);
		photos.add(output);
		numPhotos++;
		return output;
	}
	
	/**
	 * Removes a photo from the album and decrements the number of photos.
	 * @param location The location of the photo being removed from the album. 
	 */
	public void deletePhoto(String location) {
		for(int i = 0; i < photos.size(); i++) {
			if(location.equals(photos.get(i).location)) {
				photos.remove(i);
				numPhotos--;
				return;
			}
		}
	}
	
	/**
	 * Iterates over the album to find a particular photo.
	 * @param location The location of the photo the method is looking for.
	 * @return Photo -- If found returns the photo object from the album.
	 */
	public Photo getPhoto(String location, int instance) {
		for(int i = 0; i < photos.size(); i++) {
			if(location.equals(photos.get(i).location) && photos.get(i).instance == instance) {
				return photos.get(i);
			}
		}
		return null;
	}
}
