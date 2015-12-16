'use strict';

angular.module('soruManiaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('categoryLessonRelation', {
                parent: 'entity',
                url: '/categoryLessonRelations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CategoryLessonRelations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoryLessonRelation/categoryLessonRelations.html',
                        controller: 'CategoryLessonRelationController'
                    }
                },
                resolve: {
                }
            })
            .state('categoryLessonRelation.detail', {
                parent: 'entity',
                url: '/categoryLessonRelation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CategoryLessonRelation'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoryLessonRelation/categoryLessonRelation-detail.html',
                        controller: 'CategoryLessonRelationDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CategoryLessonRelation', function($stateParams, CategoryLessonRelation) {
                        return CategoryLessonRelation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('categoryLessonRelation.new', {
                parent: 'categoryLessonRelation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/categoryLessonRelation/categoryLessonRelation-dialog.html',
                        controller: 'CategoryLessonRelationDialogController',
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
                        $state.go('categoryLessonRelation', null, { reload: true });
                    }, function() {
                        $state.go('categoryLessonRelation');
                    })
                }]
            })
            .state('categoryLessonRelation.edit', {
                parent: 'categoryLessonRelation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/categoryLessonRelation/categoryLessonRelation-dialog.html',
                        controller: 'CategoryLessonRelationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CategoryLessonRelation', function(CategoryLessonRelation) {
                                return CategoryLessonRelation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('categoryLessonRelation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('categoryLessonRelation.delete', {
                parent: 'categoryLessonRelation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/categoryLessonRelation/categoryLessonRelation-delete-dialog.html',
                        controller: 'CategoryLessonRelationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CategoryLessonRelation', function(CategoryLessonRelation) {
                                return CategoryLessonRelation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('categoryLessonRelation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
