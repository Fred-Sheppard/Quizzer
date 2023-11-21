import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     */
    public void createUser(String user, String password) {
        // If the user already exists
        if (this.userExists(user)) {
            return;
        }
        // Hash the password using SHA-256
        String hashedPassword = this.hash(password);
        // Append the new username and password to the file
        try (FileWriter writer = new FileWriter(userFile, true)) {
            writer.write(String.format("%s,%s%n", user, hashedPassword));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param username The username to check
     * @return If the user already exists
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    /**
     * Checks a given username and password to see if they are equal
     *
     * @param user     The username of the user
     * @param password The password of the user
     */
    public void checkCredentials(String user, String password) {
        if (!users.containsKey(user)) {
            throw new UserNotFoundError(String.format("User %s could not be found", user));
        }
        // Hash the password using SHA-256
        String hashedPass = this.hash(password);
        if (!users.get(user).equals(hashedPass)) {
            throw new IncorrectPasswordError("Password was incorrect for the given user");
        }
    }

    /**
     * Hashes the given String using SHA-256.
     *
     * @param input The String to be hashed
     * @return The hash of the given String
     */
    private String hash(String input) {
        byte[] bytes;
        try {
            bytes = MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            // Should not fail, if it does we cannot log the user in
            throw new RuntimeException(e);
        }
        // Convert the bytes into an int
        BigInteger number = new BigInteger(1, bytes);
        // Convert to hex
        StringBuilder builder = new StringBuilder(number.toString(16));
        /* Pad with leading zeros */
        while (builder.length() < 32) {
            builder.insert(0, '0');
        }
        return builder.toString();
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
