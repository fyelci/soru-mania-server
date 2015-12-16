'use strict';

angular.module('soruManiaApp')
    .controller('QuestionDetailController', function ($scope, $rootScope, $stateParams, entity, Question, Lov, User) {
        $scope.question = entity;
        $scope.load = function (id) {
            Question.get({id: id}, function(result) {
                $scope.question = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:questionUpdate', function(event, result) {
            $scope.question = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
