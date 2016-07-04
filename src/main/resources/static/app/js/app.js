(function () {
    'use strict';

    angular.module('booktrackrApp', ['ngRoute', 'ngMessages', 'angular-storage']);

    angular.module('booktrackrApp').config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'partials/home.html',
                controller: 'HomeController',
                controllerAs: 'vm'
            })
            .when('/login', {
                templateUrl: 'partials/login.html'
            })
            .when('/signup', {
                templateUrl: 'partials/signup.html',
            });


    }]);

})();
