const pattern = /[^a-z\d]/g;

$(document).ready(function () {
    const search = $("#search");

    $(".js").css("display", "inline");

    search.on("input", function() {
        let input = $(this).val();
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

    search.keypress(function(event) {
        if (event.keyCode === 13)
            event.preventDefault();
    })
});
