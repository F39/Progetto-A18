var restServer = "http://localhost:8080/rest";

function login(){
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
   
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/user/login',
            dataType: "json",
            data: JSON.stringify({"username": username, "password": password}),
            success: function(response){
                sessionStorage.username=username;
                console.log(response.token);
                sessionStorage.token=response.token;          
                document.location.href="./newgame.html";
            },
            error: function(){
                alert("Check username and password. Did you confirm your email?");
            }
             
        }
    );
}

function signup(){
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var cPassword = document.getElementById("c_password").value;
    var email = document.getElementById("email").value;

    if(password == cPassword ){
        $.ajax(
            {
                type: 'POST',
                contentType: 'application/json',
                url: restServer + '/user/signup',
                data: JSON.stringify({"username": username, "email": email, "password": password}),
                success: function(){
                    alert("Confirmation email sent");
                    document.location.href="./index.html";
                    
                },
                error: function(){
                    alert("User already exist");
                }
            }
        );
    }
    else {
        alert("Passwords don't match");
    }
}

function logout(){
    $.ajax(
        {
            type: 'POST',
            contentType: 'application/json',
            url: restServer + '/user/logout',
            data: JSON.stringify({"username": sessionStorage.username, "token": sessionStorage.token}),
            success: function(){
                document.location.href="./index.html";
                
            },
            error: function(){
                alert("You are not logged in");
                document.location.href="./index.html";
            }
        }
    );
}
