'use strict';

angular.module('soruManiaApp').controller('LovDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lov',
        function($scope, $stateParams, $uibModalInstance, entity, Lov) {

        $scope.lov = entity;
        $scope.load = function(id) {
            Lov.get({id : id}, function(result) {
                $scope.lov = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:lovUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.lov.id != null) {
                Lov.update($scope.lov, onSaveSuccess, onSaveError);
            } else {
                Lov.save($scope.lov, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
