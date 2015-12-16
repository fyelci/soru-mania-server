'use strict';

angular.module('soruManiaApp')
    .controller('ReportedContentDetailController', function ($scope, $rootScope, $stateParams, entity, ReportedContent, Lov, Question, Comment, User) {
        $scope.reportedContent = entity;
        $scope.load = function (id) {
            ReportedContent.get({id: id}, function(result) {
                $scope.reportedContent = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:reportedContentUpdate', function(event, result) {
            $scope.reportedContent = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
