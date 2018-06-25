var restServer = "http://localhost:8080/rest";

function getUserStats(){
    $.ajax(
        {
            type: 'GET',
            url: restServer + '/stats/' + sessionStorage.userId,
            headers: {
                "token": sessionStorage.token
            },
            success: function (response) {
                document.getElementById("player_name").innerHTML = sessionStorage.username;
                document.getElementById("player_games").innerHTML = response.games;
                document.getElementById("player_win").innerHTML = response.wins;
                document.getElementById("player_lose").innerHTML = response.defeats;
                document.getElementById("player_draws").innerHTML = response.ties;
                document.getElementById("player_points").innerHTML = response.points;
            },
            error: function () {
                alert("Something went wrong");
            }

        }
    );
}