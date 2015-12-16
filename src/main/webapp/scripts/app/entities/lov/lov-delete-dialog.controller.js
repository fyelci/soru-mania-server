'use strict';

angular.module('soruManiaApp')
	.controller('LovDeleteController', function($scope, $uibModalInstance, entity, Lov) {

        $scope.lov = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Lov.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
