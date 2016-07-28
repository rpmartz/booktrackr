describe('Controller Tests', function () {

    beforeEach(angular.mock.module('booktrackrApp'));


    // beforeEach(angular.mock.inject(function(_$controller_){
    //     $controller = _$controller_;
    // }));

    describe('HomeControllerTests', function () {
        var HomeController, MockBookService, $scope;


        beforeEach(inject(function ($q, $controller, $rootScope) {
            $scope = $rootScope.$new();


            MockBookService = {
                all: function () {
                    var deferred = $q.defer();
                    deferred.resolve([]);
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
});
