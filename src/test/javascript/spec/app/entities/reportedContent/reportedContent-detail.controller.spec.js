'use strict';

describe('ReportedContent Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockReportedContent, MockLov, MockQuestion, MockComment, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockReportedContent = jasmine.createSpy('MockReportedContent');
        MockLov = jasmine.createSpy('MockLov');
        MockQuestion = jasmine.createSpy('MockQuestion');
        MockComment = jasmine.createSpy('MockComment');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ReportedContent': MockReportedContent,
            'Lov': MockLov,
            'Question': MockQuestion,
            'Comment': MockComment,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("ReportedContentDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'soruManiaApp:reportedContentUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
