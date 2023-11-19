import java.io.*;
import java.util.HashMap;


/**
 * Class for dealing with logging in the user.
 */
public class Login {

    /**
     * The file containing the user's history.
     */
    private final File userFile;
    /**
     * Maps users to their password hash.
     */
    private final HashMap<String, String> users;

    /**
     * Creates a new Login object.
     *
     * @param userFile File in which credentials are to be saved
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Login(File userFile) {
        this.userFile = userFile;
        if (!userFile.exists()) {
            userFile.getParentFile().mkdirs();
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        users = new HashMap<>();
        // Load the user history from their file
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(userFile));
        } catch (FileNotFoundException e) {
            // We created the file above, we know it exists
            throw new RuntimeException(e);
        }
        reader.lines().forEach(line -> {
            String[] split = line.split(",");
            users.put(split[0], split[1]);
        });
    }

    /**
     * Creates a user with the given username and password.
     * Returns false if the user already exists.
     *
     * @param user     The username of the user
     * @param password The password of the user
     * @return Returns false if the user already exists
     */
    public boolean createUser(String user, String password) {
        if (users.containsKey(user)) {
            return false;
        }
        // Hash the password using the default Java hashcode
        int hashedPassword = password.hashCode();
        // Append the new username and password to the file
        try (FileWriter writer = new FileWriter(userFile, true)) {
            writer.write(String.format("%s,%d%n", user, hashedPassword));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Checks a given username and password to see if they are equal
     *
     * @param user     The username of the user
     * @param password The password of the user
     */
    public void checkCredentials(String user, String password) {
        String hashedPass = String.valueOf(password.hashCode());
        if (!users.containsKey(user)) {
            throw new UserNotFoundError(String.format("User %s could not be found", user));
        }
        if (!users.get(user).equals(hashedPass)) {
            throw new IncorrectPasswordError("Password was incorrect for the given user");
        }
    }

    static class UserNotFoundError extends Error {
        public UserNotFoundError(String message) {
            super(message);
        }
    }

    static class IncorrectPasswordError extends Error {
        public IncorrectPasswordError(String message) {
            super(message);
        }
    }
}
