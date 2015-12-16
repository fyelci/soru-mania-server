'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('scoreHistory', {
                parent: 'entity',
                url: '/scoreHistorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ScoreHistorys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scoreHistory/scoreHistorys.html',
                        controller: 'ScoreHistoryController'
                    }
                },
                resolve: {
                }
            })
            .state('scoreHistory.detail', {
                parent: 'entity',
                url: '/scoreHistory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ScoreHistory'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scoreHistory/scoreHistory-detail.html',
                        controller: 'ScoreHistoryDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ScoreHistory', function($stateParams, ScoreHistory) {
                        return ScoreHistory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('scoreHistory.new', {
                parent: 'scoreHistory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scoreHistory/scoreHistory-dialog.html',
                        controller: 'ScoreHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    score: null,
                                    contentId: null,
                                    createDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('scoreHistory', null, { reload: true });
                    }, function() {
                        $state.go('scoreHistory');
                    })
                }]
            })
            .state('scoreHistory.edit', {
                parent: 'scoreHistory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scoreHistory/scoreHistory-dialog.html',
                        controller: 'ScoreHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ScoreHistory', function(ScoreHistory) {
                                return ScoreHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scoreHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('scoreHistory.delete', {
                parent: 'scoreHistory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scoreHistory/scoreHistory-delete-dialog.html',
                        controller: 'ScoreHistoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ScoreHistory', function(ScoreHistory) {
                                return ScoreHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scoreHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
