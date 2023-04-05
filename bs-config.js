module.exports = {
  proxy: 'digitaltrailsapp:8080',
  files: ['source/src/main/resources/static/**/*.{html,css,js}'],
  watch: true,
  watchEvents: ['change'],
  open: false,
  reloadDelay: 500,
  ui: false,
  port: 3000,
};