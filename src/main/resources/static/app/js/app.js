(function () {
    'use strict';

    angular.module('booktrackrApp', ['ngRoute']);

    angular.module('booktrackrApp').config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'partials/home.html',
                controller: 'HomeController',
                controllerAs: 'vm'
            });


    }]);

})();
