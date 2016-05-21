var gulp = require('gulp');
var wiredep = require('wiredep').stream;

gulp.task('default', function () {
    console.log('Gulp is working.');
});

gulp.task('wiredep', function () {
    return gulp.src('./src/main/resources/static/index.html')
        .pipe(wiredep())
        .pipe(gulp.dest('./build/resources/main/resources/static'));
});
