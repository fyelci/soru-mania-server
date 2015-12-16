 'use strict';

angular.module('soruManiaApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-soruManiaApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-soruManiaApp-params')});
                }
                return response;
            }
        };
    });
