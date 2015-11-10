import java.util.ArrayList;
import java.util.List;

// Class which models a single user node within graph
public class User {
	private int userId;
	private int version;
	private List<User> students;
	private List<User> teachers;

	public User() {
	}

	public User(int userId, int version) {
		this.userId = userId;
		this.version = version;
		students = new ArrayList<User>();
		teachers = new ArrayList<User>();
	}

	/**
		Getters and setters
	**/
	public int getUserId() {
		return userId;
	}
	public int getVersion() {
		return version;
	}
	// return copy of student list, in array form
	public User[] getStudents() {
		return students.toArray(new User[students.size()]); // store elements of students arraylist in this new array
	}
	// return copy of student list, in array form 
	public User[] getTeachers() {
		return teachers.toArray(new User[teachers.size()]); // store elements of teachers arraylist in this new array
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	/**
		Instance-specific methods. 
	**/
	// worse case O(n)
	public boolean hasStudent(User user) {
		for(int i = 0; i < students.size(); i++) {
			if (students.get(i).getUserId() == user.getUserId()) {
				return true;
			}
 		}

 		return false;
	}

	// worst case O(n)
	public boolean hasTeacher(User user) {
		for(int i = 0; i < teachers.size(); i++) {
			if (teachers.get(i).getUserId() == user.getUserId()) {
				return true;
			}
 		}
 		return false;
	}

	// worse case O(n). Returns success
	public boolean addStudent(User user) {
		if (!hasStudent(user)) {
			students.add(user);
			return true;
		}

		return false;
	}
	// worse case O(n). returns success
	public boolean addTeacher(User user) {
		if (!hasTeacher(user)) {
			teachers.add(user);
			return true;
		}

		return false;
	}

	// worse case O(n)
	public void removeStudent(User user) {
		for(int i = 0; i < students.size(); i++) {
			if (students.get(i).getUserId() == user.getUserId()) {
				students.remove(i);
				break;
			}
 		}
	}
	// worse case O(n)
	public void removeTeacher(User user) {
		for(int i = 0; i < teachers.size(); i++) {
			if (teachers.get(i).getUserId() == user.getUserId()) {
				teachers.remove(i);
				break;
			}
 		}
	}
}