var gulp = require("gulp");
var browserify = require("browserify");
var source = require('vinyl-source-stream');
var tsify = require("tsify");
var paths = {
  pages: ['src/chart.html']
};

gulp.task("copy-html", function () {
  return gulp.src(paths.pages)
    .pipe(gulp.dest("target/bundle"));
});

gulp.task("create-bundle", function () {
  return browserify({
    basedir: '.',
    debug: true,
    entries: [
      'src/ProcessValue.ts',
      'src/DataPoint.ts',
      'src/ProcessValueGraph.ts'
    ],
    cache: {},
    packageCache: {}
  })
    .plugin(tsify)
    .bundle()
    .pipe(source('bundle.js'))
    .pipe(gulp.dest("target/bundle"));
})


gulp.task("default", gulp.series("copy-html", "create-bundle"));
