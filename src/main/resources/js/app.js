angular.module("dinnerDate", ["ngRoute"]);

angular.module("dinnerDate").config(function($routeProvider) {
    var token = getValue(location.search,"token");
    $routeProvider
        .when("/homeScreen", {
            templateUrl : "templates/homeScreen.html?token="+token,
            controller : "restaurantsCtrl"
        });
});

$(document).on('click', '.nav li', function() {
    $(".nav li").removeClass("active");
    $(this).addClass("active");
});