'use strict';

describe('QuestionRating Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockQuestionRating, MockQuestion, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockQuestionRating = jasmine.createSpy('MockQuestionRating');
        MockQuestion = jasmine.createSpy('MockQuestion');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'QuestionRating': MockQuestionRating,
            'Question': MockQuestion,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("QuestionRatingDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'soruManiaApp:questionRatingUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
