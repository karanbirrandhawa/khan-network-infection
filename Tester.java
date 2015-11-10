import java.util.Random;

// Class which contains test logic for infection project
public class Tester {
	// Used for color in command lines (doesn't work for Windows Command Prompt)
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static void main(String[] args) {
		System.out.println("Test cases begin! \n");

		System.out.println("Test case 1: Empty network (0 users)");
		testCase1();
		System.out.println("Test case 2: Random network (1 users)");
		testCase2();
		System.out.println("Test case 3: Random network (5 users)");
		testCase3();
		System.out.println("Test case 4: Random network (15 users)");
		testCase4();
		System.out.println("Test case 5: Random network (50 users)");
		testCase5();
		System.out.println("Test case 6: Random network (2500 users)");
		testCase6();
		System.out.println("Test case 7: Network with student-teacher cycle");
		testCase7();
		System.out.println("Test case 8: Network with isolated user");
		testCase7();
	}

	/** 
		Helper methods 
	**/
	// Instantiate a network with a certain number of users randomly connected
	static UserGraph initializeRandomNetwork(int numUsers, int version, int minConnections) {
		Random rng = new Random();
		User[] users = new User[numUsers];
		// Instantiate a certain number of users
		for(int i = 0; i < numUsers; i++) {
			users[i] = new User(i, version);
		}

		// Set users to have students and teachers that are other users
		for(int i = 0; i < numUsers; i++) {
			User user = users[i];

			// attempt to make a certain amount of student connections to the user nodes
			for (int j = 0; j < minConnections; j++) {
				int randomIndex = rng.nextInt(numUsers);
				user.addStudent(users[randomIndex]); 
				users[randomIndex].addTeacher(user);
			}
		}

		// Add all users to network

		UserGraph graph = new UserGraph(version);
		for (int i = 0; i < numUsers; i++) {
			graph.addUserToGraph(users[i]);
		}

		return graph;
	}

	// checks if all users in graph are a certain version and return how many are not
	static int checkUserVersion(UserGraph graph, int version) {
		User[] users = graph.getAllUsers();
		int incorrectlyVersionedUsers = 0;
		for (int i = 0; i < users.length; i++) {
			if (users[i].getVersion() != 2) {
				incorrectlyVersionedUsers++;
			}
		}

		return incorrectlyVersionedUsers;
	}

	// test case logic for random network
	static void totalInfectionTestRandomNetwork(int numUsers, int minConnections) {
		System.out.println(ANSI_BLUE + "Initializing network as version 1.." + ANSI_RESET);
		UserGraph graph = initializeRandomNetwork(numUsers, 1, minConnections);

		System.out.println(ANSI_BLUE + "Running total infection. Update network to version 2!" + ANSI_RESET);

		graph.updateUserVersions(2);

		User[] users = graph.getAllUsers();
		int unupdatedUserCount = checkUserVersion(graph, 2);
		boolean pass = unupdatedUserCount == 0;

		String results = pass 
							? (ANSI_GREEN + "Pass! All users are version 2." + ANSI_RESET)
							: (ANSI_RED + "Fail. " + unupdatedUserCount + " users are not version 2." + ANSI_RESET);

		System.out.println(results + "\n");
	}

	static void limitedInfectionTestRandomNetwork(int numUsers, int minConnections, int maxUsersInfected) {
		System.out.println(ANSI_BLUE + "Initializing network as version 1.." + ANSI_RESET);
		UserGraph graph = initializeRandomNetwork(numUsers, 1, minConnections);

		System.out.println(ANSI_BLUE + "Running limited infection for " + maxUsersInfected + " users. Update network to version 2!" + ANSI_RESET);

		graph.updateUserVersions(2, maxUsersInfected);

		User[] users = graph.getAllUsers();
		int unupdatedUserCount = checkUserVersion(graph, 2);
		boolean pass = unupdatedUserCount <= numUsers - maxUsersInfected;

		String results = pass 
							? (ANSI_GREEN + "Pass! All users are version 2." + ANSI_RESET)
							: (ANSI_RED + "Fail. Incorrect number of users are not version 2." + ANSI_RESET);

		System.out.println(results + "\n");
	}
	/** 
		Test case methods 
	**/
	// Empty network
	static void testCase1() {
		totalInfectionTestRandomNetwork(0, 3);
		limitedInfectionTestRandomNetwork(0, 3, 0);
	}

	// Random network with 1 user
	static void testCase2() {
		totalInfectionTestRandomNetwork(1, 3);
		limitedInfectionTestRandomNetwork(1, 3, 0);
		limitedInfectionTestRandomNetwork(1, 3, 1);
	}

