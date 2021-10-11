const initOverview = () => {
    const gradeOverview = $('#grade-overview')
    const grades = gradeOverview.text().split('/')
    if (grades[0] === grades[1]) {
        gradeOverview.html(`<i class="fa fa-graduation-cap" aria-hidden="true"></i> ${grades[0]} grade `)
    } else {
        gradeOverview.html(`<i class="fa fa-graduation-cap" aria-hidden="true"></i> ${grades[0]} - ${grades[1]} grade`)
    }
    const subjectOverview = $('#subject-overview')
    const subject = subjectOverview.text()
    subjectOverview.html(`<i class="fa fa-book" aria-hidden="true"></i> ${subject}`)
}

const handleOutputImage = () => {
    // console.log(!($("#output-quiz").attr('src') === ""))
    const labelOutputQuiz = $("#label-output-quiz")
    if (!($("#output-quiz").attr('src') === "")) {
        labelOutputQuiz.show()
    } else {
        labelOutputQuiz.hide()
    }
}

const rearrangeQuestionSection = (orderDelete, totalSectionBeforeDelete) => {
    $(`#section-${orderDelete}`).remove();
    if (orderDelete < totalSectionBeforeDelete) {
        for (let i = orderDelete; i <= totalSectionBeforeDelete; i++) {
            let newOrder = i - 1;
            const questionTitleSelector = $(`#question-title-${i}`);
            questionTitleSelector.text(`Question ${i}`);
            questionTitleSelector.attr('id', 'question-title-' + newOrder);
            const idBtnEdit =  $(`button[data-edit='${i}']`)[0].id;
            const idBtnDelete =  $(`button[data-delete='${i}']`)[0].id;
            $(`#${idBtnEdit}`).attr('data-edit', newOrder)
            $(`#${idBtnDelete}`).attr('data-delete', newOrder)
        }
    }
}

const btnDeleteAnswerOnclickHandler = (idBtn) => {
    const idDelete = parseInt(idBtn[idBtn.length - 1])
    const numberAnswer = document.getElementsByName("answer").length;
    $(`#${idBtn}`).parent().parent().remove();
    if (idDelete < numberAnswer) {
        for (let i = idDelete + 1; i <= numberAnswer; i++) {
            let newId = i - 1;
            $(`#answer-checkbox-${i}`).attr('id', 'answer-checkbox-' + newId);
            let input = $(`#answer-content-${i}`)
            input.attr('name', 'answer-content-' + newId);
            input.attr('id', 'answer-content-' + newId);
            $(`#btn-delete-${i}`).attr('id', 'btn-delete-' + newId);
        }
    }
}

