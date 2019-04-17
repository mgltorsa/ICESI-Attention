checkbox = document.getElementById("forwarded-checkbox");
div = document.getElementById("forwarded-place");

if (!checkbox.checked) {
    div.style.visibility = "hidden";
}
realCheckbox = document.getElementById("boolean-value");

checkbox.addEventListener("click", function() {
    visibility = "hidden";
    if (checkbox.checked) {
        visibility = "visible";

    }

    realCheckbox.value = checkbox.checked;


    div.style.visibility = visibility;
});