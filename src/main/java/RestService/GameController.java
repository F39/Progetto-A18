//package RestService;
//
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@CrossOrigin(value = "*")
//@RequestMapping(path="/api/v1/game")
//public class GameController {
//
//    // Crete new game request
//    @PostMapping(path="/create")
//    public @ResponseBody String createNewGameRequest(@RequestBody GameRequest req){
//        // add req to matching queue
//        return req.getId();
//    }
//
//    // Crete new game request
//    @GetMapping(path="/create/{req_id}")
//    public @ResponseBody String waitingForMatch(@PathVariable("req_id") long req_id){
//        // wait matching thread
//        // create new game and respond
//        // if timeout is over, continue matching but cache the result instead to return it
//        Game newGame = new Game();
//        return newGame.getId();
//    }
//
//    // Create new game
//    public Game newGame createNewGame(){
//        return;
//    }
//
//    // Save game
//    @PostMapping(path="/{game_id}/save")
//    public @ResponseBody String saveGame(@PathVariable("game_id") long game_id){
//
//    }
//
//    // Join a new game after a match is find
//    @PostMapping(path="/{game_id}/join")
//    public @ResponseBody String joinNewGame(@PathVariable("game_id") long game_id){
//        // register the user to the game
//    }
//
//}
