(function () {
    'use strict';

    angular.module('booktrackrApp').factory('Book', Book);
    Book.$inject = ['$http'];

    function Book($http) {
        var service = {
            all: allBooks,
            create: create,
            getBook: getBook,
            update: update
        };

        function allBooks() {
            return $http.get('/books');
        }

        function create(newBook) {
            return $http.post('/books', newBook);
        }

        function getBook(id) {
            var bookId = '/books/' + id;
            return $http.get(bookId);
        }

        function update(id, book) {
            var path = '/books/' + id;
            return $http.put(path, book);
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

    angular.module('booktrackrApp').factory('TokenService', TokenService);
    TokenService.$inject = ['store'];

    function TokenService(store) {

        var service = {
            setCurrentUserToken: setCurrentUserToken,
            getCurrentUserToken: getCurrentUserToken
        };

        function setCurrentUserToken(token) {
            store.set('currentUserToken', token);
        }

        function getCurrentUserToken() {
            return store.get('currentUserToken');
        }

        return service
    }

})();
