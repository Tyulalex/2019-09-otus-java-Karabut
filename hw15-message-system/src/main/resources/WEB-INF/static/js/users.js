let stompClient = null;
const applicationPath = "/message-system";
const messageWsUsersPath = "/admin/users";
const replyQueueWsPath = "/user/queue/reply";
const replyQueueErrorWsPath = "/user/queue/errors";
window.onload = connect;

function Toggle() {
    const temp = document.getElementById("typepass");
    if (temp.type === "password") {
        temp.type = "text";
    } else {
        temp.type = "password";
    }
}

function connect() {
    var socket = new SockJS(`${applicationPath}/ws`);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`${replyQueueWsPath}`,
            function (message) {
                fillTheTable(JSON.parse(message.body));
            });
        stompClient.subscribe(`${replyQueueErrorWsPath}`, function (message) {
            alert("Error has occurred");
        });
    });
}

function createUser() {
    const body = {
        "name": document.getElementById("name").value,
        "login": document.getElementById("login").value,
        "age": document.getElementById("age").value,
        "address": {"street": document.getElementById("address").value},
        "phones": [{"number": document.getElementById("phone").value}],
        "password": document.getElementById("typepass").value
    };
    const myJSON = {
        "messageType": "CREATE_USER",
        "payload": JSON.stringify(body)
    };

    sendCommandToUsers(myJSON);
    console.log("user created");
    document.querySelector("[data-dismiss='modal']").click();
}

function loadUsersList() {
    const myJSON = {
        "messageType": "GET_LIST_OF_USERS"
    };
    sendCommandToUsers(myJSON);
    console.log("users requested");
}

function sendCommandToUsers(json) {
    stompClient.send(`${messageWsUsersPath}`, {}, JSON.stringify(json));
}

function fillTheTable(usersJson) {
    let i;
    const col = [];
    for (i = 0; i < usersJson.length; i++) {
        for (var key in usersJson[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }
    const table = document.createElement("table");
    table.setAttribute('class', 'table table-striped');
    let tr = table.insertRow(-1);

    for (i = 0; i < col.length; i++) {
        const th = document.createElement("th");
        th.innerHTML = col[i];
        tr.appendChild(th);
    }
    for (i = 0; i < usersJson.length; i++) {

        tr = table.insertRow(-1);

        for (let j = 0; j < col.length; j++) {
            const tabCell = tr.insertCell(-1);
            if (col[j] === "address") {
                tabCell.innerHTML = usersJson[i][col[j]].street;
            } else if (col[j] === "phones") {
                tabCell.innerHTML = usersJson[i][col[j]].map(e => e.number).join(",");
            } else {
                tabCell.innerHTML = usersJson[i][col[j]];
            }
        }
    }
    const divContainer = document.getElementById("showData");
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}
