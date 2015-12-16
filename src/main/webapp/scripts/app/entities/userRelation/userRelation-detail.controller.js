'use strict';

angular.module('soruManiaApp')
    .controller('UserRelationDetailController', function ($scope, $rootScope, $stateParams, entity, UserRelation, User, Lov) {
        $scope.userRelation = entity;
        $scope.load = function (id) {
            UserRelation.get({id: id}, function(result) {
                $scope.userRelation = result;
            });
        };
        var unsubscribe = $rootScope.$on('soruManiaApp:userRelationUpdate', function(event, result) {
            $scope.userRelation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
