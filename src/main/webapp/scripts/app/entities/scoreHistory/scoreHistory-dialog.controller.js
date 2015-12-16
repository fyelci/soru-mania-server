'use strict';

angular.module('soruManiaApp').controller('ScoreHistoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ScoreHistory', 'User', 'Lov',
        function($scope, $stateParams, $uibModalInstance, entity, ScoreHistory, User, Lov) {

        $scope.scoreHistory = entity;
        $scope.users = User.query();
        $scope.lovs = Lov.query();
        $scope.load = function(id) {
            ScoreHistory.get({id : id}, function(result) {
                $scope.scoreHistory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:scoreHistoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.scoreHistory.id != null) {
                ScoreHistory.update($scope.scoreHistory, onSaveSuccess, onSaveError);
            } else {
                ScoreHistory.save($scope.scoreHistory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
