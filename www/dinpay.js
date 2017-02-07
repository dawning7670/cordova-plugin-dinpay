
    var exec = require('cordova/exec');
    var className = "DinPay";
    var DinPay = {
        start: function () {
            cordova.exec(function(){}, function(){}, className, "start", []);
        }
    }
    module.exports = DinPay;