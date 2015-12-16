'use strict';

angular.module('soruManiaApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


