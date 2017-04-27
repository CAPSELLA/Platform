angular.module('CapsellaApp')
// Creating the Angular Controller
.controller(
		'NavController',
		function($http, $scope, AuthService, $state, $rootScope, $window) {

			$scope.$watch(function() {
				return AuthService.get("userToken") != undefined
			}, function(status) {

				if (status == true)
					if (AuthService.user != undefined)
						$scope.user = AuthService.user;

					else
						$scope.user = null;
			}, true);

			$scope.$on('LoginSuccessful', function() {
				$scope.user = AuthService.user;
			});
			$scope.$on('LogoutSuccessful', function() {
				$scope.user = null;
				$window.sessionStorage.clear();
			});
			$scope.logout = function() {

				$scope.isViewLoading = true;
				document.body.style.cursor = 'wait';
				$http
						.post('/capsella_authentication_service-dev/logOutUser',
								AuthService.user).success(function(res) {
							AuthService.user = null;
							$rootScope.$broadcast('LogoutSuccessful');
							$scope.isViewLoading = false;
							document.body.style.cursor = 'default';
							$state.go('login');

						}).error(function(error) {

							$scope.isViewLoading = false;
							document.body.style.cursor = 'default';
						});

			};
		});
