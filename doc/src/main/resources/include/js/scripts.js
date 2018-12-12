const pattern = /[^a-z\d]+/g;

$(document).ready(function () {
    const query = new URLSearchParams(window.location.search);
    const field = $("#search");

    $(".js").css("display", "inline");

    field.on("input", function() {
        search($(this).val())
    });

    if (query.has("search")) {
        let input = query.get("search");
        field.val(input);
        search(input);
    }
});

function search(content) {
    let input = searchable(content);

    $(".command-info").each(function() {
        let content = searchable($(this).text());
        $(this).css("display", content.includes(input) ? "" : "none");
    });
}

function searchable(input) {
    return input.toLowerCase().replace(pattern, "");
}
