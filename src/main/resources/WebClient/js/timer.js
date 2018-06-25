var timer = sessionStorage.timer;
function startTimer(){
    document.getElementById("time").innerHTML="Time: "+timer;
    if(timer > 0){
        if(isPaused == false){
            timer = timer-1;
        }
        setTimeout(startTimer, 1000);
    }else {
        if(sessionStorage.turn == changePlayer){
            quit();
        }
    }
    
}