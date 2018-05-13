<!--nascondo gli input quando apro il menù-->


var n= 1;
function BlockCursor() {
    n*=-1;
    n=n-1000;
    var name;
    var y = new Array(column);
    for(var j = 0; j<column; j++){
        y[j] = j*62;
        name = "c"+j;
        document.getElementById(name).style.left=n+y[j]+"px";
    }
}
