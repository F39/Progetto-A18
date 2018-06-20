//<!--calcola la posizione dove far cadere la pallina e aggiorna il contatore mosse-->
var n_slot;

// <!--caduta e hover delle pedine-->

function lastpos(num) {
    counter++;
    document.getElementById("nextRound").style.visibility="hidden";
    var calculatedpos = -270+indexOccupied[num]*0.1;
    var n_cell = "pawn"+indexOccupied[num]+num;
    n_slot = "slot"+indexOccupied[num]+num;
    var n_colonna = "c"+num;
    cont*=-1;
    if(cont===1){
        document.getElementById(n_cell).src="../Img/blue.png";
    }
    document.getElementById(n_cell).style.top=calculatedpos+"px";
    if(indexOccupied[num]>0)
        indexOccupied[num]--;
    else
        document.getElementById(n_colonna).style.visibility="hidden";
    document.getElementById("show").innerHTML="colonna"+num+"click"+cont+"next free"+(indexOccupied);
    document.getElementById("counter").innerHTML = counter;
    document.getElementById(n_slot).click();
   // setTimeout("hideSkill()", 700);
    change1();

    //<!--vettore mosse Red-->
    //<!--vettore mosse Blue-->

}

//<!--hover image-->
function show(pos) {
    var posizione = pos+"px";
    if(cont===-1)
        document.getElementById("nextRound").src = "../Img/blue.png";
    else
        document.getElementById("nextRound").src = "../Img/red.png"
    document.getElementById("nextRound").style.visibility="visible";
    document.getElementById("nextRound").style.left=posizione;
    document.getElementById("nextRound").style.top="-60px";
}

