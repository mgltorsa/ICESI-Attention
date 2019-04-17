forms = document.querySelectorAll("form");


for (let i = 0; i < forms.length; i++) {
    var form = forms[i];
    console.log(form);
    addClickListener(form);

    console.log("power")
}

function addClickListener(form) {

    form.addEventListener("click", function() {

        form.submit();

    });
}