	// Random network with 5 users
	static void testCase3() {
		totalInfectionTestRandomNetwork(5, 3);
		limitedInfectionTestRandomNetwork(5, 3, 2);
	}

	// Random network with 15 users
	static void testCase4() {
		totalInfectionTestRandomNetwork(15, 3);
		limitedInfectionTestRandomNetwork(15, 3, 4);
	}

	// Random network with 50 users
	static void testCase5() {
		totalInfectionTestRandomNetwork(50, 3);
		limitedInfectionTestRandomNetwork(50, 3, 45);
	}

	// Random network with 2500 users
	static void testCase6() {
		totalInfectionTestRandomNetwork(2500, 30);
		limitedInfectionTestRandomNetwork(2500, 30, 800);
	}

	// Network which contains teacher-student cycle (user's student is also user's teacher)
	static void testCase7() {
		User user1 = new User(0, 1);
		User user2 = new User(1, 1);
		User user3 = new User(2, 1);

		// User1 teaches user2
		user1.addStudent(user2);
		user2.addTeacher(user1);

		// User1 also teachers user3
		user1.addStudent(user3);
		user3.addStudent(user1);

		// User2 teachers user1. This means user1 and 2 both teach other (cycle)
		user2.addStudent(user1);
		user1.addTeacher(user2);

		// Add all users to graph
		System.out.println(ANSI_BLUE + "Initializing network as version 1.." + ANSI_RESET);
		UserGraph graph = new UserGraph(1);
		graph.addUserToGraph(user1);
		graph.addUserToGraph(user2);
		graph.addUserToGraph(user3);

		System.out.println(ANSI_BLUE + "Running total infection. Update network to version 2!" + ANSI_RESET);
		graph.updateUserVersions(2);

		User[] users = graph.getAllUsers();
		int unupdatedUserCount = checkUserVersion(graph, 2);
		boolean pass = unupdatedUserCount == 0;

		String results = pass 
							? (ANSI_GREEN + "Pass! All users are version 2." + ANSI_RESET)
							: (ANSI_RED + "Fail. " + unupdatedUserCount + " users are not version 2." + ANSI_RESET);

		System.out.println(results + "\n");

		// Add all users to new graph, this time to test limited infection
		int maxUsersInfected = 2;
		System.out.println(ANSI_BLUE + "Running limited infection for " + maxUsersInfected + " users. Update network to version 3!" + ANSI_RESET);

		graph.updateUserVersions(3, maxUsersInfected);

		users = graph.getAllUsers();
		unupdatedUserCount = checkUserVersion(graph, 3); // check how many users in the graph are not version 2
		pass = unupdatedUserCount <= (3 - maxUsersInfected); 

		results = pass 
					? (ANSI_GREEN + "Pass! All users are version 2." + ANSI_RESET)
					: (ANSI_RED + "Fail. Incorrect number of users are not version 2." + ANSI_RESET);
	}

	// Network with 1 node isolated from rest
	static void testCase8() {
		User user1 = new User(0, 1);
		User user2 = new User(1, 1);
		User user3 = new User(2, 1);

		// User1 teaches user2
		user1.addStudent(user2);
		user2.addTeacher(user1);

		// Add all users to graph
		System.out.println(ANSI_BLUE + "Initializing network as version 1.." + ANSI_RESET);
		UserGraph graph = new UserGraph(1);
		graph.addUserToGraph(user1);
		graph.addUserToGraph(user2);
		graph.addUserToGraph(user3);

		System.out.println(ANSI_BLUE + "Running total infection. Update network to version 2!" + ANSI_RESET);
		graph.updateUserVersions(2);

		User[] users = graph.getAllUsers();
		int unupdatedUserCount = checkUserVersion(graph, 2);
		boolean pass = unupdatedUserCount == 0;

		String results = pass 
							? (ANSI_GREEN + "Pass! All users are version 2." + ANSI_RESET)
							: (ANSI_RED + "Fail. " + unupdatedUserCount + " users are not version 2." + ANSI_RESET);

		System.out.println(results + "\n");

		// Add all users to new graph, this time to test limited infection
		int maxUsersInfected = 2;
		System.out.println(ANSI_BLUE + "Running limited infection for " + maxUsersInfected + " users. Update network to version 3!" + ANSI_RESET);

		graph.updateUserVersions(3, maxUsersInfected);

		users = graph.getAllUsers();
		unupdatedUserCount = checkUserVersion(graph, 3); // check how many users in the graph are not version 2
		pass = unupdatedUserCount <= (3 - maxUsersInfected); 

		results = pass 
					? (ANSI_GREEN + "Pass! All users are version 2." + ANSI_RESET)
					: (ANSI_RED + "Fail. Incorrect number of users are not version 2." + ANSI_RESET);
	}
}