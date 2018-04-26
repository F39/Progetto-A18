package RestService;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(path="/api/v1/game")
public class GameController {

    // Crete new game
    @PostMapping(path="/create")
    public @ResponseBody Game createNewGame(){
        return "Miao";
    }

    // Save game
    @PostMapping(path="/save")
    public @ResponseBody Game saveGame(){
        return "Bau";
    }

    // Join a new game
    @GetMapping(path="/{game_id}/join")
    public @ResponseBody Game joinNewGame(@PathVariable("game_id") long game_id){
        return "Grrrr";
    }

}
