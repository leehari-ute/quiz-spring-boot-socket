const nextStep = (step = 1) => {
    const div1 = $("#1");
    const div2 = $("#2");
    const div3 = $("#3");
    switch (step) {
        case 1:
            div1.show();
            div2.hide();
            div3.hide();
            break;
        case 2:
            div1.hide();
            div2.show();
            div3.hide();
            break;
        case 3:
            div1.hide();
            div2.hide();
            div3.show();
            break;
        default:
            break;
    }
}
const setAuthType = (type = "local") => {
    const inputAuthType =  $("#authType");
    inputAuthType.val(type);
}
const isExistUsername = () => {
    const users = JSON.parse(document.getElementById("users").value);
    const username = document.getElementById("username");
    // console.log(users)
    if (users.includes(username.value.toLowerCase())) {
        // console.log(users.indexOf(username.value))
        document.getElementById(`message1`).innerHTML = "Account already exists, please login";
    } else {
        checkRequired(2, ['username']);
    }
}
const checkRequired = (step, id = []) => {
    console.log(id)
    for (let i = 0; i < id.length; i++) {
        console.log(id[i])
        let inpObj = document.getElementById(id[i]);
        if (!inpObj.checkValidity()) {
            document.getElementById(`message${step - 1}`).innerHTML = inpObj.validationMessage;
            return;
        }
    }
    nextStep(step);
}
