'use strict';

angular.module('soruManiaApp').controller('QuestionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Question', 'LovType', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Question, LovType, User) {

        $scope.question = entity;
        $scope.categories = LovType.get({type:'CATEGORY'});
        $scope.lessons = LovType.get({type:'LESSON'});
        $scope.questionStatuses = LovType.get({type:'QUESTION_STATUS'});
        $scope.users = User.query();
        $scope.load = function(id) {
            Question.get({id : id}, function(result) {
                $scope.question = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:questionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.question.id != null) {
                Question.update($scope.question, onSaveSuccess, onSaveError);
            } else {
                Question.save($scope.question, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
