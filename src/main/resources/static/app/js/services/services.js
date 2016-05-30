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
})();
