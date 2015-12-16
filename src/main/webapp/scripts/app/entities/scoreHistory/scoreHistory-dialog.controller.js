'use strict';

angular.module('soruManiaApp').controller('ScoreHistoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ScoreHistory', 'User', 'LovType',
        function($scope, $stateParams, $uibModalInstance, entity, ScoreHistory, User, LovType) {

        $scope.scoreHistory = entity;
        $scope.users = User.query();
        $scope.contentTypes = LovType.get({type:'CONTENT_TYPE'});
        $scope.transactionTypes = LovType.get({type:'SCORE_TRANSACTION_TYPE'});
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
