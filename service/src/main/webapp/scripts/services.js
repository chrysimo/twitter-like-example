'use strict';

/* Services */

jhipsterApp.factory('LanguageService', function ($http, $translate, LANGUAGES) {
        return {
            getBy: function(language) {
                if (language == undefined) {
                    language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');
                }
                if (language == undefined) {
                    language = 'en';
                }

                var promise =  $http.get('i18n/' + language + '.json').then(function(response) {
                    return LANGUAGES;
                });
                return promise;
            }
        };
    });

jhipsterApp.factory('Register', function ($resource) {
        return $resource('/rest/api/guest/register', {}, {
        });
    });

jhipsterApp.factory('Activate', function ($resource) {
        return $resource('/rest/api/users/activate', {}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });
    });

jhipsterApp.factory('Account', function ($resource) {
        return $resource('/rest/api/users/account', {}, {
        });
    });

jhipsterApp.factory('Password', function ($resource) {
        return $resource('/rest/api/users/account/change_password', {}, {
        });
    });

jhipsterApp.factory('Sessions', function ($resource) {
        return $resource('/rest/api/users/account/sessions/:series', {}, {
            'get': { method: 'GET', isArray: true}
        });
    });

jhipsterApp.factory('MetricsService',function ($http) {
    		return {
            get: function() {
                var promise = $http.get('/rest/api/admin/metrics').then(function(response){
                    return response.data;
                });
                return promise;
            }
        };
    });

jhipsterApp.factory('ThreadDumpService', function ($http) {
        return {
            dump: function() {
                var promise = $http.get('/rest/api/admin/dump').then(function(response){
                    return response.data;
                });
                return promise;
            }
        };
    });

jhipsterApp.factory('HealthCheckService', function ($rootScope, $http) {
        return {
            check: function() {
                var promise = $http.get('/rest/api/admin/health').then(function(response){
                    return response.data;
                });
                return promise;
            }
        };
    });

jhipsterApp.factory('ConfigurationService', function ($rootScope, $filter, $http) {
    return {
        get: function() {
            var promise = $http.get('/rest/api/admin/configprops').then(function(response){
                var properties = [];
                angular.forEach(response.data, function(data) {
                    properties.push(data);
                });
                var orderBy = $filter('orderBy');
                return orderBy(properties, 'prefix');;
            });
            return promise;
        }
    };
});

jhipsterApp.factory('LogsService', function ($resource) {
        return $resource('/rest/api/admin/logs', {}, {
            'findAll': { method: 'GET', isArray: true},
            'changeLevel':  { method: 'PUT'}
        });
    });

jhipsterApp.factory('AuditsService', function ($http) {
        return {
            findAll: function() {
                var promise = $http.get('/rest/api/admin/audits/all').then(function (response) {
                    return response.data;
                });
                return promise;
            },
            findByDates: function(fromDate, toDate) {
                var promise = $http.get('/rest/api/admin/audits/byDates', {params: {fromDate: fromDate, toDate: toDate}}).then(function (response) {
                    return response.data;
                });
                return promise;
            }
        }
    });

jhipsterApp.factory('Session', function () {
        this.create = function (login, firstName, lastName, email, userRoles) {
            this.login = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.userRoles = userRoles;
        };
        this.invalidate = function () {
            this.login = null;
            this.firstName = null;
            this.lastName = null;
            this.email = null;
            this.userRoles = null;
        };
        return this;
    });

jhipsterApp.factory('AuthenticationSharedService', function ($rootScope, $http, authService, Session, Account) {
        return {
            login: function (param) {
                var data ="j_username=" + encodeURIComponent(param.username) +"&j_password=" + encodeURIComponent(param.password) +"&_spring_security_remember_me=" + param.rememberMe +"&submit=Login";
                $http.post('app/authentication', data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    Account.get(function(data) {
                        Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
                        $rootScope.account = Session;
                        authService.loginConfirmed(data);
                    });
                }).error(function (data, status, headers, config) {
                    $rootScope.authenticationError = true;
                    Session.invalidate();
                });
            },
            valid: function (authorizedRoles) {

                $http.get('protected/authentication_check.gif', {
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    if (!Session.login) {
                        Account.get(function(data) {
                            Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
                            $rootScope.account = Session;
                            if (!$rootScope.isAuthorized(authorizedRoles)) {
                                // user is not allowed
                               $rootScope.$broadcast("event:auth-notAuthorized");
                            } else {
                                $rootScope.$broadcast("event:auth-loginConfirmed");
                            }
                        });
                    }else{
                        if (!$rootScope.isAuthorized(authorizedRoles)) {
                                // user is not allowed
                                $rootScope.$broadcast("event:auth-notAuthorized");
                        } else {
                                $rootScope.$broadcast("event:auth-loginConfirmed");
                        }
                    }
                }).error(function (data, status, headers, config) {
                    if (!$rootScope.isAuthorized(authorizedRoles)) {
                        $rootScope.$broadcast('event:auth-loginRequired', data);
                    }
                });
            },
            isAuthorized: function (authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    if (authorizedRoles == '*') {
                        return true;
                    }

                    authorizedRoles = [authorizedRoles];
                }

                var isAuthorized = false;
                angular.forEach(authorizedRoles, function(authorizedRole) {
                    var authorized = (!!Session.login &&
                        Session.userRoles.indexOf(authorizedRole) !== -1);

                    if (authorized || authorizedRole == '*') {
                        isAuthorized = true;
                    }
                });

                return isAuthorized;
            },
            logout: function () {
                $rootScope.authenticationError = false;
                $rootScope.authenticated = false;
                $rootScope.account = null;

                $http.get('app/logout');
                Session.invalidate();
                authService.loginCancelled();
            }
        };
    });
