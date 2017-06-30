angular.module('CapsellaApp')
// Creating the Angular Controller
.controller('ClientIdsController', function($http, $scope, AuthService) {
	
	var init = function() {
		$scope.isViewLoading = true;
		document.body.style.cursor = 'wait';
		$http({
			url : 'getUserClientIds',
			method : "POST",
			params : {
				username : AuthService.user.username
			}
		}).success(function(res) {

			$scope.clientIds = res;
			
			$scope.isViewLoading = false;
			document.body.style.cursor = 'default';

		}).error(function(error) {
			$scope.message = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor = 'default';
		});

		

	};
	init();
	
	$scope.deleteClientId = function(token) {
		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		$http({
			url : 'deleteClientId',
			method : "POST",
			params : {
				clientIdToken : token
			}
		}).success(function(res) {
			$scope.deleteMessage ="Token has deleted!";
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
			init();
		}).error(function(error) {
			$scope.deleteMessage = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
	};
	
});