var gulp = require('gulp');
var wiredep = require('wiredep').stream;
var inject = require('gulp-inject');
var angularFilesort = require('gulp-angular-filesort');
var runSequence = require('run-sequence');

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

gulp.task('build', function () {
    runSequence('wiredep', 'inject');
});

