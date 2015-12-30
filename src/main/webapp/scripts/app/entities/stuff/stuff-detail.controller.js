'use strict';

angular.module('soruManiaApp')
    .controller('StuffDetailController', function ($scope, $rootScope, $stateParams, entity, Stuff, Lov) {
        $scope.stuff = entity;
        $scope.load = function (id) {
            Stuff.get({id: id}, function(result) {
                $scope.stuff = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:stuffUpdate', function(event, result) {
            $scope.stuff = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
