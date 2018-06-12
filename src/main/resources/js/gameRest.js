var restServer = "http://localhost:8080/rest";

function newGame(mode){
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/newgame',
            headers:{
                "token" : sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "mode": mode}),
            success: function(){
                alert("success");
            },
            error: function(){
                alert("Something went wrong");
            }
             
        }
    );
}

function move(move){
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/move',
            headers:{
                "token" : sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "gameId": sessionStorage.gameId, "move": move}),
            success: function(response){
                
            },
            error: function(){
                alert("Something went wrong");
            }
             
        }
    );
}

function pause(){
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/pause',
            headers:{
                "token" : sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "gameId": sessionStorage.gameId}),
            success: function(response){
                
            },
            error: function(){
                alert("Something went wrong");
            }
             
        }
    );
}

function quit(){
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/quit',
            headers:{
                "token" : sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "gameId": sessionStorage.gameId}),
            success: function(response){
               
            },
            error: function(){
                alert("Something went wrong");
            }
             
        }
    );
}

poll();
function poll(){
    $.ajax(
        {
            type: 'POST',
            contentType: 'text/plain',
            url: restServer + '/game/poll',
            headers:{
                "token" : sessionStorage.token
            },
            data: sessionStorage.username,
            success: function(response){
                //console.log(response);
                if(response.move == -2 && response.gameId == 0){
                    //poll response
                    console.log("poll: " + response.matchFlowState);
                }else if(response.move == -2 && response.gameId != 0) {
                    //game found
                    sessionStorage.gameId = response.gameId;
                    document.location.href = "./connect4.html";
                }else if(response.move==-1){
                    console.log("cambio di stato: " + response.matchFlowState);
                }else{
                    console.log("mossa");
                }
            },
            error: function(){
                alert("Something went wrong");
                document.location.href="./index.html";
            }
             
        }
    );
    setTimeout(poll, 5000);
}