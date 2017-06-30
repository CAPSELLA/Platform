angular.module('CapsellaApp')
// Creating the Angular Controller
.controller('groupsController', function($http, $scope, AuthService) {
	var edit = false;
	$scope.buttonText = 'Create new Group';
	$scope.newGroup = true;
	var init = function() {
		$scope.isViewLoading = true;
		document.body.style.cursor = 'wait';
		$http.get('/capsella_authentication_service/getFullGroups').success(function(res) {

			$scope.groups = res;
			$scope.message = '';
			$scope.deleteMessage = "";
			$scope.newUser = true;
			$scope.appUser = null;
			$scope.group = null;
			$scope.buttonText = 'Create new Group';
			$scope.isViewLoading = false;
			document.body.style.cursor = 'default';

		}).error(function(error) {
			$scope.message = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor = 'default';
		});

		

	};
	
	$scope.initEdit = function(group) {
		edit = true;
		$scope.group = group;
		$scope.message='';
		$scope.newGroup = false;
		$scope.buttonText = 'Edit Group';
	};
	
	$scope.initAddGroup = function() {
		edit = false;
		$scope.group = null;
		$scope.message='';
		$scope.newGroup = true;
		$scope.buttonText = 'Add new Group';
	};

	$scope.createGroup = function() {

		if(!edit)
		{
			$scope.isViewLoading = true;
			document.body.style.cursor = 'wait';
	
			$http({
				url : 'createGroup',
				method : "POST",
				params : {
					groupName : $scope.group.newName,
					groupRights : $scope.group.roles
				}
			}).success(function(res) {
				$scope.group = null;
				$scope.isViewLoading = false;
				document.body.style.cursor = 'default';
				init();
	
			}).error(function(error) {
				$scope.message = error.message;
				$scope.isViewLoading = false;
				document.body.style.cursor = 'default';
	
			});
		}
		else
		{
			$scope.isViewLoading = true;
			document.body.style.cursor = 'wait';
	
			$http.post('/capsella_authentication_service/updateGroup', $scope.group).success(function(res) {
				$scope.group = null;
				$scope.isViewLoading = false;
				document.body.style.cursor = 'default';
				init();
	
			}).error(function(error) {
				$scope.message = error.message;
				$scope.isViewLoading = false;
				document.body.style.cursor = 'default';
	
			});
		}

	};
	
	
	$scope.deleteGroup = function(groupName) {
		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		$http({
			url : 'deleteGroup',
			method : "POST",
			params : {
				name : groupName,
			}
		}).success(function(res) {
			$scope.deleteMessage ="User has deleted!";
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
			init();
		}).error(function(error) {
			$scope.deleteMessage = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
	};
	
	init();

});