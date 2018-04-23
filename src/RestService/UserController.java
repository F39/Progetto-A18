package RestService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final List<User> users = new ArrayList<User>();

    // Retrieve all users
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers(){
        System.out.println("Fetching all users");
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    // Retrieve single user
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("id") int id){
        System.out.println("Fetching user by id " + id);
        if(id < users.size()){
            return new ResponseEntity<User>(users.get(id), HttpStatus.OK);
        }
        else return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }

    // Delete single user
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") int id){
        System.out.println("Deleting user by id " + id + " if any");
        if(id < users.size()){
            users.remove(id);
            System.out.println("Deleted user by id " + id);
            return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
        }
        else return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }

    // Create new user
    @RequestMapping(value = "/create/user", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody User user)
    {
        System.out.println("Creating user " + user.getUsername());
        users.add(user);
        System.out.println("User " + user.toString() + " created");
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    // Test with urlencoded parameters
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestParam("name") String name)
    {
        System.out.println(name);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
