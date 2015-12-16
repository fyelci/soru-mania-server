'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('reportedContent', {
                parent: 'entity',
                url: '/reportedContents',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ReportedContents'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reportedContent/reportedContents.html',
                        controller: 'ReportedContentController'
                    }
                },
                resolve: {
                }
            })
            .state('reportedContent.detail', {
                parent: 'entity',
                url: '/reportedContent/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ReportedContent'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reportedContent/reportedContent-detail.html',
                        controller: 'ReportedContentDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ReportedContent', function($stateParams, ReportedContent) {
                        return ReportedContent.get({id : $stateParams.id});
                    }]
                }
            })
            .state('reportedContent.new', {
                parent: 'reportedContent',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/reportedContent/reportedContent-dialog.html',
                        controller: 'ReportedContentDialogController',
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
                        $state.go('reportedContent', null, { reload: true });
                    }, function() {
                        $state.go('reportedContent');
                    })
                }]
            })
            .state('reportedContent.edit', {
                parent: 'reportedContent',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/reportedContent/reportedContent-dialog.html',
                        controller: 'ReportedContentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ReportedContent', function(ReportedContent) {
                                return ReportedContent.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('reportedContent', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('reportedContent.delete', {
                parent: 'reportedContent',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/reportedContent/reportedContent-delete-dialog.html',
                        controller: 'ReportedContentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ReportedContent', function(ReportedContent) {
                                return ReportedContent.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('reportedContent', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
