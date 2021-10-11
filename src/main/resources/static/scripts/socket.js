let listQuestion = []
let stompClient = null;
let code = null;
let isHost = false;
let idQuiz = null;
let idGame = null;
let totalPlayers = 0;
let questionField = null;
let listClient = []
let endPlayers = 0;
let client = {
    idPlayer: -1,
    namePlayer: "host",
    point: 0,
    question: {
        indexQues: 0,
        idQues: 0
    },
    end: false,
}
const connect = () => {
    let socket = new SockJS('/quiz/play-quiz');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/play/public-room/${idGame}`, function (player) {
            onMessageReceived(player)
        });
        if (!isHost) {
            client = {
                idPlayer: $('#id-player').val(),
                namePlayer: $('#name-player').val(),
                point: 0,
                question: {
                    indexQues: 0,
                    idQues: 0
                },
                end: false,
            }
            listClient.push(client);
            sendPlayer();
        }
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
        sendLeave();
    }
    console.log("Disconnected");
}
const sendLeave = () => {
    stompClient.send(`/web/send-leave/${idGame}`, {},
        JSON.stringify(
            {sender: {
                    name: client.namePlayer,
                    id: client.idPlayer,
                    point: client.point,
                },
                type: 'LEAVE'}));
}

const sendPlayer = () => {
    stompClient.send(`/web/add-player/${idGame}`, {},
        JSON.stringify(
        {sender: {
                    name: client.namePlayer,
                    id: client.idPlayer,
                    point: client.point,
                },
            type: 'JOIN'}));
}

const sendStart = () => {
    stompClient.send(`/web/send-start/${idGame}`, {},
        JSON.stringify(
            {sender: {
                    name: "host",
                    id: 0,
                },
                content: idQuiz,
                type: 'START'}));
}

const sendAnswer = () => {
    stompClient.send(`/web/send-answer/${idGame}`, {},
        JSON.stringify(
            {sender: {
                    name: client.namePlayer,
                    id: client.idPlayer,
                    point: client.point,
                },
                content: idQuiz,
                type: 'ANSWER'}));
}

const sendEnd = () => {
    stompClient.send(`/web/send-end/${idGame}`, {},
        JSON.stringify(
            {sender: {
                    name: client.namePlayer,
                    id: client.idPlayer,
                    point: client.point,
                },
                content: idQuiz,
                type: 'END'}));
    client.end = true;
}

const onMessageReceived = (player) => {
    const message = JSON.parse(player.body);
    // console.log(message)
    // console.log(`idGame: ${idGame}`)
    // console.log("Name: " + client.namePlayer)
    // console.log("Id: " + client.idPlayer)
    switch (message.type) {
        case 'JOIN':
            totalPlayers = parseInt($('#total').val());
            if (isHost || message.sender.id !== client.idPlayer) {
                totalPlayers++;
                $('#total').val(totalPlayers)
                let newPlayer = `<div id="${message.sender.id}" class=\"card\" style=\"background-color: black;\">\n
                        <img src=\"https://res.cloudinary.com/leehari/image/upload/v1631856576/z8k64on2f76wtllsdaml.png\" style=\"width: 15%;margin-left: auto;margin-right: auto;\" class=\"img-fluid mt-3\">\n
                                    <label>${message.sender.name}</label>\n
                            </div>`
                $(`#${idGame}`).append(newPlayer);
            }
            $('#total-players').html(`<i class=\"fa fa-users\" aria-hidden=\"true\"></i> ${totalPlayers}`)
            console.log("Name: " + client.namePlayer)
            console.log("Id: " + client.idPlayer)
            break;
        case 'LEAVE':
            $(`#${message.sender.id}`).remove();
            totalPlayers--;
            $('#total-players').html(`<i class=\"fa fa-users\" aria-hidden=\"true\"></i> ${totalPlayers}`)
            $('#total').val(totalPlayers)
            break;
        case 'START':
            endPlayers = 0;
            listQuestion = message.content;
            console.log(listQuestion)
            if (isHost) {
                getLeaderboard()
            } else  {
                onReceivedStartMessage()
            }
            break;
        case 'ANSWER':
            if (client.idPlayer === message.sender.id) {
                if (client.question.indexQues < listQuestion.length) {
                    getQuestion();
                } else {
                    getLeaderboard();
                    sendEnd();
                }
            }
            if (isHost) {
                getLeaderboard();
            }
            break;
        case 'END':
            if (client.end) {
                getLeaderboard();
            }
            endPlayers++;
            break;
        default:
            break;
    }
}

const onReceivedStartMessage = () => {
    console.log("quiz: " + idQuiz)
    $.ajax({
        type: "GET",
        processData: false,
        contentType: false,
        url: `/quiz/socket/get-play/join`,
        data: "",
        dataType: "text",
        cache: false,
        timeout: 600000,
        success: function (response) {
            processAjaxData(response, "")
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const getLeaderboard = () => {
    $.ajax({
        type: "GET",
        processData: false,
        contentType: false,
        url: `/quiz/socket/get-leaderboard/${idGame}`,
        data: "",
        dataType: "text",
        cache: false,
        timeout: 600000,
        success: function (response) {
            processAjaxData(response, "")
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const processAjaxData = (response, urlPath) => {
    const body = $('#content')
    body.html(response);
    document.title = $(response).filter('title').text()
    window.history.pushState({"html":response,"pageTitle":document.title},"");
}

const countDownStart = () => {
    let time = 5;
    const countDown = $('#count-down');
    const modal = $('#modal')
    countDown.show();
    modal.show();
    const a = () => {
        if (time <= 0) {
            clearInterval(x);
            countDown.hide();
            modal.hide();
            getQuestion();
        } else {
            countDown.html(time)
            time--;
        }
    };
    const x = setInterval(a, 1000);
}

const getQuestion = () => {
    client.question.idQues = listQuestion[client.question.indexQues++];
    $.ajax({
        type: "GET",
        processData: false,
        contentType: false,
        url: `/quiz/socket/get-question/${idQuiz}/${client.question.idQues}/${client.idPlayer}`,
        data: "",
        dataType: "text",
        cache: false,
        timeout: 600000,
        success: function (response) {
            questionField.html(response);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const removeClient = (clients, value) => {
    return clients.filter(function(ele){
        return ele.idPlayer !== value;
    });
}