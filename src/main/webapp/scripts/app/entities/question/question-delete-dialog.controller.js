'use strict';

angular.module('soruManiaApp')
	.controller('QuestionDeleteController', function($scope, $uibModalInstance, entity, Question) {

        $scope.question = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Question.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
