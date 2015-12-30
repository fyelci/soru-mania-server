'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stuff', {
                parent: 'entity',
                url: '/stuffs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Stuffs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stuff/stuffs.html',
                        controller: 'StuffController'
                    }
                },
                resolve: {
                }
            })
            .state('stuff.detail', {
                parent: 'entity',
                url: '/stuff/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Stuff'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stuff/stuff-detail.html',
                        controller: 'StuffDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Stuff', function($stateParams, Stuff) {
                        return Stuff.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stuff.new', {
                parent: 'stuff',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stuff/stuff-dialog.html',
                        controller: 'StuffDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nameSurname: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stuff', null, { reload: true });
                    }, function() {
                        $state.go('stuff');
                    })
                }]
            })
            .state('stuff.edit', {
                parent: 'stuff',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stuff/stuff-dialog.html',
                        controller: 'StuffDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Stuff', function(Stuff) {
                                return Stuff.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stuff', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('stuff.delete', {
                parent: 'stuff',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stuff/stuff-delete-dialog.html',
                        controller: 'StuffDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Stuff', function(Stuff) {
                                return Stuff.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stuff', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
