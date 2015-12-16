'use strict';

angular.module('soruManiaApp').controller('UserContentPreferenceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserContentPreference', 'User', 'Lov',
        function($scope, $stateParams, $uibModalInstance, entity, UserContentPreference, User, Lov) {

        $scope.userContentPreference = entity;
        $scope.users = User.query();
        $scope.lovs = Lov.query();
        $scope.load = function(id) {
            UserContentPreference.get({id : id}, function(result) {
                $scope.userContentPreference = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:userContentPreferenceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userContentPreference.id != null) {
                UserContentPreference.update($scope.userContentPreference, onSaveSuccess, onSaveError);
            } else {
                UserContentPreference.save($scope.userContentPreference, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
