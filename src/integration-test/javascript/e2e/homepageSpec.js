describe('Demo Protractor Test', function () {
    it('Should render the home.html partial', function () {
        browser.get('/');

        expect(element(by.tagName('p')).getText()).toBe('Rendered Courtesy of AngularJS!');
    });
});
