(function () {

    angular
        .module('booktrackrApp')
        .controller('HomeController', HomeController)
        .controller('SignupController', SignupController);

    HomeController.$inject = ['Book', '$log'];
    function HomeController(Book, $log) {
        var vm = this;

        Book.all().then(function (res) {
            $log.info(res.data);
            vm.books = res.data;
        }, function (err) {
            $log.info(err)
        })
    }

    SignupController.$inject = [];
    function SignupController() {
        var vm = this;
    }

})();
