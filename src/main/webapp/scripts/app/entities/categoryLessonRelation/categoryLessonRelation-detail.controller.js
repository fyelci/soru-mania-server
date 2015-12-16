'use strict';

angular.module('soruManiaApp')
    .controller('CategoryLessonRelationDetailController', function ($scope, $rootScope, $stateParams, entity, CategoryLessonRelation, Lov) {
        $scope.categoryLessonRelation = entity;
        $scope.load = function (id) {
            CategoryLessonRelation.get({id: id}, function(result) {
                $scope.categoryLessonRelation = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:categoryLessonRelationUpdate', function(event, result) {
            $scope.categoryLessonRelation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
