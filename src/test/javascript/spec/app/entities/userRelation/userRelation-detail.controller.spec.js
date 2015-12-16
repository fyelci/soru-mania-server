'use strict';

describe('UserRelation Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockUserRelation, MockUser, MockLov;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockUserRelation = jasmine.createSpy('MockUserRelation');
        MockUser = jasmine.createSpy('MockUser');
        MockLov = jasmine.createSpy('MockLov');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'UserRelation': MockUserRelation,
            'User': MockUser,
            'Lov': MockLov
        };
        createController = function() {
            $injector.get('$controller')("UserRelationDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'soruManiaApp:userRelationUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
