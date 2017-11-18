package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.Album;
import models.User;

public class SerialUtils implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String storeDir =  new File("").getAbsolutePath() +"\\src\\models\\dat";
	public static final String fileEnd = ".dat";
	public static final String usersFile = "userList.dat";
	
	public static boolean nameTaken(List<User> userList, String newUser){
		for(User u : userList){
			if(u.toString().equals(newUser)){
				return true;
			}
		}
		
		return false;
	}
	
	public static void writeUserToFile(User user) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(storeDir + File.separator + user.toString() + fileEnd));
		oos.writeObject(user);
		oos.close();
	}
	
	public static User readUserFromFile(String name) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + name + fileEnd));
		User user = (User)ois.readObject();
		ois.close();
		return user;
	}
	

	public static boolean deleteUserFromFile(String userName){
		File f = new File(storeDir + File.separator + userName + fileEnd);
		return f.delete();
	}
	
	public static ArrayList<User> getUserList() throws IOException{
		File dir = new File(storeDir);
		File[] directoryListing = dir.listFiles();
		ArrayList<User> users = new ArrayList<>();
		User user;
		if(directoryListing != null){
			for(File f : directoryListing){
				try{
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f.getAbsolutePath()));
					user = (User)ois.readObject();
					users.add(user);
					ois.close();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return users;
	}
	
	public static User getUser(ArrayList<User> userList, String userName){
		for(User u : userList){
			if(u.toString().equals(userName)){
				return u;
			}
		}
		
		return null;
	}
}
