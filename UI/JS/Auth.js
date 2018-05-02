var server='http://localhost:8080';

function get_login_data(){
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
   

    /*JSON Version*/
    var data = JSON.stringify({"username": username, "password": password});
    var xhr=RESTrequest('POST', "/auth/login", data);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var json = JSON.parse(xhr.responseText);
            //console.log(json.username + ", " + json.token);
            sessionStorage.username=username;
            sessionStorage.token=json.token;          
            //console.log(sessionStorage.token);
            if(json.token != null){
                document.location.href="./Pages/NewGame.html";
            }
        }
    };
    
    
    
}

function get_signin_data(){

    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var c_password = document.getElementById("c_password").value;
    var email = document.getElementById("email").value;


    if(password == c_password ){

        /*Non mandare la password in chiaro. Definire crittografia*/
        var data = JSON.stringify({"username": username, "password": password, "email": email}); 
        var xhr = RESTrequest('POST', "/api/v1/user/create", data)
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var json = JSON.parse(xhr.responseText);
                //console.log(json.email + ", " + json.password);
                document.location.href="../Index.html";
            }
        };  

    }
    else {
        alert("Passwords don't match");
    }

}

function logout(){
    /*Release token and stop session*/
    sessionStorage.token = null;
    document.location.href = "../Index.html";
}

function check_token(){
    /*Check if the session is consistent and the client is not calling newgame manually*/
    console.log(sessionStorage.token);
    var data=JSON.stringify({"username":username, "token":token})
    /*Token sent with every request*/
    var xhr = RESTrequest('POST', "//", data);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            //var json = JSON.parse(xhr.responseText);
            //console.log(json.email + ", " + json.password);
            
        }else{
            alert("YOU SHALL NOT PASS!");
        }
    };  

}

function RESTrequest(type, endpoint, data){

    var xhr = new XMLHttpRequest();

    xhr.open(type, server + endpoint, true);
    xhr.setRequestHeader("Content-Type", "application/json");
   
    xhr.send(data);
    return xhr;
}