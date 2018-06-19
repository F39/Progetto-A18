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
                document.location.href = "./loadingPage.html";
            },
            error: function () {
                alert("Something went wrong");
            }

        }
    );
}

function move(move) {
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
                    if (response.matchFlowState == "started1"){
                        sessionStorage.turn = 1;
                    }else if(response.matchFlowState == "started2"){
                        sessionStorage.turn = 2;
                    }
                    document.location.href = "./connect4.html";
                } else if (response.move == -1) {
                    //console.log("cambio di stato: " + response.matchFlowState);
                    if (response.matchFlowState == "winner") {

                    } else if (response.matchFlowState == "looser") {

                    } else if (response.matchFlowState == "tie") {

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