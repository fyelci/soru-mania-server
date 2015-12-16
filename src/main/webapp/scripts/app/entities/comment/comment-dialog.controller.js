'use strict';

angular.module('soruManiaApp').controller('CommentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Comment', 'Question', 'User', 'LovType',
        function($scope, $stateParams, $uibModalInstance, entity, Comment, Question, User, LovType) {

        $scope.comment = entity;
        $scope.questions = Question.query();
        $scope.users = User.query();
        $scope.lovs = LovType.get({type:'COMMENT_STATUS'});
        $scope.load = function(id) {
            Comment.get({id : id}, function(result) {
                $scope.comment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:commentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.comment.id != null) {
                Comment.update($scope.comment, onSaveSuccess, onSaveError);
            } else {
                Comment.save($scope.comment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
