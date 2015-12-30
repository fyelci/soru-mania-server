'use strict';

angular.module('soruManiaApp').controller('StuffDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stuff', 'LovType',
        function($scope, $stateParams, $uibModalInstance, entity, Stuff, LovType) {

        $scope.stuff = entity;
        $scope.lovs = LovType.get({type:'LESSON'});
        $scope.load = function(id) {
            Stuff.get({id : id}, function(result) {
                $scope.stuff = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:stuffUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.stuff.id != null) {
                Stuff.update($scope.stuff, onSaveSuccess, onSaveError);
            } else {
                Stuff.save($scope.stuff, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
