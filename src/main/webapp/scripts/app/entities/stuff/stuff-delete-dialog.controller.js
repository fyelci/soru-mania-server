'use strict';

angular.module('soruManiaApp')
	.controller('StuffDeleteController', function($scope, $uibModalInstance, entity, Stuff) {

        $scope.stuff = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Stuff.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
