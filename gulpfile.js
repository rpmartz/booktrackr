var gulp = require('gulp');
var wiredep = require('wiredep').stream;

gulp.task('default', function () {
    console.log('Gulp is working.');
});

// todo add excludes for bootstrap
gulp.task('wiredep', function () {
    return gulp.src('./src/main/resources/static/index.html')
        .pipe(wiredep({
            exclude: [
                'bower_components/bootstrap/dist/js/' // use ui-bootstrap
            ]
        }))
        .pipe(gulp.dest('./build/resources/main/resources/static'));
});
