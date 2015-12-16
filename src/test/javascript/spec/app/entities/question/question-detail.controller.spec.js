'use strict';

describe('Question Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockQuestion, MockLov, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockQuestion = jasmine.createSpy('MockQuestion');
        MockLov = jasmine.createSpy('MockLov');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Question': MockQuestion,
            'Lov': MockLov,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("QuestionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'soruManiaApp:questionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
