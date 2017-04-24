angular.module("dinnerDate").controller('restaurantsCtrl', ['$scope', '$http',
    function($scope, $http) {
        window.document.title = "Restaurants";

        $scope.normalView = 1;
        $scope.formData = {};
        $scope.rest;

        var token = getValue(location.search,"token");
        var url = "http://localhost:9001/data/find/Restaurants?sourced=true&token="+token;
        console.log("requesting restaurants");

        $http.post(url,toRestaurant(null,null,null,null,null,null,null,null,null)).success(
            function (response) {
                if(response!="200 success"){
                    $scope.restaurantList = response;
                }else{
                    $scope.restaurantList = null;
                }

                console.log(angular.toJson(restaurantList));
            }
        );


        $scope.searchForRestaurant = function () {
            var stateName = $scope.formData.stateName;
            var cityName = $scope.formData.cityName;
            var moneyName = $scope.formData.moneyName;
            var ratingName = $scope.formData.ratingName;
            var restaurant = toRestaurant(null,ratingName,moneyName,null,null,null,null,stateName,null,cityName);
            var url = "http://localhost:9001/data/find/Restaurants?sourced=true&token="+token;
            $http.post(url,restaurant).success(
                function (response) {
                            $scope.rest = response[0];
                }
            );

                //console.log(angular.toJson(response));
                console.log(angular.toJson($scope.rest));
            $scope.normalView = 1;
        };

    }
]);

$(document).on('click', '#restaurantList a', function() {
    $("#restaurantList a").removeClass("active");
    $(this).addClass("active");
});