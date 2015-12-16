'use strict';

angular.module('soruManiaApp')
	.controller('ScoreHistoryDeleteController', function($scope, $uibModalInstance, entity, ScoreHistory) {

        $scope.scoreHistory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ScoreHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
