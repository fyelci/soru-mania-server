'use strict';

angular.module('soruManiaApp').controller('QuestionRatingDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'QuestionRating', 'Question', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, QuestionRating, Question, User) {

        $scope.questionRating = entity;
        $scope.questions = Question.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            QuestionRating.get({id : id}, function(result) {
                $scope.questionRating = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:questionRatingUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.questionRating.id != null) {
                QuestionRating.update($scope.questionRating, onSaveSuccess, onSaveError);
            } else {
                QuestionRating.save($scope.questionRating, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreateDate = {};

        $scope.datePickerForCreateDate.status = {
            opened: false
        };

        $scope.datePickerForCreateDateOpen = function($event) {
            $scope.datePickerForCreateDate.status.opened = true;
        };
        $scope.datePickerForLastModifiedDate = {};

        $scope.datePickerForLastModifiedDate.status = {
            opened: false
        };

        $scope.datePickerForLastModifiedDateOpen = function($event) {
            $scope.datePickerForLastModifiedDate.status.opened = true;
        };
}]);
