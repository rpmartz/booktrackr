module.exports = function (config) {
    config.set({
        basePath: '../../..',

        frameworks: ['jasmine'],

        browsers: ['PhantomJS'],

        files: [
            'src/main/resources/static/bower_components/angular/angular.js',
            'src/main/resources/static/bower_components/angular-messages/angular-messages.js',
            'src/main/resources/static/bower_components/angular-mocks/angular-mocks.js',
            'src/main/resources/static/bower_components/angular-route/angular-route.js',
            'src/main/resources/static/bower_components/a0-angular-storage/dist/angular-storage.js',
            'src/main/resources/static/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'src/main/resources/static/bower_components/angular-sanitize/angular-sanitize.js',
            'src/main/resources/static/bower_components/showdown/src/showdown.js',
            'src/main/resources/static/bower_components/angular-markdown-directive/markdown.js',
            'src/main/resources/static/app/js/**/*.js',
            'src/test/javascript/spec/**/*.js'
        ]
    });
};
