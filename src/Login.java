import java.io.*;
import java.util.HashMap;


public class Login {

    private final File userFile;
    private final HashMap<String, String> map;

    /**
     * Creates a new Login object.
     *
     * @param userFile File in which credentials are to be saved
     */
    public Login(File userFile) {
        this.userFile = userFile;
        map = new HashMap<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(userFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        reader.lines().forEach(line -> {
            String[] split = line.split(",");
            map.put(split[0], split[1]);
        });
    }

    /**
     * Creates a user with the given username and password.
     *
     * @param user     The username of the user
     * @param password The password of the user
     * @return If the user was created successfully
     */
    public boolean createUser(String user, String password) {
        if (map.containsKey(user)) {
            System.out.println("User already exists.");
            Main.promptEnter();
            return false;
        }
        int hashedPassword = password.hashCode();
        try (FileWriter writer = new FileWriter(userFile, true)) {
            writer.write(user + "," + hashedPassword + "\n");
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
     * @return If the login was successful
     */
    public boolean checkCredentials(String user, String password) {
        String hashedPass = String.valueOf(password.hashCode());
        if (!map.containsKey(user)) {
            System.out.println("Username not found");
            Main.promptEnter();
            return false;
        }
        if (!map.get(user).equals(hashedPass)) {
            System.out.println("Incorrect password");
            Main.promptEnter();
            return false;
        }
        return true;
    }
}
