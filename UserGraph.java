import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

// class which models user network as a graph
public class UserGraph {
	private int numUsers;
	private int networkVersion;
	private List<User> userList; // adjacency list for user connections

	public UserGraph() {
		numUsers = 0;
		userList = new ArrayList<User>();
	}
	public UserGraph(int networkVersion) {
		numUsers = 0;
		this.networkVersion = networkVersion;
		userList = new ArrayList<User>();
	}

	public int getNumUsers() {
		return numUsers;
	}
	public int getNetworkVersion() {
		return networkVersion;
	}
	public User[] getAllUsers() {
		return userList.toArray(new User[numUsers]);
	}

	public void addUserToGraph(User user) {
		userList.add(user);
		numUsers++;
	}

	// Total infection. Worse case is O(numUsers)
	public void updateUserVersions(int newVersion) {
		// the network version should now be this new version
		networkVersion = newVersion;

		// store a set of users in graph with initial capacity of powering hashmap is set to numUsers
		HashSet<User> usersToBeInfected = new HashSet<User>(numUsers);
		for(int i = 0; i < numUsers; i++) {
			usersToBeInfected.add(userList.get(i));
		}

		while (!usersToBeInfected.isEmpty()) {
			// grab any random user from the set by iterating through it
			Iterator<User> userIterator = usersToBeInfected.iterator();
			User user = userIterator.next(); // we don't need to check hasNext beause we know set is nonempty

			// grab all of the users teachers and all of its students
			User[] students = user.getStudents();
			User[] teachers = user.getTeachers();

			// start by infecting the user and removing it from usersToBeInfected set
			user.setVersion(newVersion);
			usersToBeInfected.remove(user);

			// infect all students
			for(int i = 0; i < students.length; i++) {
				students[i].setVersion(newVersion);		
				// attempt to remove this user from users to be infected set since they've been infected
				usersToBeInfected.remove(students[i]);
			}

			// infect all teachers
			for(int i = 0; i < teachers.length; i++) {
				teachers[i].setVersion(newVersion);		
				// attempt to remove this user from users to be infected set since they've been infected
				usersToBeInfected.remove(teachers[i]);
			}
		}
	}

	// Limited infection. 
	public void updateUserVersions(int newVersion, int maxInfected) {
		// the network version should now be this new version
		networkVersion = newVersion;
		// keep track of current infected
		int numInfected = 0;

		// Grab all uninfected users
		HashSet<User> usersToBeInfected = new HashSet<User>(numUsers);
		for(int i = 0; i < numUsers; i++) {
			usersToBeInfected.add(userList.get(i));
		}

		while (!usersToBeInfected.isEmpty()) {
			// grab any random user from the set by iterating through it
			Iterator<User> userIterator = usersToBeInfected.iterator();
			User user = userIterator.next(); // we don't need to check hasNext beause we know set is nonempty

			// grab all of the users teachers and all of its students
			User[] students = user.getStudents();
			User[] teachers = user.getTeachers();

			// start by infecting the user and removing it from usersToBeInfected set
			user.setVersion(newVersion);
			usersToBeInfected.remove(user);

			// infect all students
			for(int i = 0; i < students.length; i++) {
				students[i].setVersion(newVersion);		
				// attempt to remove this user from users to be infected set since they've been infected
				usersToBeInfected.remove(students[i]);
				numInfected++;

				if (numInfected == maxInfected) {
					return;
				}
			}

			// infect all teachers
			for(int i = 0; i < teachers.length; i++) {
				teachers[i].setVersion(newVersion);		
				// attempt to remove this user from users to be infected set since they've been infected
				usersToBeInfected.remove(teachers[i]);

				if (numInfected == maxInfected) {
					return;
				}
			}
		}
	}
}