angular.module('CapsellaApp').config(function($stateProvider, $urlRouterProvider) {
	
	// the ui router will redirect if a invalid state has come.
	$urlRouterProvider.otherwise('/page-not-found');
	// parent view - navigation state
	$stateProvider.state('nav', {
		abstract : true,
		url : '',
		views : {
			'nav@' : {
				templateUrl : 'app/views/nav.html',
				controller : 'NavController'
			}
		}
	}).state('login', {
		parent : 'nav',
		url : '/login',
		views : {
			'content@' : {
				templateUrl : 'app/views/login.html',
				controller : 'LoginController'
			}
		}
	}).state('users', {
		parent : 'nav',
		url : '/users',
		data : {
			role : 'admin'
		},
		views : {
			'content@' : {
				templateUrl : 'app/views/users.html',
				controller : 'UsersController',
			}
		}
	}).state('registeredUsers', {
		parent : 'nav',
		url : '/registeredUsers',
		data : {
			role : 'admin'
		},
		views : {
			'content@' : {
				templateUrl : 'app/views/registeredUsers.html',
				controller : 'registeredUsersController',
			}
		}
	}).state('groups', {
		parent : 'nav',
		url : '/groups',
		data : {
			role : 'admin'
		},
		views : {
			'content@' : {
				templateUrl : 'app/views/groups.html',
				controller : 'groupsController',
			}
		}
		}).state('home', {
		parent : 'nav',
		url : '/',
		views : {
			'content@' : {
				templateUrl : 'app/views/home.html',
				controller : 'HomeController'
			}
		}
	}).state('page-not-found', {
		parent : 'nav',
		url : '/page-not-found',
		views : {
			'content@' : {
				templateUrl : 'app/views/page-not-found.html',
				controller : 'PageNotFoundController'
			}
		}
	}).state('access-denied', {
		parent : 'nav',
		url : '/access-denied',
		views : {
			'content@' : {
				templateUrl : 'app/views/access-denied.html',
				controller : 'AccessDeniedController'
			}
		}
	}).state('register', {
		parent : 'nav',
		url : '/register',
		views : {
			'content@' : {
				templateUrl : 'app/views/register.html',
				controller : 'RegisterController'
			}
		}
	}).state('generateToken', {
		parent : 'nav',
		url : '/generateToken',
		views : {
			'content@' : {
				templateUrl : 'app/views/generateToken.html',
				controller : 'GenerateTokenController'
			}
		}
	}).state('clientIds', {
		parent : 'nav',
		url : '/clientIds',
		views : {
			'content@' : {
				templateUrl : 'app/views/clientIds.html',
				controller : 'ClientIdsController'
			}
		}
	});;
});
