function closeCreateUserForm() {
    document.getElementById("myForm").style.display = "none";
}

function Toggle() {
    var temp = document.getElementById("typepass");
    if (temp.type === "password") {
        temp.type = "text";
    } else {
        temp.type = "password";
    }
}

function createUser() {

    const name = document.getElementById("name").value;
    const age = document.getElementById("age").value;
    const address = document.getElementById("address").value;
    const phone = document.getElementById("phone").value;
    const login = document.getElementById("login").value;
    const password = document.getElementById("typepass").value;

    var xhr = new XMLHttpRequest();

    var myJSON = {
        "name": name,
        "login": login,
        "age": age,
        "address": {"street": address},
        "phones": [{"number": phone}]
    };
    var body = JSON.stringify(myJSON);

    xhr.open("POST", '/hw13_dependency_injection_war_exploded/api/user', true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 201) {
            console.log("user created");
            document.querySelector("[data-dismiss='modal']").click();
            loadTableFromJSON();
        }
    }

    xhr.send(body);
}


function loadTableFromJSON() {
    fetch('/hw13_dependency_injection_war_exploded/api/users', {
        method: 'GET',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json',
        },
        redirect: 'follow',
        referrer: 'no-referrer',
    })
        .then(response => response.json())
        .then(fillTheTable);
}

function fillTheTable(usersJson) {
    var col = [];
    for (var i = 0; i < usersJson.length; i++) {
        for (var key in usersJson[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }
    var table = document.createElement("table");
    table.setAttribute('class', 'table table-striped');
    var tr = table.insertRow(-1);

    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");
        th.innerHTML = col[i];
        tr.appendChild(th);
    }
    for (var i = 0; i < usersJson.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            if (col[j] === "address") {
                tabCell.innerHTML = usersJson[i][col[j]].street;
            } else if (col[j] === "phones") {
                tabCell.innerHTML = usersJson[i][col[j]].map(e => e.number).join(",");
            } else {
                tabCell.innerHTML = usersJson[i][col[j]];
            }
        }
    }
    var divContainer = document.getElementById("showData");
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}

