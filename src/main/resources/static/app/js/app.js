(function () {
    'use strict';

    angular.module('booktrackrApp', ['ngRoute', 'ngMessages', 'angular-storage']);

    angular.module('booktrackrApp').config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'partials/home.html',
                controller: 'HomeController',
                controllerAs: 'vm'
            })
            .when('/books', {
                templateUrl: 'partials/books.html'
            })
            .when('/books/new', {
                templateUrl: 'partials/new-book.html'
            })
            .when('/books/:bookId', {
                templateUrl: 'partials/view-book.html'
            })
            .when('/login', {
                templateUrl: 'partials/login.html'
            })
            .when('/signup', {
                templateUrl: 'partials/signup.html'
            });

        $httpProvider.interceptors.push('authInterceptor');
    }]);

    angular.module('booktrackrApp').factory('authInterceptor', function ($rootScope, TokenService) {

        var authInterceptor = {
            request: function (config) {
                var currentUserToken = TokenService.getCurrentUserToken();
                var token = currentUserToken ? currentUserToken : null;

                if (token) {
                    config.headers.authorization = token;
                }
                return config;
            },
            response: function (response) {
                if (response.status === 401) {
                    $rootScope.$broadcast('unauthorized');
                }
                else if (response.status == 403) {
                    $rootScope.$broadcast('forbidden');
                }
                return response;
            }
        };
        return authInterceptor;
    });

})();
