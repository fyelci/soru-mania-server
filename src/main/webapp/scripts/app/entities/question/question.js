'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('question', {
                parent: 'entity',
                url: '/questions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Questions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/question/questions.html',
                        controller: 'QuestionController'
                    }
                },
                resolve: {
                }
            })
            .state('question.detail', {
                parent: 'entity',
                url: '/question/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Question'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/question/question-detail.html',
                        controller: 'QuestionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Question', function($stateParams, Question) {
                        return Question.get({id : $stateParams.id});
                    }]
                }
            })
            .state('question.new', {
                parent: 'question',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/question/question-dialog.html',
                        controller: 'QuestionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    detail: null,
                                    mediaUrl: null,
                                    commentCount: null,
                                    createDate: null,
                                    lastModifiedDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('question', null, { reload: true });
                    }, function() {
                        $state.go('question');
                    })
                }]
            })
            .state('question.edit', {
                parent: 'question',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/question/question-dialog.html',
                        controller: 'QuestionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Question', function(Question) {
                                return Question.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('question', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('question.delete', {
                parent: 'question',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/question/question-delete-dialog.html',
                        controller: 'QuestionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Question', function(Question) {
                                return Question.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('question', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
