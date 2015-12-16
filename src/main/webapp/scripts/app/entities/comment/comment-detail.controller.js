'use strict';

angular.module('soruManiaApp')
    .controller('CommentDetailController', function ($scope, $rootScope, $stateParams, entity, Comment, Question, User, Lov) {
        $scope.comment = entity;
        $scope.load = function (id) {
            Comment.get({id: id}, function(result) {
                $scope.comment = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:commentUpdate', function(event, result) {
            $scope.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
