'use strict';

angular.module('soruManiaApp').controller('UserRelationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserRelation', 'User', 'Lov',
        function($scope, $stateParams, $uibModalInstance, entity, UserRelation, User, Lov) {

        $scope.userRelation = entity;
        $scope.users = User.query();
        $scope.lovs = Lov.query();
        $scope.load = function(id) {
            UserRelation.get({id : id}, function(result) {
                $scope.userRelation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:userRelationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userRelation.id != null) {
                UserRelation.update($scope.userRelation, onSaveSuccess, onSaveError);
            } else {
                UserRelation.save($scope.userRelation, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
