var exec = require('cordova/exec');
var className = "DinPay";
var DinPay = {
    pay: function (success, err, param) {
        exec(success, err, className, "pay", [param]);
    }
}
module.exports = DinPay;