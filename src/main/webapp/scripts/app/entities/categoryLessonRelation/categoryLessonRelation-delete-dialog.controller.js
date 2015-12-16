'use strict';

angular.module('soruManiaApp')
	.controller('CategoryLessonRelationDeleteController', function($scope, $uibModalInstance, entity, CategoryLessonRelation) {

        $scope.categoryLessonRelation = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CategoryLessonRelation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
