let idQuiz = 0;
let listQuestion = [];
let idPlayer = null;
let idGame = 0;
let point = 0;
let idQues = 0;
let order = 0;
let correct = 0;

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
            getQuestionPractice();
        } else {
            countDown.html(time)
            time--;
        }
    };
    const x = setInterval(a, 1000);
}

const getQuestionPractice = () => {
    idQues = listQuestion[order++]
    $.ajax({
        type: "GET",
        processData: false,
        contentType: false,
        url: `/quiz/practice/get-question/${idQuiz}/${idQues}`,
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

const savePoint = () => {
    $.ajax({
        type: "POST",
        processData: false,
        contentType: "application/json",
        url: `/quiz/practice/save-point/${idPlayer}/${point}`,
        data: "",
        dataType: "json",
        cache: false,
        timeout: 600000,
        success: function (response) {
            console.log(response);
            // console.log(point)
            if (response.success) {
                if (order === listQuestion.length) {
                    getResult();
                } else {
                    getQuestionPractice();
                }
            }
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const getResult = () => {
    const percent = Math.floor(100*correct/listQuestion.length)
    // console.log(percent)
    $.ajax({
        type: "GET",
        processData: false,
        contentType: false,
        url: `/quiz/practice/get-result/${idPlayer}/${percent}`,
        data: "",
        dataType: "text",
        cache: false,
        timeout: 600000,
        success: function (response) {
            // console.log(response);
            processAjaxData(response);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const processAjaxData = (response) => {
    const body = $('#content')
    body.html(response);
    document.title = $(response).filter('title').text()
    window.history.pushState({"html":response,"pageTitle":document.title},"");
}