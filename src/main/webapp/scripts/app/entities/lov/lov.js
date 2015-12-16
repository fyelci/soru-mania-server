'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lov', {
                parent: 'entity',
                url: '/lovs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Parametreler'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lov/lovs.html',
                        controller: 'LovController'
                    }
                },
                resolve: {
                }
            })
            .state('lov.detail', {
                parent: 'entity',
                url: '/lov/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Parametre'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lov/lov-detail.html',
                        controller: 'LovDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Lov', function($stateParams, Lov) {
                        return Lov.get({id : $stateParams.id});
                    }]
                }
            })
            .state('lov.new', {
                parent: 'lov',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lov/lov-dialog.html',
                        controller: 'LovDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
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
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('lov', null, { reload: true });
                    }, function() {
                        $state.go('lov');
                    })
                }]
            })
            .state('lov.edit', {
                parent: 'lov',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lov/lov-dialog.html',
                        controller: 'LovDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Lov', function(Lov) {
                                return Lov.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('lov', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('lov.delete', {
                parent: 'lov',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lov/lov-delete-dialog.html',
                        controller: 'LovDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Lov', function(Lov) {
                                return Lov.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('lov', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
