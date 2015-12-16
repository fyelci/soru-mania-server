'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userRelation', {
                parent: 'entity',
                url: '/userRelations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserRelations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userRelation/userRelations.html',
                        controller: 'UserRelationController'
                    }
                },
                resolve: {
                }
            })
            .state('userRelation.detail', {
                parent: 'entity',
                url: '/userRelation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserRelation'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userRelation/userRelation-detail.html',
                        controller: 'UserRelationDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserRelation', function($stateParams, UserRelation) {
                        return UserRelation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userRelation.new', {
                parent: 'userRelation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userRelation/userRelation-dialog.html',
                        controller: 'UserRelationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    createDate: null,
                                    lastModifiedDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userRelation', null, { reload: true });
                    }, function() {
                        $state.go('userRelation');
                    })
                }]
            })
            .state('userRelation.edit', {
                parent: 'userRelation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userRelation/userRelation-dialog.html',
                        controller: 'UserRelationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserRelation', function(UserRelation) {
                                return UserRelation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userRelation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userRelation.delete', {
                parent: 'userRelation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userRelation/userRelation-delete-dialog.html',
                        controller: 'UserRelationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserRelation', function(UserRelation) {
                                return UserRelation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userRelation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
