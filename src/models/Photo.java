package models;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Photo {
	public String caption;
	public Date captureDate;
	public ArrayList<String> tags;
	public String location;
	
	public Photo(String location) {
		this.location = location;
		caption = "";
		tags = new ArrayList<String>();
		File file = new File(location);
		captureDate = new Date(file.lastModified());
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public void deleteTag(String tag) {
		for(int i = 0; i < tags.size(); i++) {
			if(tag.equals(tags.get(i))) {
				tags.remove(i);
				return;
			}
		}
	}
}
