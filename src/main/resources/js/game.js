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

