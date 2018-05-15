<!--calcola la posizione dove far cadere la pallina e aggiorna il contatore mosse-->
var n_slot;


function lastpos(num) {
    counter++;
    document.getElementById("immagine").style.visibility="hidden";
    var calculatedpos = -270+indexOccupied[num]*0.1;
    var n_cell = "d"+indexOccupied[num]+num;
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

    <!--vettore mosse Red-->
    <!--vettore mosse Blue-->

}

<!--hover image-->
function show(pos) {
    var posizione = pos+"px";
    document.getElementById("immagine").style.visibility="visible";
    document.getElementById("immagine").style.left=posizione;
    document.getElementById("immagine").style.top="-70px";
}

