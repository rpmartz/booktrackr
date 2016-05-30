(function () {
    'use strict';

    angular.module('booktrackrApp').factory('Book', Book);

    Book.$inject = ['$http'];

    function Book($http) {

        return {
            allBooks: function () {
                return $http.get('/books');
            }
        };
    }
})();
