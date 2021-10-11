// var dialog = document.getElementById("dialog-Edit");
//
// document.querySelector("#cancel").onclick = function () {
//     dialog.close();
// };

function collectionEdit() {
    var dialog = document.getElementById("dialog-Edit");
    dialog.showModal();
}

function  cancelEdit(){
    var dialog = document.getElementById("dialog-Edit");
    dialog.close();
}

function collectionCreate() {
    var dialogCreate = document.getElementById("dialog-Create");
    dialogCreate.showModal();
}

function cancelCreate() {
    var dialogCreate = document.getElementById("dialog-Create");
    dialogCreate.close();
}
