'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questionRating', {
                parent: 'entity',
                url: '/questionRatings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'QuestionRatings'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionRating/questionRatings.html',
                        controller: 'QuestionRatingController'
                    }
                },
                resolve: {
                }
            })
            .state('questionRating.detail', {
                parent: 'entity',
                url: '/questionRating/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'QuestionRating'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionRating/questionRating-detail.html',
                        controller: 'QuestionRatingDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'QuestionRating', function($stateParams, QuestionRating) {
                        return QuestionRating.get({id : $stateParams.id});
                    }]
                }
            })
            .state('questionRating.new', {
                parent: 'questionRating',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/questionRating/questionRating-dialog.html',
                        controller: 'QuestionRatingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    rate: null,
                                    createDate: null,
                                    lastModifiedDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('questionRating', null, { reload: true });
                    }, function() {
                        $state.go('questionRating');
                    })
                }]
            })
            .state('questionRating.edit', {
                parent: 'questionRating',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/questionRating/questionRating-dialog.html',
                        controller: 'QuestionRatingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['QuestionRating', function(QuestionRating) {
                                return QuestionRating.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('questionRating', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('questionRating.delete', {
                parent: 'questionRating',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/questionRating/questionRating-delete-dialog.html',
                        controller: 'QuestionRatingDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['QuestionRating', function(QuestionRating) {
                                return QuestionRating.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('questionRating', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
