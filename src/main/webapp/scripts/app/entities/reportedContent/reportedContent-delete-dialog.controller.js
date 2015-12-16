'use strict';

angular.module('soruManiaApp')
	.controller('ReportedContentDeleteController', function($scope, $uibModalInstance, entity, ReportedContent) {

        $scope.reportedContent = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ReportedContent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
