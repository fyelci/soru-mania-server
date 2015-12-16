'use strict';

angular.module('soruManiaApp')
    .controller('ScoreHistoryDetailController', function ($scope, $rootScope, $stateParams, entity, ScoreHistory, User, Lov) {
        $scope.scoreHistory = entity;
        $scope.load = function (id) {
            ScoreHistory.get({id: id}, function(result) {
                $scope.scoreHistory = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:scoreHistoryUpdate', function(event, result) {
            $scope.scoreHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
