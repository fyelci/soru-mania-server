'use strict';

angular.module('soruManiaApp')
    .controller('UserContentPreferenceDetailController', function ($scope, $rootScope, $stateParams, entity, UserContentPreference, User, Lov) {
        $scope.userContentPreference = entity;
        $scope.load = function (id) {
            UserContentPreference.get({id: id}, function(result) {
                $scope.userContentPreference = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:userContentPreferenceUpdate', function(event, result) {
            $scope.userContentPreference = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
