(function () {
    'use strict';

    angular.module('booktrackrApp').factory('Book', Book);
    Book.$inject = ['$http'];

    function Book($http) {
        var service = {
            all: allBooks
        };

        function allBooks() {
            return $http.get('/books');
        }

        return service;
    }

    angular.module('booktrackrApp').factory('AuthService', AuthService);
    AuthService.$inject = ['$http'];

    function AuthService($http) {
        var service = {
            login: login,
            signup: signup
        };

        function login(credentials) {
            return $http.post('/authenticate', credentials);
        }

        function signup(signupForm) {
            return $http.post('/users', signupForm);
        }

        return service;
    }
})();
