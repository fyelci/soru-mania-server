'use strict';

angular.module('soruManiaApp')
	.controller('UserRelationDeleteController', function($scope, $uibModalInstance, entity, UserRelation) {

        $scope.userRelation = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserRelation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
