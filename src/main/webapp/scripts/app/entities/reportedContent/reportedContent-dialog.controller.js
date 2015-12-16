'use strict';

angular.module('soruManiaApp').controller('ReportedContentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReportedContent', 'LovType', 'Question', 'Comment', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, ReportedContent, LovType, Question, Comment, User) {

        $scope.reportedContent = entity;
        $scope.lovs = LovType.get({type:'REPORTED_TYPE'});
        $scope.questions = Question.query();
        $scope.comments = Comment.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            ReportedContent.get({id : id}, function(result) {
                $scope.reportedContent = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('soruManiaApp:reportedContentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.reportedContent.id != null) {
                ReportedContent.update($scope.reportedContent, onSaveSuccess, onSaveError);
            } else {
                ReportedContent.save($scope.reportedContent, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
