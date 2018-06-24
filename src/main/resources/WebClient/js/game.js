var gameboard = [];
var gameID = 0;

var rows=6;
var columns=7;



function newGameSetup(){    
    
    for(var i=0; i<rows; i++){
        gameboard[i]=[];
        for(var j=0; j<columns; j++){
            gameboard[i][j]=0;
        }
    }

}

function loadGame(gameID){
    /*Define endpoint to load game*/
    var data = JSON.stringify({"type": "loadGame", "gameID": gameID});
    send(data);
}

function addMove(player, move){
    for(var i=rows-1; i>=0; i--){
        if(gameboard[i][move]===0){
            gameboard[i][move]=player;
            PlayerMove.cont++;
            PlayerMove.y_coordinate = i;
            PlayerMove.isallowed = "true";
            PlayerMove.x_coordinate = move;
            DrawPawn();
            changePlayer -= Math.pow(-1,changePlayer);
            if(sessionStorage.turn == changePlayer){
                document.getElementById("currentTurn").innerHTML = "Your Turn";
            }else{
                document.getElementById("currentTurn").innerHTML = "Enemy Turn";
            }
            
            return;
        }

    }

    PlayerMove.isallowed = "false";
}

