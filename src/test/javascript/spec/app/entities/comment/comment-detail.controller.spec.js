'use strict';

describe('Comment Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockComment, MockQuestion, MockUser, MockLov;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockComment = jasmine.createSpy('MockComment');
        MockQuestion = jasmine.createSpy('MockQuestion');
        MockUser = jasmine.createSpy('MockUser');
        MockLov = jasmine.createSpy('MockLov');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Comment': MockComment,
            'Question': MockQuestion,
            'User': MockUser,
            'Lov': MockLov
        };
        createController = function() {
            $injector.get('$controller')("CommentDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'soruManiaApp:commentUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
