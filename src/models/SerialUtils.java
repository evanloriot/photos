package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class used to manage serializing/storing all of the users' data. 
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class SerialUtils implements Serializable{
	/**
	 * Need for implementation of Serializable.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The directory that will be used to store the users' data.
	 */
	public static final String storeDir =  new File("").getAbsolutePath() +"\\src\\models\\dat";
	/**
	 * The file extension to be used.
	 */
	public static final String fileEnd = ".dat";
	
	/**
	 * Checks to see if the a user name is already taken when the admin is trying to add new users.
	 * @param userList The list of all known users.
	 * @param newUser The name of the user that is trying to be added.
	 */
	public static boolean nameTaken(List<User> userList, String newUser){
		for(User u : userList){
			if(u.toString().equals(newUser)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Takes a user object and serializes the data and stores it.
	 * @param user The user object to be stored.
	 * @exception IOException -- If it can't open the file.
	 * @see IOException
	 */
	public static void writeUserToFile(User user) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(storeDir + File.separator + user.toString() + fileEnd));
		oos.writeObject(user);
		oos.close();
	}
	
	/**
	 * Takes a name and attempts to open the file that would be associated with that name.
	 * @param name The name of the user who's file you would like to open.
	 * @exception IOException -- If it can't open the file.
	 * @see IOException
	 * @exception ClassNotFoundException -- If it can't find a user class.
	 * @see ClassNotFoundException
	 * @return User -- If it could find the user that was requested it returns a user object.
	 */
	public static User readUserFromFile(String name) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + name + fileEnd));
		User user = (User)ois.readObject();
		ois.close();
		return user;
	}
	
	/**
	 * Looks for a certain file and deletes it.
	 * @param userName The name of the user we would like to remove.
	 * @return boolean -- True if were able to delete the user, false otherwise.
	 */
	public static boolean deleteUserFromFile(String userName){
		File f = new File(storeDir + File.separator + userName + fileEnd);
		return f.delete();
	}
	
	/**
	 * Iterates through the all the files that contains the user objects, collects them,
	 * and returns a list of them.
	 * @exception IOException -- If it can't open a file
	 * @see IOException
	 * @return ArrayList<User> -- De-serializes and returns all users found in models.dat
	 */
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
				} catch(ClassNotFoundException e){
					System.out.println("Error when reading the list of users from file.");
					e.printStackTrace();
				}
			}
		}
		return users;
	}
	
	/**
	 * Looks for a specific user
	 * @param userList List of all the users
	 * @param userName Name of the user that's currently being looked for
	 * @return User -- If found returns the user object, null otherwise.
	 */
	public static User getUser(ArrayList<User> userList, String userName){
		for(User u : userList){
			if(u.toString().equals(userName)){
				return u;
			}
		}
		
		return null;
	}
}
