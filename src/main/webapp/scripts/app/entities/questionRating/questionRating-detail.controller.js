'use strict';

angular.module('soruManiaApp')
    .controller('QuestionRatingDetailController', function ($scope, $rootScope, $stateParams, entity, QuestionRating, Question, User) {
        $scope.questionRating = entity;
        $scope.load = function (id) {
            QuestionRating.get({id: id}, function(result) {
                $scope.questionRating = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:questionRatingUpdate', function(event, result) {
            $scope.questionRating = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
