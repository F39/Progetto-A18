var in_num = (row*column) - Math.floor(0.70*(row*column));
var fin_num = Math.floor(0.6*in_num);
var casualNumber = Math.floor((Math.random()*fin_num)+(in_num));

    <!--nascondo la board dopo tot mosse-->

function change1() {

    document.getElementById("test").innerHTML = in_num +'-'+fin_num+'--'+casualNumber;
    if(counter===casualNumber)
        setTimeout("hideMove()", 400);
    if(counter===casualNumber+4)
        document.getElementsByClassName("backElem")[0].style.visibility="hidden";

}

function hideMove() {
    document.getElementsByClassName("backElem")[0].style.visibility = "visible";

}

