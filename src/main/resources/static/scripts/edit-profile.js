var dialog = document.querySelector("dialog");

// document.querySelector("#cancel").onclick = function () {
//     dialog.close();
// };


function editProfile() {
    var dialogEditPro = document.getElementById("edit-profile");
    dialogEditPro.showModal();
}

function canceleditPro() {
    var dialogEditPro = document.getElementById("edit-profile");
    dialogEditPro.close();
}


function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $(".image-upload-wrap").hide();

            $(".file-upload-image").attr("src", e.target.result);
            $(".file-upload-content").show();

            $(".image-title").html(input.files[0].name);
        };

        reader.readAsDataURL(input.files[0]);
    } else {
        removeUpload();
    }
}

function removeUpload() {
    $(".file-upload-input").replaceWith($(".file-upload-input").clone());
    $(".file-upload-content").hide();
    $(".image-upload-wrap").show();
}
const validatePassword = () => {
    const password = document.getElementById('new-password' ).value;
    const again_password = document.getElementById('again-password').value;
    console.log(password);
    console.log(again_password)
    if (password === again_password) {
        document.getElementById("btn-change-password" ).disabled = false;
        document.getElementById(`message`).innerHTML = "";
    } else {

        document.getElementById("btn-change-password" ).disabled = true;
        document.getElementById(`message`).innerHTML = "Password Not Match";
    }
}
