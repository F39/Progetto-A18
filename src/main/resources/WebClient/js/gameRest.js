var restServer = "http://localhost:8080/rest";

function newGame(mode) {
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/newgame',
            headers: {
                "token": sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "mode": mode}),
            success: function () {
                if(mode == 0){
                    sessionStorage.timer = 60;
                }else if(mode == 4){
                    sessionStorage.timer = 10;
                }
                 
                document.location.href = "./loadingPage.html";
            },
            error: function () {
                alert("Something went wrong");
            }

        }
    );
}

function newGameWithFriend(mode){
    //var opponentUsername = document.getElementById("opponentUsername").value;
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/newgame',
            headers: {
                "token": sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "mode": mode, "opponentUsername": opponentUsername}),
            success: function () {
                if(mode == 0){
                    sessionStorage.timer = 60;
                }else if(mode == 4){
                    sessionStorage.timer = 10;
                }
                 
                document.location.href = "./loadingPage.html";
            },
            error: function () {
                alert("Something went wrong");
            }

        }
    );
}

function accept(){
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/newgame',
            headers: {
                "token": sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "opponentUsername": opponentUsername}),
            success: function () {
                if(sessionStorage.mode == 0){
                    sessionStorage.timer = 60;
                }else if(sessionStorage.mode == 4){
                    sessionStorage.timer = 10;
                }
                 
                document.location.href = "./loadingPage.html";
            },
            error: function () {
                alert("Something went wrong");
            }

        }
    );
}

function move(move) {
    if(move < 0){
        move = 0;
    }
    if(move > 6){
        move = 6;
    }
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/move',
            headers: {
                "token": sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "gameId": sessionStorage.gameId, "move": move}),
            success: function (response) {

            },
            error: function () {
                alert("Something went wrong");
            }

        }
    );
}

function pause() {
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/pause',
            headers: {
                "token": sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "gameId": sessionStorage.gameId}),
            success: function (response) {
                //alert("Paused");
            },
            error: function () {
                alert("Something went wrong");
            }

        }
    );
}

function quit() {
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/game/quit',
            headers: {
                "token": sessionStorage.token
            },
            data: JSON.stringify({"username": sessionStorage.username, "gameId": sessionStorage.gameId}),
            success: function (response) {

            },
            error: function () {
                alert("Something went wrong");
            }

        }
    );
}

poll();

function poll() {
    $.ajax(
        {
            type: 'POST',
            contentType: 'text/plain',
            url: restServer + '/game/poll',
            headers: {
                "token": sessionStorage.token
            },
            data: sessionStorage.username,
            success: function (response) {
                //console.log(response);
                if (response.move == -2 && response.gameId == 0) {
                    //poll response
                    //console.log("poll: " + response.matchFlowState);
                } else if (response.move == -2 && response.gameId != 0) {
                    //game found
                    sessionStorage.gameId = response.gameId;
                    if(response.mode == "MultiPlayer"){
                        sessionStorage.mode = 0;
                    }else if(response.mode == "MultiPlayerTurbo"){
                        sessionStorage.mode = 4;                    ;
                    }else if (response.matchFlowState == "started1"){
                        sessionStorage.turn = 1;
                        sessionStorage.noRefresh = 0;
                    document.location.href = "./connect4.html";
                    }else if(response.matchFlowState == "started2"){
                        sessionStorage.turn = 2;
                        sessionStorage.noRefresh = 0;
                    document.location.href = "./connect4.html";
                    }
                    
                    
                    
                } else if (response.move == -1) {
                    //console.log("cambio di stato: " + response.matchFlowState);
                    if (response.matchFlowState == "winner") {
                        endOfMatch("You Win!")
                    } else if (response.matchFlowState == "looser") {
                        endOfMatch("You Lose!")
                    } else if (response.matchFlowState == "tie") {
                        endOfMatch("Tie!")
                    } else if (response.matchFlowState == "paused"){
                        isPause();
                    } else if (response.matchFlowState == "resumed"){
                        isPause();
                    } 
                } else {
                    //console.log("mossa");
                    var enemyTurn = sessionStorage.turn - Math.pow(-1,sessionStorage.turn);
                    addMove(enemyTurn, response.move);
                }
            },
            error: function () {
                alert("Something went wrong");
                document.location.href = "./index.html";
            }

        }
    );
    setTimeout(poll, 1000);
}