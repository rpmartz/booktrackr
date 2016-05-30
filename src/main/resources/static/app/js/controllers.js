(function () {

    angular
        .module('booktrackrApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['Book', '$log'];
    function HomeController(Book, $log) {
        var vm = this;

        Book.allBooks().then(function (res) {
            $log.info(res.data);
            vm.books = res.data;
        }, function (err) {
            $log.info(err)
        })
    }

})();
