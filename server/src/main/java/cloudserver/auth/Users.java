package cloudserver.auth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/*
 static users data
 */
public class Users {

    public final static Map<String, String> registeredUsers = new HashMap<>(3);
    static {
        registeredUsers.put("Ilya", "1234");
        registeredUsers.put("vanya", "Qw12");
        registeredUsers.put("nagibator777", "00000");
    }

    public final static Map<String, Integer> userLogInAttempts = new HashMap<>(3);
    static {
        userLogInAttempts.put("Ilya", 3);
        userLogInAttempts.put("vanya", 3);
        userLogInAttempts.put("nagibator777", 3);
    }

    public static Set<String> loginsOfLoggedInUsers = new HashSet<>();
}
