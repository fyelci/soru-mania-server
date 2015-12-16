'use strict';

angular.module('soruManiaApp')
    .controller('LovDetailController', function ($scope, $rootScope, $stateParams, entity, Lov) {
        $scope.lov = entity;
        $scope.load = function (id) {
            Lov.get({id: id}, function(result) {
                $scope.lov = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:lovUpdate', function(event, result) {
            $scope.lov = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
