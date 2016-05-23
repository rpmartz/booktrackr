exports.config = {
    framework: 'jasmine',
    seleniumAddress: 'http://localhost:4444/wd/hub',
    specs: ['e2e/**/*.js'],

    baseUrl: 'http://localhost:8080/',
    directConnect: true,
    capabilities: {
        'browserName': 'firefox'
    }
};
