var gulp = require('gulp');
var wiredep = require('wiredep').stream;

gulp.task('default', function () {
    console.log('Gulp is working.');
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
        .pipe(gulp.dest('./build/resources/main/resources/static'));
});
