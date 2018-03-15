'use strict';

angular.module('crudApp').controller('AdminController',
    ['AdminService', '$scope','$location','$window',  function( AdminService, $scope, $location, $window) {

        var self = this;
        
        self.cancelTrain = cancelTrain;
        self.resetCapacity = resetCapacity;
        self.cancelTrainId ='';
        self.capacity ='';
       
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        
        function cancelTrain() {
    		//alert("Cancel Train");
    		//alert(self.cancelTrainId);
    		AdminService.cancelTrain(self.cancelTrainId);
        }
        
        function resetCapacity() {
    		//alert("Reset Capacity");
    		//alert(self.Capacity);
    		AdminService.resetCapacity(self.capacity);
        }

    }
    ]);