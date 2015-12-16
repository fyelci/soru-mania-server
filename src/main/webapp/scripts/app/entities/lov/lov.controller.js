'use strict';

angular.module('soruManiaApp')
    .controller('LovController', function ($scope, $state, Lov) {

        $scope.lovs = [];
        $scope.loadAll = function() {
            Lov.query(function(result) {
               $scope.lovs = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.lov = {
                type: null,
                name: null,
                strParam1: null,
                strParam2: null,
                intParam1: null,
                description: null,
                status: null,
                sequence: null,
                id: null
            };
        };
    });
