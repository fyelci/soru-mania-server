'use strict';

describe('UserContentPreference Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockUserContentPreference, MockUser, MockLov;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockUserContentPreference = jasmine.createSpy('MockUserContentPreference');
        MockUser = jasmine.createSpy('MockUser');
        MockLov = jasmine.createSpy('MockLov');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'UserContentPreference': MockUserContentPreference,
            'User': MockUser,
            'Lov': MockLov
        };
        createController = function() {
            $injector.get('$controller')("UserContentPreferenceDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'soruManiaApp:userContentPreferenceUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
