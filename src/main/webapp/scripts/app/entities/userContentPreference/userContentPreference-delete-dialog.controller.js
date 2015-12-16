'use strict';

angular.module('soruManiaApp')
	.controller('UserContentPreferenceDeleteController', function($scope, $uibModalInstance, entity, UserContentPreference) {

        $scope.userContentPreference = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserContentPreference.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
