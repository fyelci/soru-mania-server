'use strict';

angular.module('soruManiaApp')
    .factory('Stuff', function ($resource, DateUtils) {
        return $resource('api/stuffs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
