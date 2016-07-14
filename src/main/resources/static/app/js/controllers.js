(function () {

    angular
        .module('booktrackrApp')
        .controller('HomeController', HomeController)
        .controller('SignupController', SignupController)
        .controller('LoginController', LoginController)
        .controller('BooksController', BooksController)
        .controller('NewBookController', NewBookController)
        .controller('EditBookController', EditBookController)
        .controller('DeleteBookController', DeleteBookController);

    HomeController.$inject = ['Book', '$log'];
    function HomeController(Book, $log) {
        var vm = this;

        Book.all().then(function (res) {
            vm.books = res.data;

        }, function (err) {
            $log.error('all books call failed: ', err);
        });
    }

    SignupController.$inject = ['AuthService', '$log', '$location'];
    function SignupController(AuthService, $log, $location) {
        var vm = this;
        vm.newUser = {};

        vm.signup = function () {
            AuthService.signup(vm.newUser)
                .then(function (res) {
                        $location.path('/books');
                    },
                    function (err) {
                        $log.error('Signup failed ', err);
                    });

        }

    }

    LoginController.$inject = ['AuthService', '$log', '$location', 'TokenService'];
    function LoginController(AuthService, $log, $location, TokenService) {
        var vm = this;
        vm.credentials = {};

        vm.authenticate = function () {
            AuthService.login(vm.credentials)
                .then(
                    function (res) {
                        var jwt = res.headers('Authorization');
                        TokenService.setCurrentUserToken(jwt);
                        $location.path('/books');
                    },
                    function (err) {
                        $log.error('Auth Failed ', err);
                        // todo show failed auth message
                    })
        }
    }

    BooksController.$inject = ['Book', '$log', '$uibModal'];
    function BooksController(Book, $log, $uibModal) {
        var vm = this;
        vm.books = [];

        Book.all().then(function (res) {
            vm.books = res.data;
        }, function (err) {
            $log.error('all books call failed', err);
        });

        vm.openDeleteModal = function (book) {
            var modalInstance = $uibModal.open({
                templateUrl: 'deleteBookModal.html',
                controller: 'DeleteBookController',
                resolve: {
                    book: book
                }
            });

            modalInstance.result.then(function (bookToDelete) {
                for (var i = 0; i <= vm.books.length - 1; i++) {
                    if (vm.books[i].id == bookToDelete.id) {
                        vm.books.splice(i, 1);
                    }
                }
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        }
    }

    DeleteBookController.$inject = ['Book', '$log', '$location', '$scope', '$uibModalInstance', 'book'];
    function DeleteBookController(Book, $log, $location, $scope, $uibModalInstance, book) {
        $scope.book = book;

        $scope.ok = function () {
            Book.delete(book).then(function (res) {
                $uibModalInstance.close($scope.book);
            }, function (err) {
                $log.error('Error deleting book ', err);
            });

        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }

    NewBookController.$inject = ['Book', '$location', '$log'];
    function NewBookController(Book, $location, $log) {
        var vm = this;
        vm.book = {};

        vm.save = function () {
            Book.create(vm.book).then(function () {
                $location.path('/books');
            }, function (err) {
                $log.debug('error creating new book', err);
            })
        }
    }

    EditBookController.$inject = ['Book', '$location', '$routeParams'];
    function EditBookController(Book, $location, $routeParams) {
        var vm = this;
        vm.book = {};

        var currentBookId = $routeParams.bookId;
        Book.getBook($routeParams.bookId).then(
            function (res) {
                vm.book = res.data;
            },
            function (err) {
                $log.error('Error getting book by ID');
            }
        );

        vm.save = function () {
            Book.update(currentBookId, vm.book).then(function (res) {
                $location.path('/books');
            }, function (err) {
                $log.error('save book failed', err);
            })
        };
    }

})();
