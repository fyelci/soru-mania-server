'use strict';

describe('CategoryLessonRelation Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCategoryLessonRelation, MockLov;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCategoryLessonRelation = jasmine.createSpy('MockCategoryLessonRelation');
        MockLov = jasmine.createSpy('MockLov');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'CategoryLessonRelation': MockCategoryLessonRelation,
            'Lov': MockLov
        };
        createController = function() {
            $injector.get('$controller')("CategoryLessonRelationDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'soruManiaApp:categoryLessonRelationUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