const btnDeleteQuestionOnclickHandler = (idBtn) => {
    console.log("delete")
    const selector = $(`#${idBtn}`)
    const idQuiz = parseInt($("#id-quiz").val())
    const idQues = parseInt(selector.data('id'));
    const order = parseInt(selector.data('delete'));
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/quiz/api/delete-question/" + idQuiz + "/" + idQues,
        data: "",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (response) {
            console.log(response)
            if (response.success) {
                console.log("SUCCESS : ", response);
                const numberQuestionSelector = $("#number-questions")
                const totalQuestionBeforeDelete = numberQuestionSelector.val()
                numberQuestionSelector.val(parseInt(response.data))
                rearrangeQuestionSection(order, totalQuestionBeforeDelete)
            }
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const btnEditQuestionOnclickHandler = (idBtn) => {
    console.log("edit")
    const selector = $(`#${idBtn}`)
    const idQuiz = parseInt($("#id-quiz").val())
    const idQues = parseInt(selector.data('id'));
    $("#order-question").val(selector.data('edit'))
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/quiz/api/get-question/" + idQuiz + "/" + idQues,
        data: "",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (response) {
            console.log(response)
            if (response.success) {
                // console.log("SUCCESS : ", response);
                const question = response.data.question
                const answers = response.data.answers
                // console.log(question)
                $(`#question-content`).val(question.content)
                $(`#duration`).val(question.duration)
                // console.log(answers)
                const answerAmount = answers.length
                for (let i = answerAmount; i > 2; i--) {
                    addAnswer();
                }
                for (let i = 1; i <= answerAmount; i++) {
                    $(`#answer-content-${i}`).val(answers[i - 1].content)
                    if (answers[i - 1].isCorrect) {
                        $(`#answer-checkbox-${i}`).prop("checked", true);
                    }
                }
                showQuestionEditor(true, idQues);
            }
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const selectDurationOnChangeHandler = () => {
    const element = $('select')
    element.off('change');
    element.on('change', function () {
        const ele = $(this)
        const idQues = ele.data('id')
        if (idQues) {
            const idQuiz = parseInt($("#id-quiz").val())
            console.log(ele.val())
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/quiz/api/update-duration-question/" + idQuiz + "/" + idQues,
                data: JSON.stringify(ele.val()),
                dataType: 'json',
                cache: false,
                timeout: 600000,
                success: function (response) {
                    if (response.success) {
                        console.log("SUCCESS", response)
                    }
                },
                error: function (e) {
                    console.log("ERROR : ", e);
                }
            });
        }
    })
}

const OnclickHandler = function(element, type) {
    this.element = element || {};
    this.element.off('click');
    this.element.on('click', function () {
        const idBtn = this.id
        switch (type) {
            case 'btn-delete-answer':
                btnDeleteAnswerOnclickHandler(idBtn);
                break;
            case 'btn-delete-question':
                btnDeleteQuestionOnclickHandler(idBtn);
                break;
            case 'btn-edit-question':
                btnEditQuestionOnclickHandler(idBtn);
                break;
            default:
                break;
        }
    })
}

const showQuestionEditor = (isUpdate, idQues = -1) => {
    $("#id-question").val(idQues)
    document.getElementById("question-editor").style.display = "block";
    $("#is-update-question").val(isUpdate)
    console.log(idQues)
}

const closeQuestionEditor = () => {
    document.getElementById("form-editor-question").reset();
    $("#id-question").val(-1)
    $('.option-added').remove();
    document.getElementById("question-editor").style.display = 'none';
}

const addAnswer = () => {
    const idAnswer = document.getElementsByName("is-correct").length + 1;
    if (idAnswer === 6) {
        $('#alert-max-answer').show()
    } else {
        const insertElement = "<div class=\"row mt-3 answer option-added\">\n" +
            "                                            <div class=\"col-sm-1 text-center pt-3 pb-3\">\n" +
            "                                                <label class=\"container-selector\">\n" +
            "                                                    <input type=\"radio\" name=\"is-correct\" id=\"answer-checkbox-"+ idAnswer +"\">\n" +
            "                                                    <span class=\"checkmark\"></span>\n" +
            "                                                </label>\n" +
            "                                            </div>\n" +
            "                                            <div class=\"col-sm-10 pt-3 pb-3\">\n" +
            "                                                <input required type=\"text\" class=\"form-control pt-4 pb-4\" name=\"answer-content-"+ idAnswer +"\" id=\"answer-content-"+ idAnswer +"\" placeholder=\"Your answer here\">\n" +
            "                                            </div>\n" +
            "                                            <div class=\"col-sm-1 pt-4 pb-4\">\n" +
            "                                                <button type=\"button\" class=\"btn-delete\" id=\"btn-delete-"+ idAnswer +"\" style=\"border: none\"><i class=\"bi bi-trash\"></i></button>\n" +
            "                                            </div>\n" +
            "                                        </div>";
        $("#add-answer").before(insertElement);
    }
}

const editQuestion = (data, idQuiz, idQues, formData) => {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/quiz/api/update-question?idQuiz=" + idQuiz + "&idQues=" + idQues,
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (response) {
            if (response.success) {
                console.log("SUCCESS : ", response);
                saveImageQuestion(idQuiz, idQues, formData, $("#order-question").val(), true)
            }
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const addQuestion = (data, idQuiz, formData) => {
    console.log(data)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/quiz/api/add-question?id=" + idQuiz,
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (response) {
            if (response.success) {
                console.log("SUCCESS : ", response);
                const responseData = JSON.parse(response.data);
                console.log(responseData)
                $("#number-questions").val(responseData.questionAmount);
                saveImageQuestion(idQuiz, responseData.idQues, formData, responseData.questionAmount, false)
            }
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const saveImageQuestion = (idQuiz, idQues, image, order, isUpdate) => {
    $.ajax({
        type: "POST",
        url: "/quiz/api/upload-image-question?idQuiz=" + idQuiz +"&idQues=" +idQues,
        data: image,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (response) {
            console.log("SUCCESS : ", response);
            appendQuestion(idQuiz, idQues, order, isUpdate);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const saveQuestion = (formData) => {
    const idQuiz = parseInt($("#id-quiz").val())
    const numberQuestion = parseInt($("#number-questions").val())
    const idQues = parseInt($("#id-question").val())
    console.log(idQuiz)
    console.log(numberQuestion)
    console.log(idQues)
    const question = {
        primaryKey: {
            id: idQues,
            idQuiz: idQuiz,
        },
        content: $("#question-content").val(),
        duration: $("#duration").val(),
        file: "",
        type: $(`#type-question`).val(),
    }
    const answerAmount = document.getElementsByName("is-correct").length;
    let answers = []
    for (let i = 1; i <= answerAmount; i++) {
        answers.push({
            primaryKey: {
                id: i,
                questionPrimaryKey: {
                    id: idQues,
                    idQuiz: idQuiz,
                }
            },
            content: $(`#answer-content-${i}`).val(),
            isCorrect: $(`#answer-checkbox-${i}`).is(":checked")
        })
    }
    const data = {
        question: question,
        answers: answers,
    }
    // console.log(data)
    const isUpdate = $("#is-update-question").val()
    // console.log(typeof isUpdate)
    if (isUpdate !== "false") {
        editQuestion(data, idQuiz, idQues, formData)
    } else {
        addQuestion(data, idQuiz, formData)
    }
}

const appendQuestion = (idQuiz, idQues, order, isUpdate) => {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/quiz/api/get-saved-view/" + idQuiz + "/" + idQues + "/" + order,
        data: "",
        dataType: 'text',
        cache: false,
        timeout: 600000,
        success: function (data) {
            const div = document.createElement("div")
            div.className = "card w-100 mb-3";
            div.style.backgroundColor = "#E7E7E7";
            $(div).html(data)
            if (isUpdate) {
                $(`#section-${order}`).replaceWith(div)
            } else {
                document.getElementById("question-section").appendChild(div);
            }
            div.id = `section-${order}`
            closeQuestionEditor();
            new OnclickHandler($('.btn-delete-question'), 'btn-delete-question');
            new OnclickHandler($('.btn-edit-question'), 'btn-edit-question');
            selectDurationOnChangeHandler();
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

const publishQuiz = (idQuiz) => {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: `/quiz/api/publish-quiz?idQuiz=${idQuiz}`,
        data: "",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (response) {
            console.log(response)
            if (response.success) {
                location.assign("/quiz/home");
            }
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}