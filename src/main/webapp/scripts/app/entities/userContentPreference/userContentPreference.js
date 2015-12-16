'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userContentPreference', {
                parent: 'entity',
                url: '/userContentPreferences',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserContentPreferences'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userContentPreference/userContentPreferences.html',
                        controller: 'UserContentPreferenceController'
                    }
                },
                resolve: {
                }
            })
            .state('userContentPreference.detail', {
                parent: 'entity',
                url: '/userContentPreference/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserContentPreference'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userContentPreference/userContentPreference-detail.html',
                        controller: 'UserContentPreferenceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserContentPreference', function($stateParams, UserContentPreference) {
                        return UserContentPreference.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userContentPreference.new', {
                parent: 'userContentPreference',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userContentPreference/userContentPreference-dialog.html',
                        controller: 'UserContentPreferenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    contentId: null,
                                    createDate: null,
                                    lastModifiedDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userContentPreference', null, { reload: true });
                    }, function() {
                        $state.go('userContentPreference');
                    })
                }]
            })
            .state('userContentPreference.edit', {
                parent: 'userContentPreference',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userContentPreference/userContentPreference-dialog.html',
                        controller: 'UserContentPreferenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserContentPreference', function(UserContentPreference) {
                                return UserContentPreference.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userContentPreference', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userContentPreference.delete', {
                parent: 'userContentPreference',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userContentPreference/userContentPreference-delete-dialog.html',
                        controller: 'UserContentPreferenceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserContentPreference', function(UserContentPreference) {
                                return UserContentPreference.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userContentPreference', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
