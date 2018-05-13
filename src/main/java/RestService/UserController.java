package RestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(path="/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Retrieve all users
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> listAllUsers(){
        System.out.println("Fetching all users");
        return userRepository.findAll();
    }

    // Retrieve single user
    @GetMapping(path="/{id}")
    public @ResponseBody User getUserByID(@PathVariable("id") long id){
        System.out.println("Fetching user by id " + id);
        if(userRepository.existsById(id)){
            //return userRepository.findById(id);
        }
        // return http header code not found
        return null;
    }

    // Create new user
    @PostMapping(path="/create")
    public @ResponseBody String createNewUser(@RequestBody User user)
    {
        System.out.println("Creating user " + user.getUsername());
        userRepository.save(user);
        // generate auth token
        System.out.println("User " + user.toString() + " created");
        return "Saved";
        // return token;
    }

    // User login
    @PostMapping(path="/login")
    public @ResponseBody String userLogin(@RequestBody User user)
    {
        // create user session
        return "Yoh";
    }

    // User logout
    @PostMapping(path="/logout")
    public @ResponseBody String userLogout(@RequestBody User user)
    {
        // destroy user session
        return "Yoh";
    }

}
