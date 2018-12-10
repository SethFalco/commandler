const pattern = /[^a-z\d]/g;

$(document).ready(function () {
    $(".js").css("display", "inline");

    $("#search").on("input", function() {
        let input = document.getElementById("search").value;
        let commands = $(".command-info");

        if (!input)
            return commands.css("display", "");

        commands.each(function() {
            let content = $(this).text().toLowerCase().replace(pattern, "");
            input = input.toLowerCase().replace(pattern, "");

            if (content.includes(input))
                return $(this).css("display", "");

            $(this).css("display", "none");
        });
    });
});
