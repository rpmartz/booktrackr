describe('Controller Tests', function () {

    beforeEach(angular.mock.module('booktrackrApp'));

    describe('HomeController Tests', function () {
        var HomeController, MockBookService, $scope;


        beforeEach(inject(function ($q, $controller, $rootScope) {
            $scope = $rootScope.$new();


            MockBookService = {
                all: function () {
                    var deferred = $q.defer();
                    deferred.resolve({data: []});
                    return deferred.promise;
                }
            };

            HomeController = $controller('HomeController', {Book: MockBookService, $scope: $scope});

        }));

        it('should have "books" defined', function () {
            $scope.$apply();
            expect(HomeController.books).toBeDefined();
        })
    });

    describe('ViewBookController Test', function () {
        var ViewBookController, MockBookService, $scope;

        beforeEach(inject(function ($q, $controller, $rootScope) {
            $scope = $rootScope.$new();

            MockBookService = {
                getBook: function () {
                    var deferred = $q.defer();
                    deferred.resolve({data: {}});
                    return deferred.promise;
                }
            };

            ViewBookController = $controller('ViewBookController', {Book: MockBookService, $scope: $scope});

        }));

        it('should have "book" defined', function () {
            $scope.$apply();
            expect(ViewBookController.book).toBeDefined();
        })
    });

    describe('SignupController Test', function () {
        var SignupController, MockAuthService, $scope;

        beforeEach(inject(function ($q, $controller, $rootScope) {
            $scope = $rootScope.$new();

            MockAuthService = {
                signup: function () {
                    var deferred = $q.defer();
                    deferred.rewsolve({data: {}});
                    return deferred.promise;
                }
            };

            SignupController = $controller('SignupController', {AuthService: MockAuthService, $scope: $scope});
        }));

        it('should signup then redirect to "/books"', function () {
            $scope.$apply();
            expect(SignupController.newUser).toBeDefined();

        })
    })

});
