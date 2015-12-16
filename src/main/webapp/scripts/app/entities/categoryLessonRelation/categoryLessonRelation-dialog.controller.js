'use strict';

angular.module('soruManiaApp').controller('CategoryLessonRelationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CategoryLessonRelation', 'LovType',
        function($scope, $stateParams, $uibModalInstance, entity, CategoryLessonRelation, LovType) {

        $scope.categoryLessonRelation = entity;
        $scope.categories = LovType.get({type:'CATEGORY'});
        $scope.lessons = LovType.get({type:'LESSON'});
        $scope.load = function(id) {
            CategoryLessonRelation.get({id : id}, function(result) {
                $scope.categoryLessonRelation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:categoryLessonRelationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.categoryLessonRelation.id != null) {
                CategoryLessonRelation.update($scope.categoryLessonRelation, onSaveSuccess, onSaveError);
            } else {
                CategoryLessonRelation.save($scope.categoryLessonRelation, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
