var in_num = (row*column) - Math.floor(0.70*(row*column));
var fin_num = Math.floor(0.6*in_num);
var casualNumber = Math.floor((Math.random()*fin_num)+(in_num));

function change1() {

    document.getElementById("test").innerHTML = in_num +'-'+fin_num+'--'+casualNumber;
    if(counter===casualNumber)
        setTimeout("hideMove()", 400);
    if(counter===casualNumber+4)
        document.getElementsByClassName("top")[0].style.visibility="hidden";

}

function hideMove() {
    document.getElementsByClassName("top")[0].style.visibility = "visible";

}