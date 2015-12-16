'use strict';

angular.module('soruManiaApp')
    .controller('LovController', function ($scope, $state, $uibModal, Lov, LovType) {

        $scope.lovs = [];
        $scope.lovTypes = [];
        $scope.selectedLovType = {};

        $scope.loadAll = function() {
            Lov.query(function(result) {
               $scope.lovs = result;
            });
        };

        $scope.loadLovList = function() {
            LovType.query({type:$scope.selectedLovType.name}, function(result) {
               $scope.lovs = result;
            });
        };

        $scope.loadTypes = function() {
            LovType.query(function(result) {
               $scope.lovTypes = result;
            });
        };

        $scope.loadTypes();


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
