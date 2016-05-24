var gulp = require('gulp');
var wiredep = require('wiredep').stream;
var inject = require('gulp-inject');
var angularFilesort = require('gulp-angular-filesort');
var runSequence = require('run-sequence');
var Server = require('karma').Server;
var protractor = require('gulp-protractor').protractor;
var bower = require('gulp-bower');

gulp.task('bower', function () {
    return bower();
});

gulp.task('wiredep', function () {
    return gulp.src('src/main/resources/static/index.html')
        .pipe(wiredep({
            exclude: [
                'bower_components/bootstrap/dist/js/' // use ui-bootstrap
            ],
            overrides: { // see discussion here https://github.com/twbs/bootstrap/issues/16663
                bootstrap: {
                    main: [
                        "dist/css/bootstrap.css"
                    ]
                }
            }
        }))
        .pipe(gulp.dest('src/main/resources/static'));
});

gulp.task('inject', function () {
    return gulp.src('src/main/resources/static/index.html')
        .pipe(inject(
            gulp.src('src/main/resources/static/app/js/**/*.js')
                .pipe(angularFilesort()), {relative: true}
        ))
        .pipe(gulp.dest('src/main/resources/static'));

});

gulp.task('test', function (done) {
    new Server({
        configFile: __dirname + '/src/test/javascript/karma.conf.js',
        singleRun: true
    }, done).start();
});

gulp.task('itest', function () {
    var configObj = {
        configFile: 'src/integration-test/javascript/protractor.conf.js'
    };

    return gulp.src([])
        .pipe(protractor(configObj))
        .on('error', function () {
            console.log('E2E Tests failed');
            process.exit(1);
        });
});

gulp.task('build', function () {
    runSequence('bower', 'wiredep', 'inject');
});

