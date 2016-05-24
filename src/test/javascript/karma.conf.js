module.exports = function (config) {
    config.set({
        basePath: '../../..',

        frameworks: ['jasmine'],

        browsers: ['PhantomJS'],

        files: [
            'src/main/resources/static/bower_components/angular/angular.js',
            'src/main/resources/static/bower_components/angular-route/angular-route.js',
            'src/main/resources/static/app/js/**/*.js',
            'src/test/javascript/spec/**/*.js'
        ]
    });
};
