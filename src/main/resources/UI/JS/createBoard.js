var boardArray = new Array(column);
var indexOccupied = new Array(column);

<!--create board-->
for (var i = 0; i < column; i++) {
    document.write('<div class="column" id="col' + i + '" >');
    for (var j = 0; j < row; j++) {
        document.write('<div class="cell" id="cell' + j + i + '">');
        document.write('<div class="backElem"></div>');
        document.write('<div class="elem"></div>');
        document.write('<div class="frontElem"></div>');
        document.write('</div>');
    }

    document.write('</div>');

    var backElemWidth = column * 61;
    var backElemHeight = row * 61;
    document.getElementsByClassName("backElem")[0].style.visibility = "hidden";
    document.getElementsByClassName("backElem")[0].style.width = backElemWidth + "px";
    document.getElementsByClassName("backElem")[0].style.height = backElemHeight + "px";

}

document.write('<img id="nextRound" src="../Img/red.png" height="45" width="45">');

function showim() {
    document.getElementById("nextRound").style.visibility = "hidden";
}