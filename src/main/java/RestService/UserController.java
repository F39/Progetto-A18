package RestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Retrieve all users
    @GetMapping(path="/user")
    public @ResponseBody Iterable<User> listAllUsers(){
        System.out.println("Fetching all users");
        return userRepository.findAll();
    }

    // Retrieve single user
    @GetMapping(path="/user/{id}")
    public @ResponseBody User getUserByID(@PathVariable("id") long id){
        System.out.println("Fetching user by id " + id);
        if(userRepository.existsById(id)){
            //return userRepository.findById(id);
        }
        // return http header code not found
        return null;
    }

    // Create new user
    @PostMapping(path="/user/create")
    public @ResponseBody String createNewUser(@RequestBody User user)
    {
        System.out.println("Creating user " + user.getUsername());
        userRepository.save(user);
        // generate auth token
        System.out.println("User " + user.toString() + " created");
        return "Saved";
        // return token;
    }

}
