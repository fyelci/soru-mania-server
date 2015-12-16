'use strict';

angular.module('soruManiaApp')
    .controller('UserContentPreferenceController', function ($scope, $state, UserContentPreference, ParseLinks) {

        $scope.userContentPreferences = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            UserContentPreference.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.userContentPreferences = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userContentPreference = {
                contentId: null,
                createDate: null,
                lastModifiedDate: null,
                id: null
            };
        };
    });
