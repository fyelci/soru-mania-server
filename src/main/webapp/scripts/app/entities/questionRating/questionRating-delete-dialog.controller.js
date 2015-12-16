'use strict';

angular.module('soruManiaApp')
	.controller('QuestionRatingDeleteController', function($scope, $uibModalInstance, entity, QuestionRating) {

        $scope.questionRating = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            QuestionRating.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
